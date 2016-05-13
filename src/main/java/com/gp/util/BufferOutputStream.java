package com.gp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class BufferOutputStream extends OutputStream {

	private static final int BUF_SIZE = 0x1000; // 4K

	ByteBuffer bbuf;

	public BufferOutputStream(ByteBuffer buf) {
		this.bbuf = buf;
	}

	@Override
	public void write(int b) throws IOException {
		bbuf.put((byte) b);
	}

	@Override
	public void write(byte[] bytes, int off, int len) throws IOException {
		bbuf.put(bytes, off, len);
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
		int available = this.bbuf.capacity();
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
