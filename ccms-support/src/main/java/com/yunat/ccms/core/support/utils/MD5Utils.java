package com.yunat.ccms.core.support.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.springframework.util.Assert;

import com.yunat.ccms.core.support.cons.EncodeName;
import com.yunat.ccms.core.support.exception.CcmsBusinessException;

public class MD5Utils {

	public static final String MD5 = "MD5";

	public static byte[] md5ToBytes(final String source, final EncodeName encodeName) throws IllegalArgumentException {
		Assert.notNull(source);
		Assert.notNull(encodeName);
		try {
			final MessageDigest md = MessageDigest.getInstance(MD5);
			return md.digest(source.getBytes(encodeName.getName()));
		} catch (final NoSuchAlgorithmException e) {
			// impossible
			e.printStackTrace();
			return null;
		} catch (final UnsupportedEncodingException e) {
			// impossible
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] md5ToBytes(final String source) throws IllegalArgumentException {
		return md5ToBytes(source, EncodeName.UTF8);
	}

	public static byte[] md5ToBytes(final EncodeName encodeName, final Object... source)
			throws IllegalArgumentException {
		Assert.notNull(source);
		return md5ToBytes(Arrays.toString(source), encodeName);
	}

	public static byte[] md5ToBytes(final Object... source) throws IllegalArgumentException {
		return md5ToBytes(EncodeName.UTF8, source);
	}

	// TODO:这个方法跟EncodeByMd5的作用应该是一样的
	public static String md5ToHexString(final String source, final EncodeName encodeName)
			throws IllegalArgumentException {
		final byte[] b = md5ToBytes(source, encodeName);
		final StringBuilder sb = new StringBuilder();
		for (final byte element : b) {
			final int i = element & 0xFF;
			if (i < 16) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(i));
		}
		return sb.toString();
	}

	public static String md5ToHexString(final String source) throws IllegalArgumentException {
		return md5ToHexString(source, EncodeName.UTF8);
	}

	// TODO:这个方法跟md5ToHexString作用应该是一样的.
	public static String EncodeByMd5(final String str) {
		// 确定计算方法
		try {
			final MessageDigest md5 = MessageDigest.getInstance(MD5);
			md5.update(str.getBytes());
			final byte[] md5Hash = md5.digest();
			String md5Result = "";
			for (final byte element : md5Hash) {
				final int v = element & 0xFF;
				if (v < 16) {
					md5Result += "0";
				}
				md5Result += Integer.toString(v, 16);
			}
			return md5Result;

		} catch (final Exception e) {
			throw new CcmsBusinessException("MD5计算方法初始化错误", e);
		}

	}

	/**
	 * 判断用户密码是否正确
	 * 
	 * @param newpasswd
	 *            用户输入的密码
	 * @param oldpasswd
	 *            数据库中存储的密码－－用户密码的摘要
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean checkpassword(final String newpasswd, final String oldpasswd)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if (EncodeByMd5(newpasswd).equals(oldpasswd)) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(final String[] args) throws Exception {
		System.out.println(MD5Utils.EncodeByMd5("beimei"));
	}
}
