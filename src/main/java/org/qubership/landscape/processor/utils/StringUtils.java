package org.qubership.landscape.processor.utils;

public class StringUtils {
    public static String getFirstNonEmpty(String str1, String str2) {
        if (str1 != null && !str1.isEmpty()) return str1;
        return str2;
    }

    public static boolean isEmpty(String testString) {
        return  (testString == null || testString.isBlank());
    }
}
