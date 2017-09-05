/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - com.arisvn.arissmarthiddenbox.encryption - Encryption.java
 * Date create: 4:15:37 PM - Sep 27, 2013 - 2013
 * 
 * 
 */

package com.arisvn.arissmarthiddenbox.encryption;

import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;

/**
 * The Class Encryption.
 */
public class Encryption {
    public static final int BYTES_TO_ENCRYPT_AUDIO = 262144;//1024
    
    public static final int BYTES_TO_ENCRYPT_PHOTO = 1024;
    
    public static final int BYTES_TO_ENCRYPT_VIDEO = 1024;

    private static final String ENCRYPT_MODE = "AES/CTR/NoPadding";

    private Cipher cipher;

    private IvParameterSpec ivspec;

    private SecretKey key;

    private static byte[] keyValue;
    String decryption_key = "018812818E72F429ABA0FB0AB34E9F37F11B874F756A47DE989D15EA90A0D16C";
    Context mContext;

    /**
     * Instantiates a new encryption.
     */
    public Encryption(Context context) {
    	mContext = context;
        try {
            initEncryption();
            setKey();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    /**
     * Inits the encryption.
     */
    private void initEncryption()
    {
        try {
        	String key = mContext.getPackageName();
        	keyValue = decrypt(key, decryption_key).getBytes();
            this.cipher = Cipher.getInstance(ENCRYPT_MODE);
            this.ivspec = new IvParameterSpec("fedcba9876543210".getBytes());
            return;
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
        } catch (NoSuchPaddingException localNoSuchPaddingException) {
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Sets the key.
     * 
     * @throws IllegalArgumentException the illegal argument exception
     */
    private void setKey() throws IllegalArgumentException {
        this.key = new SecretKeySpec(keyValue, "AES/CTR/NoPadding");
    }

    /**
     * Gets the required decrypted bytes from byte.
     * 
     * @param paramArrayOfByte the param array of byte
     * @return the required decrypted bytes from byte
     */
    public byte[] getRequiredDecryptedBytesFromByte(byte[] paramArrayOfByte) {
        byte[] arrayOfByte = null;
        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.key, this.ivspec);
            arrayOfByte = new byte[paramArrayOfByte.length];
            CipherInputStream localCipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(paramArrayOfByte), this.cipher);
            localCipherInputStream.read(arrayOfByte);
            localCipherInputStream.close();
        } catch (Exception localException) {
            localException.printStackTrace();
        } finally {
        }
        return arrayOfByte;
    }

    /**
     * Gets the required encrypted bytes from byte.
     * 
     * @param paramArrayOfByte the param array of byte
     * @return the required encrypted bytes from byte
     */
    public byte[] getRequiredEncryptedBytesFromByte(byte[] paramArrayOfByte)

    {
        byte[] arrayOfByte = null;
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.key, this.ivspec);
            arrayOfByte = new byte[paramArrayOfByte.length];
            CipherInputStream localCipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(paramArrayOfByte), this.cipher);
            localCipherInputStream.read(arrayOfByte);
            localCipherInputStream.close();
            return arrayOfByte;
        } catch (Exception localException) {
            localException.printStackTrace();
        } finally {
        }
        return arrayOfByte;
    }
    
	public static String encrypt(String seed, String cleartext) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes("UTF-8"));
		byte[] result = encrypt(rawKey, cleartext.getBytes());
		return toHex(result);
	}

	public static String decrypt(String seed, String encrypted) throws Exception {
		byte[] rawKey = getRawKey(seed.getBytes("UTF-8"));
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}
    
	private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}
    
	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}
	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length()/2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2*buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}
	private final static String HEX = "0123456789ABCDEF";
	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
	}

}
