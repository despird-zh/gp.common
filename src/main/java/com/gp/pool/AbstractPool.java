package com.gp.pool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import com.gp.exception.PoolException;

public abstract class AbstractPool<T> implements BasePool<T> {

    protected final Queue<T> queue;
    protected final Supplier<T> supplier;
    protected final long maxWait;
    protected final int minIdle;
    protected int maxIdle;

    ResizeableSemaphore semaphore = null; 
    
    protected AbstractPool(Supplier<T> supplier) {
        this(supplier, 1, Integer.MAX_VALUE, 0);
    }

    protected AbstractPool(Supplier<T> supplier, int minIdle, int maxIdle, long maxWait) {

        if (minIdle < 0 || minIdle >= maxIdle) {
            throw new IllegalArgumentException("minIdle must be non-negative, and also strictly less than maxIdle");
        }
        semaphore = new ResizeableSemaphore(minIdle);
        
        this.supplier = supplier;
        this.minIdle = minIdle;
        this.maxIdle = maxIdle;
        this.queue = new ConcurrentLinkedQueue<T>();
        this.maxWait = maxWait;

        // Set up a feed of input values.
        if (this.minIdle > 0) {
            lazyAdd(this.minIdle);
        }
    }

    public int getMinIdle() {
        return minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    synchronized void setMaxIdel(int newMax) {
        if (newMax < 1) {
            throw new IllegalArgumentException("Semaphore size must be at least 1,"
                + " was " + newMax);
        }
 
        int delta = newMax - this.maxIdle;
 
        if (delta == 0) {
            return;
        } else if (delta > 0) {
            // new max is higher, so release that many permits
            this.semaphore.release(delta);
        } else {
            delta *= -1;
            // delta < 0.
            // reducePermits needs a positive #, though.
            this.semaphore.reducePermits(delta);
        }
 
        this.maxIdle = newMax;
    }
    
    public int getCurrentIdle() {
        return semaphore.availablePermits();
    }

    /**
     * lazyAdd will start a background thread to add instances to the queue (if
     * possible). The cost of creating a thread is assumed to be insignificant
     * compared to the cost of creating the instance.
     * 
     * @param count
     *            the number of items to add to the pool
     */
    private void lazyAdd(final int count) {
        Runnable toRun = new Runnable() {

			@Override
			public void run() {
	            for (int i = 0; i < count; i++) {
	                if (!queue.offer(supplier.get())) {
	                    return;
	                }
	            }
			}
        };
        Thread t = new Thread(toRun);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void release(T item) {
        if (item == null) {
            return;
        }
        queue.offer(item);
        semaphore.release();
    }

    @Override
    public T acquire() throws PoolException, InterruptedException {
        
    	// First, get permission to take or create a resource
        semaphore.tryAcquire(maxWait, TimeUnit.MILLISECONDS);

    	T got = queue.poll();
    	
        if (got != null) {
            return got;
        }

        return handleExhausted();
    }

    @Override
    public String toString() {
        return String.format("Pool with %d items and min %d, max %d wait %dms", queue.size(), minIdle, maxIdle,
                maxWait);
    }

    /**
     * What to do if there's nothing to return within the expected time. options
     * are to create an instance manually, or alternatively throw an exception.
     * returning null would be a bad idea, though.
     * 
     * @param supplier
     *            the supplier that feeds this pool - can be used to get a
     *            guaranteed, but slow instance.
     * @return the value for the client to use
     * @throws Poolxception
     *             if that is how the pool instance should respond.
     */
    protected abstract T handleExhausted() throws PoolException;

    /**
     * This is a workaround to reset the semaphore permits limit. 
     **/
    private static final class ResizeableSemaphore extends Semaphore {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
 
        /**
         * Create a new semaphore with 0 permits.
         */
        ResizeableSemaphore(int perms) {
            super(perms,true);
        }
        
        @Override
        protected void reducePermits(int reduction) {
            super.reducePermits(reduction);
        }
    }
}