package cn.ehome.blockchain.util;

import java.security.MessageDigest;

/**
 * @author Guoxiujun
 * @date 2019/1/28
 */
public class  CryptoUtil {
    private CryptoUtil() {
    }

	public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            System.out.println("getSHA256 is error" + e.getMessage());
        }
        return encodeStr;
    }

    private static String byte2Hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        String temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                builder.append("0");
            }
            builder.append(temp);
        }
        return builder.toString();
    }
//    public static void main(String[] args) {
//		String str = "abcd";
//		String stred = CryptoUtil.getSHA256(str);
//		System.err.println(stred);
//	}
}
