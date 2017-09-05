/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * 
 * Copyright 2011 - 2013 Aris-vn, Inc. All rights reserved.
 * Author: Tam-LT
 * Location: ARISSmartHiddenBox - jp.co.aris_kk.jni - CipherUtil.java
 * Date create: 3:00:19 PM - Nov 21, 2013 - 2013
 * 
 * 
 */
package jp.co.aris_kk.jni;

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * The Class CipherUtil.
 */
public class CipherUtil {

	static {
		System.loadLibrary("cipher");
    }
	
	/**
	 * 繝�く繧ｹ繝医ｒ證怜捷蛹悶☆繧�.
	 *
	 * @param context 繧ｳ繝ｳ繝�く繧ｹ繝�
	 * @param text 證怜捷蛹悶☆繧区枚蟄怜�
	 * @return 證怜捷蛹悶＆繧後◆譁�ｭ怜���yte驟榊���
	 */
	public static native byte[] encrypt(Context context,String text);
	
	/**
	 * 蠕ｩ蜿ｷ蛹悶☆繧�.
	 *
	 * @param context 繧ｳ繝ｳ繝�く繧ｹ繝�
	 * @param encryptedText 證怜捷蛹悶＆繧後◆譁�ｭ怜���yte驟榊���
	 * @return 蠕ｩ蜿ｷ蛹悶＠縺滓枚蟄怜�
	 */
	public static native String decrypt(Context context,byte[] encryptedText);
}
