package com.gp.pool;

import com.gp.exception.PoolException;

public interface BasePool<T> {

    T borrowItem() throws PoolException, InterruptedException;
    void returnItem(T item);

}
