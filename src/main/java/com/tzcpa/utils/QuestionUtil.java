package com.tzcpa.utils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * 随机选择答案
 *
 * @author wangbj
 * <p>
 * 2019年5月5日
 */
public class QuestionUtil {

    // 可选字符串
    private static final String CONSTANT_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // 最大可选数量
    private static final Integer ANSWER_COUNT = 26;
    // 最大返回数量
    private static final Integer RETURN_COUNT = 13;
    // 常量值
    private static final Integer NUM = 3;

    /**
     * 从answerCount个字符中选择returnCount个字符
     *
     * @param answerCount
     * @param returnCount
     * @return
     */
    public static String getRandomAnswer(int answerCount, int returnCount) {
        if (answerCount > ANSWER_COUNT) {
            answerCount = ANSWER_COUNT;
        }
        if (returnCount > RETURN_COUNT) {
            returnCount = RETURN_COUNT;
        }
        System.out.println(answerCount + "," + returnCount);
        // 开始获取
        SecureRandom secureRandom = new SecureRandom();
        String result = "";
        int count = 0;
        while (count < returnCount) {
            // 产生随机数
            Integer random = secureRandom.nextInt(answerCount);
            System.out.println("random=" + random);
            // 获取字符
            String resultTemp = CONSTANT_STRING.substring(random, random + 1);
            System.out.println("resultTemp=" + resultTemp);
            // 不重复作为结果
            if (!result.contains(resultTemp)) {
                result = result + resultTemp;
                count++;
            }
        }
        System.out.println("result===" + result);
        // 排序
        result= sortString(result);
        System.out.println("result===" + result);
        // 加逗号
        char[] charArray = result.toCharArray();
        result = "";
        for (int i = 0; i < charArray.length; i++) {
            result = result + charArray[i] + ",";
        }
        result = result.substring(0, result.length() - 1);
        List<String> results = Arrays.asList(result.split(","));
        result = JsonUtil.listToJson(results);
        System.out.println("result===" + result);
        return result;
    }
    /**
     * 从answerCount个字符中选择returnCount个字符
     *
     * @param answerCount
     * @param returnCount
     * @return
     */
    public static String getRandomAnswerT(int answerCount, int returnCount) {
        if (answerCount > ANSWER_COUNT) {
            answerCount = ANSWER_COUNT;
        }
        if (returnCount > RETURN_COUNT) {
            returnCount = RETURN_COUNT;
        }
        System.out.println(answerCount + "," + returnCount);
        // 开始获取
        SecureRandom secureRandom = new SecureRandom();
        String result = "";
        Integer count = 1;
        while (count <= returnCount) {
        	// 获取字符
            String resultTemp = null;
        	 // 不重复作为结果
            if (count.equals(NUM)) {
            	// 产生随机数
                Integer randomT = secureRandom.nextInt(2);
                System.out.println("randomT=" + randomT);
                // 获取字符
                resultTemp = CONSTANT_STRING.substring(randomT, randomT + 1);
                
            } else{
            	// 产生随机数
                Integer random = secureRandom.nextInt(answerCount);
                System.out.println("random=" + random);
                
                resultTemp = CONSTANT_STRING.substring(random, random + 1);
                System.out.println("resultTemp=" + resultTemp);
            }
            
           
            result = result + resultTemp;
            count++;
        }
        System.out.println("result===" + result);
        // 排序
        //result= sortString(result);
        System.out.println("result===" + result);
        // 加逗号
        char[] charArray = result.toCharArray();
        result = "";
        for (int i = 0; i < charArray.length; i++) {
            result = result + charArray[i] + ",";
        }
        result = result.substring(0, result.length() - 1);
        List<String> results = Arrays.asList(result.split(","));
        result = JsonUtil.listToJson(results);
        System.out.println("result===" + result);
        return result;
    }

    // 字符串排序
    private static String sortString(String sortString) {
        char[] charArray = sortString.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            for (int j = 0; j < i; j++) {
                if (charArray[i] < charArray[j]) {
                    char temp = charArray[i];
                    charArray[i] = charArray[j];
                    charArray[j] = temp;
                }
            }
        }
        String result = new String(charArray);
        return result;
    }

    public static void main(String[] args) {
        String randomAnswer = QuestionUtil.getRandomAnswerT(4, 3);
        System.out.println(randomAnswer);
    }
}
