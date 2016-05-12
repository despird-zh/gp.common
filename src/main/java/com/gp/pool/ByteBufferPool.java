package com.gp.pool;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp.exception.PoolException;
import com.gp.util.CommonUtils;

/**
 * ByteBufferPool provides byteBuffer with fixed length
 * 
 * @author gary diao
 * @version 0.1 2015-12-23
 * 
 **/
public class ByteBufferPool extends AbstractPool<ByteBuffer>{
	
	static Logger LOGGER = LoggerFactory.getLogger(ByteBufferPool.class);

	private ByteBufferBuilder supplier ;
	private ScheduledExecutorService executorService;
	// 30 seconds
	final long validationInterval = 5;

	public ByteBufferPool(ByteBufferBuilder supplier,int min, int max, long maxWait) {		

        super(supplier, min, max, maxWait);
        this.supplier = supplier;
		// monitor the queue to keep only min idle available.
		queueMonitor();
	}
	

    @Override
    public void release(ByteBuffer item) {
    
        item.clear();
        if(LOGGER.isDebugEnabled())
        	LOGGER.debug("Return bytebuffer(size = {}) to blocking queue", CommonUtils.humanReadableByteCount(item.capacity()));

        super.release(item);
        
    }
    
    private void queueMonitor(){    	
    	// check pool conditions in a separate thread
    	executorService = Executors.newSingleThreadScheduledExecutor();
    	executorService.scheduleAtFixedRate(new Runnable()
    	{
    		@Override 
    		public void run() {
    			LOGGER.debug("Queue monitor start...");
    			try{
	    			int size = getCurrentIdle();
	    			if (size < getMinIdle()) {
	    				
	    				int sizeToBeAdded = getMinIdle() - size;
	    				LOGGER.debug("Shrink[append] the blocking queue[{}] - [+{}]", size, sizeToBeAdded);
	    				for (int i = 0; i < sizeToBeAdded; i++) {
	    					release(supplier.get());
	    				}
	    			} else if (size > getMaxIdle()) {
	    				int sizeToBeRemoved = size - getMaxIdle();
	    				LOGGER.debug("Shrink[remove] the blocking queue[{}] - [-{}]", size, sizeToBeRemoved);
	    				for (int i = 0; i < sizeToBeRemoved; i++) {
	    					ByteBuffer buffer = acquire();
	    					supplier.drop(buffer);
	    				}
	    			} else{
	    				
	    				LOGGER.debug("Shrink[nothing] the blocking queue[{}] - [0]", size);
	    			}
    			}catch(Exception ex){
    				// ignore 
    				LOGGER.error("Error during shrink the pool size", ex);
    			}
    			LOGGER.debug("Queue monitor end...");
    		}
    			
    	}, validationInterval, validationInterval, TimeUnit.SECONDS);

    }

	@Override
	protected ByteBuffer handleExhausted() throws PoolException {
		if(LOGGER.isDebugEnabled())
			LOGGER.debug("Create new byte buffer coz of exhaused, size = {}", getCurrentIdle());
		
		return supplier.get();
	}
}
