package com.gp.info;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.gp.util.Base64;
import com.gp.util.ByteString;
import com.gp.util.HashUtils;

import junit.framework.TestCase;

public class HashTest  extends TestCase{

	public void test1() throws Exception{
		
		String salt = "salt0123";
		
		String pwd = "demopwd";
		
		byte[] hashbytes = HashUtils.hash(pwd.toCharArray(), salt.getBytes(StandardCharsets.UTF_8));

		String hashStr = Base64.encode(hashbytes);
		System.out.println("after encode "+hashStr);
		
		byte[] hashbytes2 = Base64.decode(hashStr);
		
		boolean s3 = Arrays.equals(hashbytes, hashbytes2);
		System.out.println(" --equals :" + s3);
		
		boolean same = HashUtils.isExpectedPassword(pwd.toCharArray(), 
				salt.getBytes(StandardCharsets.UTF_8), 
				hashbytes2);
		
		System.out.println(" same :" + same);
	}
	
	@Test
	public void test2(){
		
		HashFunction hf = Hashing.sha1();
		HashCode hc = hf.newHasher()
		       .putString("ehome", Charsets.UTF_8)
		       .putString("nhome", Charsets.UTF_8)
		       .putLong(234l)
		       .hash();
		
		byte[] hbytes = hc.asBytes();
		String hbstr = ByteString.of(hbytes).hex();
		
		System.out.println(hbstr);		
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        boolean result = verifyChecksum("/home/eclipse-jee-indigo-SR2-linux-gtk-x86_64.tar.gz", "177750b65a21a9043105fd0820b85b58cf148ae4");
        System.out.println("Does the file's checksum matches the expected one? " + result);
    }
     
    /**
     * Verifies file's SHA1 checksum
     * @param Filepath and name of a file that is to be verified
     * @param testChecksum the expected checksum
     * @return true if the expeceted SHA1 checksum matches the file's SHA1 checksum; false otherwise.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static boolean verifyChecksum(String file, String testChecksum) throws NoSuchAlgorithmException, IOException
    {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        FileInputStream fis = new FileInputStream(file);
  
        byte[] data = new byte[1024];
        int read = 0; 
        while ((read = fis.read(data)) != -1) {
            sha1.update(data, 0, read);
        };
        byte[] hashBytes = sha1.digest();
  
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < hashBytes.length; i++) {
          sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        String fileHash = sb.toString();
         
        return fileHash.equals(testChecksum);
    }
}
