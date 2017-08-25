package com.gp.info;

import java.net.URI;

import com.gp.info.InfoId;
import com.gp.util.StorageUtils;

import junit.framework.TestCase;

public class BinaryStoreIdTest extends TestCase{
	
	public void test1()throws Exception{
		
		InfoId<Integer> i = new InfoId<Integer>("m", 12);
		
		System.out.println(i);
		
		InfoId<Integer> t = InfoIds.parseInfoId(i.toString(), Integer.class);
		
		System.out.println(t);
		
		InfoId<Long> t1 = InfoIds.parseInfoId("USER:2345", Long.class);
		
		InfoId<Long> t11 = InfoIds.parseInfoId("USER", Long.class);
		
		InfoId<String> t2 = InfoIds.parseInfoId(i.toString(), String.class);

		String pak = StorageUtils.toPathKey(367);
		System.out.println("pathkey:" + pak);
		String pak0 = StorageUtils.toPureKey(pak);
		System.out.println("pathkey:" + pak0);
		String pak1 = StorageUtils.toPureKey(367);
		System.out.println("purekey:" + pak1);
		
		String u1 = StorageUtils.toURIStr(new InfoId<Integer>("storage",123), pak);
		System.out.println(u1);
		URI u = new URI(u1);
		System.out.println(u.getScheme());
		System.out.println(u.getPort());
		System.out.println(u.getPath());
		
		u1 = StorageUtils.toURIStr(new InfoId<Integer>("storeage",123), 21l);
		System.out.println(u1);
		u = new URI(u1);
		System.out.println(u.getScheme());
		System.out.println(u.getPort());
		System.out.println(u.getPath());
		
		u1 = StorageUtils.toURIStr(new InfoId<Integer>("storeage",123), 1123l);
		System.out.println(u1);
		u = new URI(u1);
		System.out.println(u.getScheme());
		System.out.println(u.getPort());
		System.out.println(u.getPath());
	}
}
