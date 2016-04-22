package com.gp.pool;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.exception.PoolException;
import com.gp.pool.BasePool;
import com.gp.util.CommonUtils;

/**
 * ByteBufferPool provides byteBuffer with fixed lengths. the length of buffer could be :128K, 512K, 1M, 2M, 4M.
 * 
 * @author gary diao
 * @version 0.1 2015-12-23
 * 
 **/
public class ByteBufferPool implements BasePool<ByteBuffer>{
	
	static Logger LOGGER = LoggerFactory.getLogger(ByteBufferPool.class);
	
    private final ByteBufferBuilder supplier;
    private final long maxWait;

    private Map<Integer, BlockingQueueWrapper> queuemap;	

	private int[] sizearray = null;
	
	private int defaultSize;
	
	private ScheduledExecutorService executorService;
	// 30 seconds
	final long validationInterval = 30;

	public ByteBufferPool(ByteBufferBuilder supplier,int[][] settingarray, long maxWait) {		

        this.supplier = supplier;
        this.maxWait = maxWait;

        queuemap = new HashMap<Integer, BlockingQueueWrapper>(5);
        this.sizearray = new int[settingarray.length];
        defaultSize = settingarray[0][0];
		// initialize the ByteBuffer queue
        for(int i = 0 ; i < settingarray.length; i++){
        	BlockingQueueWrapper wrapper = new BlockingQueueWrapper();
        	
        	wrapper.bufferSize = settingarray[i][0];
        	wrapper.minIdle = settingarray[i][1];
        	wrapper.maxIdle = settingarray[i][2];
        	
        	this.sizearray[i] = settingarray[i][0];
        	
        	wrapper.bufferQueue = new LinkedBlockingQueue<ByteBuffer>(wrapper.minIdle);
        	queuemap.put(wrapper.bufferSize, wrapper);
        }
		// allocate the ByteBuffer queue
        lazyAppendQueue(queuemap.values().toArray(new BlockingQueueWrapper[queuemap.size()]));
		// monitor the queue to keep only min idle available.
		queueMonitor();
	}
	
	/**
	 * handle in case of exhausted
	 * 
	 **/
	protected ByteBuffer handleExhausted(int buffersize) throws PoolException {
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Create new byte buffer coz of exhaused, size = {}", buffersize);
		
		return supplier.get(buffersize);
	}	

	/**
	 * append queue in extra thread. 
	 * 
	 **/
	private void lazyAppendQueue(final BlockingQueueWrapper ... queuewrapper ) {
    	
        Runnable toRun = new Runnable() {
			@Override
			public void run() {
				LOGGER.debug("Lazy append start ...");
				for(int i = 0; i < queuewrapper.length; i++){
					appendQueue(queuewrapper[i].bufferQueue, queuewrapper[i].minIdle, queuewrapper[i].bufferSize);
				}
				LOGGER.debug("Lazy append end ...");
			}
        };
        Thread t = new Thread(toRun);
        t.setDaemon(true);
        t.start();
    }
    
	/**
	 * append queue with specified buffer size. 
	 **/
    private void appendQueue(BlockingQueue<ByteBuffer> queue, int count, int buffersize) {
    	if(LOGGER.isDebugEnabled())
    		LOGGER.debug("Append bytebuffer to blocking queue : count -> {} / buffersize -> {}", count, 
    			CommonUtils.humanReadableByteCount(buffersize,false));
    	
    	for (int i = 0; i < count; i++) {
        	
            if (!queue.offer(supplier.get(buffersize))) {
                return;
            }
        }
    }

    @Override
    public void returnItem(ByteBuffer item) {
    	
        if (item == null) {
            return;
        }
        item.clear();
        if(LOGGER.isDebugEnabled())
        	LOGGER.debug("Return bytebuffer(size = {}) to blocking queue", CommonUtils.humanReadableByteCount(item.capacity()));
        BlockingQueueWrapper wrapper = queuemap.get(item.capacity());
        if(wrapper != null){
        	wrapper.bufferQueue.offer(item);
        }
        
    }

