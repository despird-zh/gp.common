package com.gp.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import com.gp.util.ImageUtils;

public class ImageUtilsTest {
	
	public static void main(String[] args){
		test2();
	}
	
	
	public static void test2(){
		
		try {
			String uffix =ImageUtils.detect("http://b.hiphotos.baidu.com/baike/w%3D268/sign=784fdfd934d3d539c13d08c50286e927/8c1001e93901213f09abb1fb56e736d12f2e9579.jpg");
	
			System.out.println("---=== : " + uffix);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public static void test1(){
		
		BufferedImage img = ImageUtils.read("d:\\2015-11-28_180537.png");
		BufferedImage img1 = ImageUtils.rotate(img, 45);
		ImageUtils.write(img1, "d:\\img0.png", "png");
		BufferedImage img2 = ImageUtils.crop(img1, 300, 300, 100, 100);
		ImageUtils.write(img2, "d:\\img1.png", "png");
		BufferedImage img3 = ImageUtils.crop(img1, 600, 300, 100, 100);
		ImageUtils.write(img3, "d:\\img2.png", "png");
		BufferedImage img4 = ImageUtils.crop(img1, 900, 300, 100, 100);
		ImageUtils.write(img4, "d:\\img3.png", "png");
		System.out.println("print done ");		
		
	}
}
