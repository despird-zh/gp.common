package com.gp.util;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.gp.util.BufferOutputStream;

import junit.framework.TestCase;

public class BufferStreamTest extends TestCase{

	public void test1(){
		
		ByteBuffer bbuf = ByteBuffer.allocate(10);
		
		BufferOutputStream bos = new BufferOutputStream(bbuf);
		
		for(int i = 0; i<10; i++){
			try {
				
				bos.write(i);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
