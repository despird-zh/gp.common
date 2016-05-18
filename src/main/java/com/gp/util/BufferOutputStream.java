package com.gp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class BufferOutputStream extends OutputStream {

	private static final int BUF_SIZE = 4 * 1024; // 4K

	private ByteBuffer bbuf;
	
	private int capacity;
	
	public BufferOutputStream(ByteBuffer buf) {
		this.bbuf = buf;
		this.capacity = buf.capacity();
	}

	public BufferOutputStream(ByteBuffer buf, int capacity) throws IOException{
		if(bbuf.capacity() < capacity)
			throw new IOException("buffer overflow capacity:"+bbuf.capacity());
		
		this.bbuf = buf;
		this.capacity = capacity;
	}
	
	@Override
	public void write(int b) throws IOException {
		if(bbuf.position() + 4 > capacity)
			throw new IOException("buffer overflow current position is:"+bbuf.position());
		bbuf.put((byte) b);
	}

	@Override
	public void write(byte[] bytes, int off, int len) throws IOException {
		
		if(bbuf.position() + len > capacity)
			throw new IOException("buffer overflow current position is:"+bbuf.position());
		
		bbuf.put(bytes, off, len);
	}

	@Override
	public void close(){
		this.bbuf = null;
		this.capacity = 0;
	}
	
	/**
	 * Write the current OutputStream with an InputStream.
	 * 
	 * @param from the input stream
	 * 
	 * @return long the length of written content.
	 **/
	public long writeFromStream(InputStream from) throws IOException {
		byte[] buf = new byte[BUF_SIZE];
		long total = 0;
		int available = this.capacity;
		while (available > 0) {
			
			int r = available > BUF_SIZE ? 
					from.read(buf):
					from.read(buf, 0, available);
			if (r == -1) {
				break;
			}
			write(buf, 0, r);
			total += r;
			available -= r;
		}
		return total;
	}
}
