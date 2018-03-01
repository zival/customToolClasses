package org.tongwoo.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Configuration {
    private static final ConcurrentHashMap<String, String> VALUES = new ConcurrentHashMap();
    private static final Properties properties = System.getProperties();
    private static final String separator = properties.getProperty("file.separator");
    static FileInputStream fileInputStream;

    static {
        try {
            //http配置信息
            Properties dataProperties = new Properties();
            fileInputStream = new FileInputStream(properties.get("user.dir")+separator+"config"+separator+"config.properties");
            dataProperties.load(fileInputStream);
            Enumeration<?> enu_d = dataProperties.keys();
            while(enu_d.hasMoreElements()){
                String key = String.valueOf(enu_d.nextElement());
                setValue(key, (String) dataProperties.get(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public  static void setValue(String key, String value) {
        VALUES.put(key, value);
    }

    public static String getValue(String key) { return VALUES.get(key); }
    public static Boolean getBooleanValue(String key) { return new Boolean(VALUES.get(key)); }
    public static Integer getIntegerValue(String key) { return new Integer(VALUES.get(key)); }
    public static Long getLongValue(String key) { return new Long(VALUES.get(key)); }
}
