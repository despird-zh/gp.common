package com.gp.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import com.gp.exception.PoolException;

public abstract class AbstractPool<T> implements BasePool<T> {

    protected final BlockingQueue<T> queue;
    protected final Supplier<T> supplier;
    protected final long maxWait;
    protected final int minIdle;
    protected final int maxIdle;

    protected AbstractPool(Supplier<T> supplier) {
        this(supplier, 1, Integer.MAX_VALUE, 0);
    }

    protected AbstractPool(Supplier<T> supplier, int minIdle, int maxIdle, long maxWait) {

        if (minIdle < 0 || minIdle >= maxIdle) {
            throw new IllegalArgumentException("minIdle must be non-negative, and also strictly less than maxIdle");
        }

        this.supplier = supplier;
        this.minIdle = minIdle;
        this.maxIdle = maxIdle;
        this.queue = new LinkedBlockingQueue<>(maxIdle);
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

    public int getCurrentIdle() {
        return queue.size();
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
    }

    @Override
    public T acquire() throws PoolException, InterruptedException {
        T got = queue.poll();

        // regardless of other things, add a lazy instance if the current state
        // is too low.
        if (queue.size() < minIdle) {
            lazyAdd(1);
        }

        if (got != null) {
            return got;
        }
        if (maxWait > 0) {
            // Try again but block until there's something available.
            // The lazy add above may be the instance we pull out!
            // It is also possible that some other thread may steal the one we
            // added!
            got = queue.poll(maxWait, TimeUnit.MILLISECONDS);

        }
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

}