    /**
     * borrow item  
     **/
    public ByteBuffer borrowItem(final int buffersize) throws PoolException, InterruptedException {
       
    	if(!isValidSize(buffersize))
    		throw new PoolException("the buffer size["+ buffersize+ "] is not valid");
		
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Borrow buffer recommend size : {}", CommonUtils.humanReadableByteCount(buffersize));
		
		BlockingQueueWrapper wrapper = queuemap.get(buffersize);
				
    	ByteBuffer got = wrapper.bufferQueue.poll();

        // regardless of other things, add a lazy instance if the current state
        // is too low.
        if (wrapper.bufferQueue.size() < wrapper.minIdle) {
        	lazyAppendQueue(wrapper);
        }

        if (got != null) {
            return got;
        }
        if (maxWait > 0) {
            // Try again but block until there's something available.
            // The lazy add above may be the instance we pull out!
            // It is also possible that some other thread may steal the one we
            // added!
            got = wrapper.bufferQueue.poll(maxWait, TimeUnit.MILLISECONDS);

        }
        if (got != null) {
            return got;
        }

        return handleExhausted(buffersize);
    }
    
    /**
     * buffer size is supported  
     **/
    private boolean isValidSize(int buffersize){
    	
    	for(int i = 0; i < sizearray.length; i++){
    		
    		if(sizearray[i] == buffersize)
    			return true;
    	}
    	
    	return false;
    }
    
    /**
     * @return the index of the closest match to the given value
     */
//    @Deprecated
//    private int nearestSizeMatch(int[] array, int value) {
//        if (array.length == 0) {
//            throw new IllegalArgumentException();
//        }
//        int nearestMatchIndex = 0;
//        for (int i = 1; i < array.length; i++) {
//            if ( Math.abs(value - array[nearestMatchIndex])
//                    > Math.abs(value - array[i]) ) {
//                nearestMatchIndex = i;
//            }
//        }
//        return nearestMatchIndex;
//    }
    
    private void queueMonitor(){    	
    	// check pool conditions in a separate thread
    	executorService = Executors.newSingleThreadScheduledExecutor();
    	executorService.scheduleAtFixedRate(new Runnable()
    	{
    		@Override 
    		public void run() {
    			LOGGER.debug("Queue monitor start...");
    			for(BlockingQueueWrapper wrapper : queuemap.values()){
    				shrinkQueue(wrapper);
    			}
    			LOGGER.debug("Queue monitor end...");
    		}
    			
    	}, validationInterval, validationInterval, TimeUnit.SECONDS);

    }
    
    /**
     * shrink the blocking queue to proper size.
     **/
    private void shrinkQueue(BlockingQueueWrapper wrapper){
		// default buffer queue
		int size = wrapper.bufferQueue.size();
		if (size < wrapper.minIdle) {
			
			int sizeToBeAdded = wrapper.minIdle - size;
			LOGGER.debug("Shrink[append] the blocking queue[{}] - [{}]", wrapper.bufferSize, sizeToBeAdded);
			for (int i = 0; i < sizeToBeAdded; i++) {
				wrapper.bufferQueue.add(supplier.get(wrapper.bufferSize));
			}
		} else if (size > wrapper.maxIdle) {
			int sizeToBeRemoved = size - wrapper.maxIdle;
			LOGGER.debug("Shrink[remove] the blocking queue[{}] - [{}]", wrapper.bufferSize, sizeToBeRemoved);
			for (int i = 0; i < sizeToBeRemoved; i++) {
				ByteBuffer buffer = wrapper.bufferQueue.poll();
				supplier.drop(buffer);
			}
		} else{
			
			LOGGER.debug("Shrink[nothing] the blocking queue[{}] - [{}]", wrapper.bufferSize, size);
		}
    }
    
    private class BlockingQueueWrapper{
    	public Integer minIdle;
    	public Integer maxIdle;
    	public Integer bufferSize;
    	public BlockingQueue<ByteBuffer> bufferQueue;
    }

	@Override
	public ByteBuffer borrowItem() throws PoolException, InterruptedException {
		
		return borrowItem(defaultSize);
	}

	public int[][] getStatistics(){
		int[][] rtv = new int[queuemap.size()][2]; 
		int cnt = 0;
		for(BlockingQueueWrapper wrapper: queuemap.values()){
			rtv[cnt][0] = wrapper.bufferSize;
			rtv[cnt][1] = wrapper.bufferQueue.size();
			cnt ++;
		}
		
		return rtv;
	}
	/**
	 * Get the buffer size array 
	 **/
	public int[] getBufferSizes(){
				
		return this.sizearray.clone();
	}
}
