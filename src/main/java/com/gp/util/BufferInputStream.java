package com.gp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class BufferInputStream extends InputStream {
	
	private static final int BUF_SIZE = 0x1000; // 4K
	
	ByteBuffer bbuf;

	public BufferInputStream(ByteBuffer buf) {
		this.bbuf = buf;
	}

	public int read() throws IOException {
		if (!bbuf.hasRemaining()) {
			return -1;
		}
		return bbuf.get() & 0xFF;
	}

	public int read(byte[] bytes, int off, int len) throws IOException {
		if (!bbuf.hasRemaining()) {
			return -1;
		}

		len = Math.min(len, bbuf.remaining());
		bbuf.get(bytes, off, len);
		return len;
	}
	
	/**
	 * read the buffer bytes to output stream
	 * @param to output stream
	 *  
	 **/
	public int readToStream(OutputStream to)throws IOException {
		
	    byte[] buf = new byte[BUF_SIZE];
	    int total = 0;
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