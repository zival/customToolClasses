package org.tongwoo.util;

import org.apache.log4j.Logger;

import java.sql.Timestamp;

/**
 *  sqlStringUtil version 1.4 modify content:
 *      1.modify equals list mothod, add param is judge '=' or '!='
 *      2.add mothod onDuplicateUpdate
 *      3.add judge '>' and '>=' and '<' and '<=' mothod list
 *      4.add limit method
 *      5.add multi-table queries
 *      6.match ? placeHolder
 *      7. equals and relation method judge true of false use '?:'
 */
public class SqlStringUtil {
    private static Logger logger = Logger.getLogger(SqlStringUtil.class);
    private String dbType;
    private StringBuffer sql;

    //sql base operation
    public SqlStringUtil query(String tableName, String...colums){
        sql = new StringBuffer("select ");
        for(String col:colums)
            sql.append(col).append(",");
        sql.deleteCharAt(sql.length()-1);
//        if("*".equals(colums)) sql.append("*");
//        sql = new StringBuffer(sql.substring(0, sql.length()-1));
        sql.append(" from ").append(tableName);
        return this;
    }

    //multi-table queries
    public SqlStringUtil multiTableQuery(String[] tableNames, String...colums) {
        sql = new StringBuffer("select ");
        for(String col:colums)
            sql.append(col).append(",");
        sql.deleteCharAt(sql.length()-1);
        sql.append(" from ");
        for(String name:tableNames)
            sql.append(name).append(",");
        sql.deleteCharAt(sql.length()-1);
        return this;
    }

    public SqlStringUtil update(String tableName, String...colums) {
        this.sql = new StringBuffer("update ").append(tableName).append(" set ");
        for(String col:colums)
            this.sql.append(col).append("=?,");
        this.sql = new StringBuffer(this.sql.substring(0, sql.length()-1));
        return this;
    }

    public SqlStringUtil update(String tableName, String[] colums, Object[] values) {
        if( colums!=null && colums.length!=values.length) {
            logger.warn("insert method happen cloums vs values different of size.");
            return this;
        }
        this.sql = new StringBuffer("update ").append(tableName).append(" set ");
        for(int i=0;i<colums.length;i++) {
            this.sql.append(colums[i]).append("=");
            judgeValuesType(values[i]);
        }
        this.sql = new StringBuffer(this.sql.substring(0, sql.length()-1));
        return this;
    }

    /**
     * if oracle of db,please add 'DATE:' at time value before
     * @param tableName
     * @param colums
     * @param values
     * @return
     */
    public SqlStringUtil insert(String tableName, boolean isIgnoreInto, String[] colums, Object...values) {
        if( colums!=null && colums.length!=values.length) {
            logger.warn("insert method happen cloums vs values size different.");
            return this;
        }
        if(isIgnoreInto && "mysql".equals(dbType))
            this.sql = new StringBuffer("insert ignore into ").append(tableName);
        else
            this.sql = new StringBuffer("insert into ").append(tableName);
        //如果colums为空，判断为没有指定插入的列名
        if(colums!=null) {
            sql.append("(");
            for(String col:colums)
                sql.append(col).append(",");
            sql.deleteCharAt(sql.length()-1);
            sql.append(")");
        }
        this.sql.append(" values(");
        for(Object val:values) {
            //判断val类型，插入生成相应的sql
            judgeValuesType(val);
        }
//        System.out.println("nullCount: "+nullCount);
        this.sql.deleteCharAt(this.sql.length()-1);
        this.sql.append(")");
        return this;
    }

    public SqlStringUtil delete(String tableName) {
        sql = new StringBuffer("delete from ").append(tableName);
        return this;
    }

    public SqlStringUtil onDuplicateUpdate(String[] colums, Object[] values){
        if(colums.length != values.length) {
            logger.warn("onDuplicateUpdate method happen cloums vs values size different.");
            return this;
        }
        sql.append(" on duplicate key update ");
        for(int i=0;i<colums.length;i++){
            sql.append(colums[i]).append("=");
            Object val = values[i];
            //判断val类型，插入生成相应的sql
            judgeValuesType(val);
        }
        this.sql.deleteCharAt(this.sql.length()-1);

        return this;
    }

    //additional conditions
    public SqlStringUtil between(String fieldName, String startTime, String endTime){
        addWhere();
        if("oracle".equals(dbType)) {
            if (startTime != null)
                this.sql.append(" and ").append(fieldName).append(">=to_date('").append(startTime).append("', 'yyyy-mm-dd hh24:mi:ss')");
            if (endTime != null)
                this.sql.append(" and ").append(fieldName).append("<to_date('").append(endTime).append("', 'yyyy-mm-dd hh24:mi:ss')");
        }else {
            if (startTime != null)
                this.sql.append(" and ").append(fieldName).append(">='").append(startTime).append("'");
            if (endTime != null)
                this.sql.append(" and ").append(fieldName).append("<'").append(endTime).append("'");
        }
        return this;
    }

