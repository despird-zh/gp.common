package com.gp.pool;

import com.gp.exception.PoolException;

public interface BasePool<T> {

    T acquire() throws PoolException, InterruptedException;
    
    void release(T item);

}
