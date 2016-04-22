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
	
	public ByteBuffer get(int bufferSize) {
		
		return ByteBuffer.allocate(bufferSize);
	}
	
	public void drop(ByteBuffer byteBuffer){
		// ignore
	}
}
