package com.ltnw.common;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class AESCBCLib 
{
	public String Encrypt(String data,String key,String iv)
	{
		 try {

	            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
	            int blockSize = cipher.getBlockSize();

	            byte[] dataBytes = data.getBytes();
	            int plaintextLength = dataBytes.length;
	            if (plaintextLength % blockSize != 0) {
	                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
	            }

	            byte[] plaintext = new byte[plaintextLength];
	            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
	            
	            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
	            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

	            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
	            byte[] encrypted = cipher.doFinal(plaintext);
	           
	            return  android.util.Base64.encodeToString(encrypted, Base64.DEFAULT);
//	            return new BASE64Encoder().encode(encrypted);

	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	}
	
	public String DEncrypt(String data,String key,String iv)
	{
		 try
	        {
			 
//	            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);
			 	byte[] encrypted1 =android.util.Base64.decode(data, Base64.DEFAULT);
			 
//			 	byte[] encrypted1=hexToBytes(data);
			 	
	            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
	            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
	            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
	            
	            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
	 
	            byte[] original = cipher.doFinal(encrypted1);
	            String originalString = new String(original);
	            return originalString;
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	}
	
	public byte[] hexToBytes(String str) 
	{  
        if (str==null) {  
                return null;  
        } else if (str.length() < 2) {  
                return null;  
        } else {  
                int len = str.length() / 2;  
                byte[] buffer = new byte[len];  
                for (int i=0; i<len; i++) {  
                        buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);  
                }  
                return buffer;  
        }  
    }  
	
	
}