    public SqlStringUtil isNull(String columnName){
        addWhere();
        sql.append(" and ").append(columnName).append(" is null");
        return this;
    }

    public SqlStringUtil isNotNull(String columnName){
        addWhere();
        sql.append(" and ").append(columnName).append(" is not null");
        return this;
    }

    public SqlStringUtil relation(String columnName1, String columnName2, boolean isEquals){
        addWhere();
        sql.append(" and ").append(columnName1).append(isEquals ? "=":"!=").append(columnName2);
        return this;
    }

    public SqlStringUtil equals(String key, String value, boolean isEquals){
        addWhere();
        sql.append(" and ").append(key).append(isEquals ? "='":"!='").append(value).append("'");
        return this;
    }

    public SqlStringUtil equalsToDate(String key, String value, boolean isEquals) {
        addWhere();
        sql.append(" and ").append(key).append(isEquals ? "=to_date('":"!=to_date('").append(value).append("','yyyy-mm-dd hh24:mi:ss')");
        return this;
    }

    public SqlStringUtil equalsToChar(String key, String value, boolean isEquals) {
        addWhere();
        sql.append(" and ").append(key).append(isEquals ? "=to_char('":"!=to_char('").append(value).append("','yyyy-mm-dd hh24:mi:ss'");
        return this;
    }

    public SqlStringUtil gtData(String fieldName, String columnValue) {
        if("".equals(this.sql)) return this;
        addWhere();
        sql.append(" and ").append(fieldName).append(">'").append(columnValue).append("'");
        return this;
    }

    public SqlStringUtil ltData(String fieldName, String columnValue) {
        if("".equals(this.sql)) return this;
        addWhere();
        sql.append(" and ").append(fieldName).append("<'").append(columnValue).append("'");
        return this;
    }

    public SqlStringUtil gteData(String fieldName, String columnValue) {
        if("".equals(this.sql)) return this;
        addWhere();
        sql.append(" and ").append(fieldName).append(">='").append(columnValue).append("'");
        return this;
    }

    public SqlStringUtil lteData(String fieldName, String columnValue) {
        if("".equals(this.sql)) return this;
        addWhere();
        sql.append(" and ").append(fieldName).append("<='").append(columnValue).append("'");
        return this;
    }

    public SqlStringUtil like(String[] columns, String[] values) {
        if(columns.length != values.length) {
            logger.warn("like method happen cloums vs values size different");
            return this;
        }
        for(int i=0;i<columns.length;i++) {
            like(columns[i], values[i]);
        }
        return this;
    }

    public SqlStringUtil like(String column, String value) {
        if("".equals(value)) return this;
        addWhere();
        sql.append(" and ").append(column).append(" like '%").append(value).append("%'");
        return this;
    }

    //special operation
    public SqlStringUtil addWhere(){
        if(!"".equals(this.sql) && this.sql.indexOf("where 1=1")==-1) this.sql.append(" where 1=1");
        return this;
    }
    public SqlStringUtil groupBy(String fieldName){
        if(!"".equals(this.sql)) this.sql.append(" group by ").append(fieldName);
        return this;
    }

    public SqlStringUtil orderBy(String sortord, String...fieldNames) {
        if("".equals(sql)) return this;
        sql.append(" group by ");
        for(String fieldName:fieldNames){
             sql.append(fieldName).append(",");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" ").append(sortord);
        return this;
    }

    public SqlStringUtil limit(int page, int pageSize) {
        if("".equals(this.sql)) return this;
        sql.append(" limit ").append(page).append(",").append(pageSize);
        return this;
    }

    public String getSql(){
        //匹配所有？占位符
        String _sql_ = sql.toString().replaceAll("'\\?'", "?");
        logger.info("<<<<<"+_sql_+">>>>>");
        return _sql_;
    }

    private void judgeValuesType(Object val) {
        //首先判断插入值的类型
        if(val instanceof String) {
            //如果db类型是oracle，插入时间时必须在时间值前加上DATE:字符串
            String value = val.toString();
//            if("?".equals(value)){
//                sql.append("?,");
//                return;
//            }
            if (value.indexOf("DATE:") > -1 && "oracle".equals(dbType)) {
                value = value.substring(value.indexOf(":") + 1, value.length());
                sql.append("to_date('").append(value).append("','yyyy-mm-dd hh24:mi:ss'),");
            } else
                this.sql.append("'").append(value).append("',");
        }else if(val instanceof Integer)
            sql.append(Integer.valueOf(val.toString())).append(",");
        else if(val instanceof Long)
            sql.append(Long.valueOf(val.toString())).append(",");
        else if(val instanceof Double)
            sql.append(Double.valueOf(val.toString())).append(",");
        else if(val instanceof Timestamp)
            sql.append("'").append(val).append("',");
        else
            //没有判断成功的类型默认为空字符
            sql.append("'',");
    }

    public SqlStringUtil(String dbType){
        this.dbType = dbType;
    }
}
