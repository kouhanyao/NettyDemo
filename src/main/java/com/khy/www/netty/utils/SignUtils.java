package com.khy.www.netty.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class SignUtils {

    public static void main(String[] args) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("status", "rejected");
        data.put("reason", "??????");
        data.put("appy_id", "D201806081409170003");
        data.put("timestamp", "1528712896707");
        System.out.println(getSign(data, "0AN57KdVz652314EKZ6yriAYrlJuDpWz"));

    }

    /**
     * 获取签名
     *
     * @param param  参数
     * @param secret 秘钥
     * @return
     */
    public static String getSign(HashMap<String, Object> param, String secret) {
        String stringBuilder = "";
        List<Map.Entry<String, Object>> maps = mapSort(param);
        for (Map.Entry<String, Object> map : maps) {
            stringBuilder += ((stringBuilder.length() == 0 ? "" : "&") + map.getKey() + "=" + map.getValue().toString());
        }
        stringBuilder += "&secret=" + secret;
        return md5Upper(stringBuilder);
    }

    /**
     * hashmap排序
     *
     * @param sortList
     * @return
     */
    private static List<Map.Entry<String, Object>> mapSort(HashMap<String, Object> sortList) {
        List<Map.Entry<String, Object>> infoIds =
                new ArrayList<>(sortList.entrySet());
        Collections.sort(infoIds, (o1, o2) -> (o1.getKey()).compareTo(o2.getKey()));
        return infoIds;
    }

    /**
     * md5加密
     *
     * @param str
     * @return
     */
    public static String md5(String str) {

        String result = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update((str).getBytes("UTF-8"));
            byte b[] = md5.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            result = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (IOException e1) {
            return "";
        }
        return result;
    }

    public static String md5Upper(String str) {
        return md5(str).toUpperCase();
    }
}
