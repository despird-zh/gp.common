package com.gp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class BufferInputStream extends InputStream {
	
	private static final int BUF_SIZE = 4 * 1024; // 4K
	
	private ByteBuffer bbuf;
	private int capacity;
	
	public BufferInputStream(ByteBuffer buf) {
		this.bbuf = buf;
		this.capacity = buf.capacity();
	}

	public BufferInputStream(ByteBuffer buf, int capacity) {
		this.bbuf = buf;
		this.capacity = capacity;
	}
	
	@Override
	public int read() throws IOException {
		if (!bbuf.hasRemaining() || bbuf.position() >= capacity) {
			return -1;
		}
		return bbuf.get() & 0xFF;
	}

	@Override
	public int read(byte[] bytes, int off, int len) throws IOException {
		
		if (!bbuf.hasRemaining() || bbuf.position() >= capacity) {
			return -1;
		}

		len = Math.min(len, bbuf.remaining());
		len = Math.min(len, this.capacity - bbuf.position());
		bbuf.get(bytes, off, len);
		return len;
	}
	
	@Override
	public void close(){
		this.bbuf = null;
		this.capacity = 0;
	}
	
	/**
	 * Read the buffer bytes from this InputStream to the OutputStream
	 * 
	 * @param to the target output stream
	 * 
	 * @return the length of read content.
	 **/
	public long readToStream(OutputStream to)throws IOException {
		
	    byte[] buf = new byte[BUF_SIZE];
	    long total = 0;
	    while (true) {
	      int r = read(buf);
	      if (r == -1) {
	        break;
	      }
	      
	      to.write(buf, 0, r);
	      total += r;
	    }
	    return total;
	}
}
