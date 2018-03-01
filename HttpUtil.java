package org.loginProj.tools;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class HttpUtil {
    private static Logger logger = Logger.getLogger(HttpUtil.class.getName());

	/**
	* @param requestUrl
	* @param headerMap 请求头{}
	* @param param POST的数据，GET方式请填null
	* @param method POST/GET
	*/
    public static String httpConn(String requestUrl, Map headerMap, String param, String method) throws IOException {
        String resultStr = "no data";
        if(method.equals("GET")) resultStr = doGet(requestUrl, headerMap, param);
        if(method.equals("POST")) resultStr = doPost(requestUrl, headerMap, param);
        return resultStr;
    }

    static String doGet(String requestUrl, Map headerMap, String param) {

        return "";
    }

    static String doPost(String requestUrl, Map headerMap, String param) throws IOException {
        DataOutputStream out = null;
        BufferedReader read = null;
        String result = "";

        try {
            //打开url连接
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //设置http请求头
            if (headerMap != null && headerMap.size() > 0) {
                for (String key : (Set<String>) headerMap.keySet()) {
                    conn.setRequestProperty(key, (String) headerMap.get(key));
                }
            }
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取输出流
            out = new DataOutputStream(conn.getOutputStream());
            out.write(param.getBytes("utf-8"));
            out.flush();

            if(conn.getResponseCode() == 302) {
                logger.warning("httpStats:302");
                return "no data";
            }

            if(conn.getResponseCode() == 200) {
                logger.info("httpStats:200");
            }

            read = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = read.readLine()) != null) {
                result += line;
            }

            return result;
        }finally {
            try {
                if(out!=null) out.close();
                if(read!=null) read.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}