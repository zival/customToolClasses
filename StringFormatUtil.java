package org.tongwoo.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.sql.ResultSet;

/**
 * Created by tw on 2017/9/20.
 */
public class StringFormatUtil {


    public Map<String,String> resultSet2JsonMap(ResultSet rs, String key) throws SQLException {
        Map<String,String> map = new HashMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while(rs.next()) {
            String jsonString = "{";
            for(int i=1;i<columnCount;i++){
                jsonString += rsmd.getColumnName(i)+":'"+rs.getString(i)+"',";
            }
            jsonString = jsonString.substring(0, jsonString.length()-1);
            jsonString += "}";
            map.put(rs.getString(key), jsonString);
        }
        return map;
    }

    public List<String> resultSet2JsonList(ResultSet rs) throws SQLException {
        List<String> list = new LinkedList<String>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while(rs.next()) {
            String jsonString = "{";
            for(int i=1;i<columnCount;i++){
                jsonString += rsmd.getColumnName(i)+":'"+rs.getString(i)+"',";
            }
            jsonString = jsonString.substring(0, jsonString.length()-1);
            jsonString += "}";
            list.add(jsonString);
        }
        return list;
    }

}
