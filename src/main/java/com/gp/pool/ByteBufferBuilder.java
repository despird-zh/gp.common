package com.gp.pool;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

public class ByteBufferBuilder implements Supplier<ByteBuffer>{

	private int defaultSize;
	
	private boolean directEnable = false;
	
	public ByteBufferBuilder(int defaultSize){
		this.defaultSize = defaultSize;
	}
	
	public ByteBufferBuilder(int defaultSize, boolean directEnable){
		this.defaultSize = defaultSize;
		this.directEnable = directEnable;
	}
	
	public int getDefaultSize(){
		
		return this.defaultSize;
	}
	
	@Override
	public ByteBuffer get() {
		
		if(this.directEnable)
			return ByteBuffer.allocateDirect(defaultSize);
		else
			return ByteBuffer.allocate(defaultSize);
	}

	public void drop(ByteBuffer byteBuffer){
		if(this.directEnable)
			((sun.nio.ch.DirectBuffer) byteBuffer).cleaner().clean();
	}
}
