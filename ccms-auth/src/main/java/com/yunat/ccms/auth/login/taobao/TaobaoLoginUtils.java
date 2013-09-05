package com.yunat.ccms.auth.login.taobao;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.taobao.api.internal.util.codec.Base64;
import com.yunat.ccms.core.support.cons.EncodeName;
import com.yunat.ccms.core.support.exception.CcmsRuntimeException;
import com.yunat.ccms.core.support.utils.MD5Utils;

/**
 * 淘宝登录参数验证和解析的类
 * 
 * @author ruiming.lu
 */

@SuppressWarnings("restriction")
public class TaobaoLoginUtils {

	private static Logger log = LoggerFactory.getLogger(TaobaoLoginUtils.class);

	/**
	 * 签名运算
	 * 
	 * @param parameter
	 * @param secret
	 * @return
	 * @throws CcmsRuntimeException
	 */
	public static String sign(final String parameter, final String secret) throws Exception {
		// 对参数+密钥做MD5运算
		final byte[] digest = MD5Utils.md5ToBytes(parameter + secret);
		// 对运算结果做BASE64运算并加密
		final Base64 encode = new Base64();
		return new String(encode.encode(digest));
	}

	/**
	 * 验证签名
	 * 
	 * @param sign
	 * @param parameter
	 *            top_appkey+top_parameter+top_session
	 * @param secret
	 * @return
	 * @throws CcmsRuntimeException
	 */
	public static boolean validateSign(final String sign, final String parameter, final String secret) throws Exception {
		return sign != null && parameter != null && secret != null && sign.equals(sign(parameter, secret));
	}

	/**
	 * 验证TOP回调地址的签名是否合法。要求所有参数均为已URL反编码的。
	 * 
	 * @param topParams
	 *            TOP私有参数（未经BASE64解密）
	 * @param topSession
	 *            TOP私有会话码
	 * @param topSign
	 *            TOP回调签名
	 * @param appKey
	 *            应用公钥
	 * @param appSecret
	 *            应用密钥
	 * @return 验证成功返回true，否则返回false
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static boolean verifyTopResponse(final String topParams, final String topSession, final String topSign,
			final String appKey, final String appSecret) throws NoSuchAlgorithmException, IOException {
		final byte[] bytes = MD5Utils.md5ToBytes(appKey + topParams + topSession + appSecret);
		final BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(bytes).equals(topSign);
	}

	public static String parametersName(final String top_parameters, final String parameterName) {
		final Map<String, String> map = convertBase64StringtoMap(top_parameters);
		return map.get(parameterName);
	}

	/**
	 * 把经过BASE64编码的字符串转换为Map对象
	 * 
	 * @param str
	 * @return
	 */
	public static Map<String, String> convertBase64StringtoMap(final String str) {
		if (str == null) {
			return null;
		}
		final BASE64Decoder decoder = new BASE64Decoder();
		String keyvalues = null;
		try {
			keyvalues = new String(decoder.decodeBuffer(str), EncodeName.UTF8.getName());
		} catch (final IOException e) {
			log.error(str + "不是一个合法的BASE64编码字符串");
			return null;
		}
		if (keyvalues == null || keyvalues.length() == 0) {
			return null;
		}

		final String[] keyvalueArray = keyvalues.split("&");
		final Map<String, String> map = new HashMap<String, String>();
		for (final String keyvalue : keyvalueArray) {
			final String[] s = keyvalue.split("=");
			if (s == null || s.length != 2) {
				return null;
			}
			map.put(s[0], s[1]);
		}
		return map;
	}

	/**
	 * 登录超时检测
	 * 
	 * @param ts
	 * @param timeOut,超时时间(单位:分钟)
	 * @return true ——> 登录状态正常;false ——> 登录超时
	 * @author gang.zheng
	 */
	public static boolean loginTimeValidate(final String ts, final int timeOut) {
		log.info("开始判断登录时间戳-->ts:" + ts);
		DateTime dateTime;
		try {
			final Long tsDate = Long.parseLong(ts);
			final DateTime nowTime = new DateTime();
			final DateTime beginTime = nowTime.minusMinutes(timeOut);
			final DateTime endTime = nowTime.plusMinutes(timeOut);

			dateTime = new DateTime(tsDate);
			log.info("开始判断登录是否超时");

			if (!(dateTime.isAfter(beginTime) && endTime.isAfter(dateTime))) {
				log.info("登录时间超时");
				return false;
			}
			log.info("开始判断登录时间戳    时间戳成功");
		} catch (final Throwable e) {
			log.debug("判断时间戳出现异常 ts---->" + ts, e);
			return false;
		}
		log.info("**结束登录验证，验证通过**");
		return true;
	}

	/**
	 * 验证从ucerter过来的参数签名是否合法
	 */
	public static boolean validate(final TopParams params) {
		final String top_appkey = params.getTop_appkey();
		final String top_parameters = params.getTop_parameters();
		final String top_session = params.getTop_session();
		final String top_sign = params.getTop_sign();
		TaobaoLoginSource.logger.info("top_appkey:{}", top_appkey);
		TaobaoLoginSource.logger.info("top_parameters:{}", top_parameters);
		TaobaoLoginSource.logger.info("top_session:{}", top_session);
		TaobaoLoginSource.logger.info("top_sign:{}", top_sign);
		boolean isvalid = false;
		final StringBuilder result = new StringBuilder().append(top_appkey).append(top_parameters).append(top_session);
		try {
			isvalid = validateSign(top_sign, result.toString(), TaobaoLoginSource.SECRET);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		TaobaoLoginSource.logger.info("isvalid:{}", isvalid);
		return isvalid;
	}
}
