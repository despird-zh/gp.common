package com.gp.pool;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

public class ByteBufferBuilder implements Supplier<ByteBuffer>{

	private int defaultSize;
	
	public ByteBufferBuilder(int defaultSize){
		this.defaultSize = defaultSize;
	}
	
	public int getDefaultSize(){
		
		return this.defaultSize;
	}
	
	@Override
	public ByteBuffer get() {
		
		return ByteBuffer.allocate(defaultSize);
	}

	public void drop(ByteBuffer byteBuffer){
		//((sun.nio.ch.DirectBuffer) byteBuffer).cleaner().clean();
	}
}
