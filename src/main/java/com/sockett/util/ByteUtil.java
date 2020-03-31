package com.sockett.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByteUtil {
    /**
     * @return byte[]
     * @author JJia
     * @date 2018/12/18 10:33
     * @Description 将16进制字符串转换为byte[]
     * @param: str
     */
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    /**
     * 正则验证ip
     *
     * @param ipAddress ip地址
     * @return
     */
    public static boolean isboolIp(String ipAddress) {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static void main(String[] args) {
        System.out.println(hexFloatToBigdecimal("5C8F423F"));
        System.out.println(hexFloatToBigdecimal("3F428F5C"));
    }

    /**
     * @auther: JJia
     * @param: hexStr 16进制的字符串（正序）
     * @return: 计算好的Bigedecimal类型的结果
     * @Descirpt: 将16进制的字符串按照IEEE 754规则转换为bigdecimal类型
     * @date: 2019/11/18 8:34
     */
    public static BigDecimal hexFloatToBigdecimal(String hexStr) {
        Float floatValue = Float.intBitsToFloat(Integer.valueOf(hexStr, 16));
        BigDecimal bigDecimalValue = BigDecimal.valueOf(floatValue);
        return bigDecimalValue;
    }

    /**
     * @auther: JJia
     * @param: hexStr 16进制的字符串（正序）
     * @return: 计算好的String类型的结果
     * @Descirpt: 将16进制的字符串按照IEEE 754规则转换为String类型
     * @date: 2019/11/18 8:34
     */
    public static String hexFloatToString(String hexStr) {
        Float floatValue = Float.intBitsToFloat(Integer.valueOf(hexStr, 16));
        BigDecimal bigDecimalValue = BigDecimal.valueOf(floatValue);
        return bigDecimalValue.toPlainString();
    }

    /**
     * @auther: JJia
     * @param: hexString 16进制字符串
     * @return:
     * @Descirpt: 16进制字符串转字节数组
     * @date: 2019/11/18 8:39
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1])
            );
        }
        return d;
    }

    /**
     * @auther: JJia
     * @Descirpt: 将16进制的字符串转换为经度或者是纬度
     * @date: 2019/11/18 15:25
     */
    public Double getLonOrLatStr(String param) {
        Long value = Long.valueOf(param, 16);
        DecimalFormat df = new DecimalFormat("0.000000");
        int multiple = (int) Math.pow(10, 6);
        String so = df.format((double) value / multiple);
        return new Double(so);
    }

    /**
     * @auther: JJia
     * @param: src 字节数组
     * @return:
     * @Descirpt: 字节数组转16进制字符串
     * @date: 2019/11/18 8:39
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
