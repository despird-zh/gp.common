package com.gp.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to help manipulate the image file. support resize rotate etc.
 * 
 * @author despird
 * @version 0.1 2015-3-4
 * 
 **/
public class ImageUtils {
	
	static Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);
	
	/**
	 * Crop the image and return the cropped one
	 * 
	 * @param image The image of source
	 * @param startX the start of x
	 * @param startY the start of y
	 * @param width the width of target image
	 * @param height the height of target image
	 * 
	 * @return The cropped image
	 **/
	public static BufferedImage crop(BufferedImage image, int startX, int startY, int width, int height){
		
		 BufferedImage dest = image.getSubimage(startX, startY, width, height);
	     return dest; 
	}
	
	/**
	 * Crop the image and return the cropped to new one
	 * 
	 * @param image The image of source
	 * @param startX the start of x
	 * @param startY the start of y
	 * @param width the width of target image
	 * @param height the height of target image
	 * 
	 * @return The cropped image
	 **/
	public static BufferedImage cropNew(BufferedImage image, int startX, int startY, int width, int height){
		
		 BufferedImage dest = image.getSubimage(startX, startY, width, height);
		 BufferedImage copyOfImage = new BufferedImage(dest.getWidth(), dest.getHeight(), BufferedImage.TYPE_INT_RGB);
		 Graphics2D g = copyOfImage.createGraphics();
		 g.drawImage(dest, 0, 0, null);
		 g.dispose();
		 return copyOfImage; //or use it however you want
	}
	
	/**
     * Resizes the image
     * @param filePath File path to the image to resize
     * @param w Width of the image
     * @param h Height of the image
     * @return A resized image
     */
	public static BufferedImage resize(String filePath, int width, int height) {

		BufferedImage  bufdest = read(filePath);
		return resize(bufdest, width, height);

	}
    
	/**
     * Resizes the image
     * @param filePath File path to the image to resize
     * @param w Width of the image
     * @param h Height of the image
     * @return A resized image
     */
	public static BufferedImage resize(BufferedImage bufsrc, int width, int height) {

		BufferedImage  bufdest;
		// scale the image
		try {

			bufdest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufdest.createGraphics();
			AffineTransform at = AffineTransform.getScaleInstance((double) width / bufsrc.getWidth(),
					(double) height / bufsrc.getHeight());
			g.drawRenderedImage(bufsrc, at);
			g.dispose();
			return bufdest;
		} catch (Exception e) {
			LOGGER.error("error resize the image.", e);
			// restore the old background
			return null;
		}

	}
	
	/**
     * Rotate the image
     * @param image Image object to be processed
     * @param angle The degree to rotate
     * 
     * @return A rotated image
     */
	public static BufferedImage rotate(BufferedImage image, int angle) {
		double rotationRequired = Math.toRadians (45);
		return rotate(image, rotationRequired);
	}
	
	/**
     * Rotate the image
     * @param image Image object to be processed
     * @param angle The angle to rotate
     * 
     * @return A rotated image
     */
	public static BufferedImage rotate(BufferedImage image, double angle) {
		
		double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
		
		int w = image.getWidth(), h = image.getHeight();	
		int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
		BufferedImage result = null;
		Graphics2D g = null;
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		GraphicsDevice gs = ge.getDefaultScreenDevice();
//		GraphicsConfiguration gc = gs.getDefaultConfiguration();
//		gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
		try {
			result = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_RGB);
			g = result.createGraphics();
			g.translate((neww - w) / 2, (newh - h) / 2);
			g.rotate(angle, w / 2, h / 2);
			g.drawRenderedImage(image, null);
			
		} finally {
			
			g.dispose();
		}
		
		return result;
	}

	/**
     * Save the image to a file
     * @param image Image object to be saved
     * @param filepath The File path to save image
     * @param imagetype The Image format type
     * 
     * @return true: success ; false : fail
     */
	public static boolean write(BufferedImage image, String filepath, String imagetype)
	{
		Path path = Paths.get(filepath);
		if(null == image || Files.exists(path) || StringUtils.isBlank(imagetype)){
			LOGGER.debug("parameters are not fully qualified.");
			return false;
		}
	    try 
	    {
	        File outputfile = new File(filepath);
	        ImageIO.write(image, imagetype, outputfile);
	        return true;
	        
	    } catch (IOException e) 
	    {
	    	LOGGER.error("fail save the image to : " + filepath, e);	    	
	    }
	    return false;
	}
	
	/**
	 * Load file into a buffer image
	 * @param filePath File path of target file
	 * @return the buffered image  
	 **/
	public static BufferedImage read(String filePath){
		
		BufferedImage bufsrc = null;
		
		try {
			bufsrc = ImageIO.read(new File(filePath));
			return bufsrc;
		} catch (IOException e) {
			LOGGER.error("error resize the image.", e);
			// restore the old background
			return null;
		}
		
	}
	
	/**
	 * Load file into a buffer image
	 * @param fileurl File link of target image
	 * @return the buffered image  
	 **/
	public static BufferedImage read(URL fileurl){
		
		BufferedImage bufsrc = null;
		
		try {
			bufsrc = ImageIO.read(fileurl);
			return bufsrc;
		} catch (IOException e) {
			LOGGER.error("error resize the image.", e);
			// restore the old background
			return null;
		}
		
	}
	
	/**
	 * detect the suffix of file 
	 **/
	public static String detect(String link) throws MalformedURLException, IOException{
		
		URL url = new URL(link);
		URLConnection conn = url.openConnection();
		String contentType = conn.getContentType();
		
		String suffix = null;
		Iterator<ImageReader> readers = ImageIO.getImageReadersByMIMEType(contentType);
		
		while (suffix == null && readers.hasNext()) {
		    ImageReaderSpi provider = readers.next().getOriginatingProvider();
		    if (provider != null) {
		        String[] suffixes = provider.getFileSuffixes();
		        if (suffixes != null) {
		            suffix = suffixes[0];
		        }
		    }
		}
		return suffix;
	}
}
