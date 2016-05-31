package zwj.test.Utils.EncrpytUtils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class EncryptUtil {
	public static final String KEY_SHA = "SHA";
	public static final String KEY_MD5 = "MD5";
	public static final String KEY_MAC = "HmacMD5";

	// sun不推荐使用它们自己的base64,用apache的挺好
	/**
	 * BASE64加密：Base64编码使用64个明文来编码任意的二进制文件，它里面只使用了A-Z,a-z，0-9，+，/这64个字符。简单有效的加密方法：用途：邮件加密（邮件中的字符有时会被当做命令处理）
	 *
	 * SHA：类似MD5，单向加密算法。通过散列算法可实现数字签名，数字签名的原理是将要传送的明文通过一种函数运算（Hash）转换成报文摘要（不同的明文对应不同的报文摘要，当然要比原来的报文短），报文摘要加密后与明文一起传送给接受方，接受方将接受的明文产生新的报文摘要与发送方的发来报文摘要解密比较，比较结果一致表示明文未被改动，如果不一致表示明文已被篡改。
	 * MAC：在发送数据之前，发送方首先使用通信双方协商好的散列函数计算其摘要值。在双方共享的会话密钥作用下，由摘要值获得消息验证码。之后，它和数据一起被发送。接收方收到报文后，首先利用会话密钥还原摘要值，同时利用散列函数在本地计算所收到数据的摘要值，并将这两个数据进行比对。若两者相等，则报文通过认证。
	 * HMAC，它基于MD5或者SHA-1，在计算散列值时将密钥和数据同时作为输入，并采用了二次散列迭代的方式
	 */
	public static byte[] decryptBASE64(byte[] dest) {
		if (dest == null) {
			return null;
		}
		return Base64.decodeBase64(dest);
	}

	/**
	 * BASE64加密
	 */
	public static byte[] encryptBASE64(byte[] origin) {
		if (origin == null) {
			return null;
		}
		return Base64.encodeBase64(origin);
	}

	/**
	 * MD5加密
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] encryptMD5(byte[] data)
			throws NoSuchAlgorithmException {
		if (data == null) {
			return null;
		}
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(data);
		return md5.digest();
	}

	/**
	 * SHA加密
	 *
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] encryptSHA(byte[] data)
			throws NoSuchAlgorithmException {
		if (data == null) {
			return null;
		}
		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(data);
		return sha.digest();
	}

	/**
	 * 初始化HMAC密钥
	 *
	 * @throws NoSuchAlgorithmException
	 */
	public static String initMacKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
		SecretKey secretKey = keyGenerator.generateKey();
		System.out.println("secretKey="+secretKey.getEncoded());
		return new String(encryptBASE64(secretKey.getEncoded()));
	}

	/**
	 * HMAC加密
	 *
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] encryptHMAC(byte[] data, String key)
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKey secretKey = new SecretKeySpec(decryptBASE64(key.getBytes()),
				KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);

	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String data = "简单加密";
		System.out.println(new Date().getTime());
		System.out.println(new BigInteger(encryptBASE64(data.getBytes()))
				.toString(16));
		System.out.println(new BigInteger(encryptBASE64(data.getBytes()))
				.toString(32));

		System.out.println(new String(decryptBASE64(encryptBASE64(data
				.getBytes()))));
		System.out.println(new Date().getTime());

		System.out.println(new BigInteger(encryptMD5(data.getBytes()))
				.toString());
		System.out.println(new Date().getTime());

		System.out.println(encryptMD5(data.getBytes()));
		System.out.println(new Date().getTime());

		System.out.println(new BigInteger(encryptSHA(data.getBytes()))
				.toString());
		System.out.println(new Date().getTime());

		System.out.println(encryptSHA(data.getBytes()));
		System.out.println(new Date().getTime());
        
		System.out.println(new BigInteger(encryptHMAC(data.getBytes(),
				initMacKey())).toString());
		System.out.println(new Date().getTime());
	}
}
