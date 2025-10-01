package com.lowcode.workflow.llm.tools.http;


import com.drew.lang.StringUtil;
import lombok.Data;

@Data
public class MySqlConnectionInfo {

    /**
     * 主机名或ip地址
     */
    private String host;
    /**
     * 端口号
     */
    private int port;
    /**
     * 数据库名
     */
    private String database;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 表名
     */
    private String tableName;


    /**
     * 获取url连接字符串
     * @return url连接字符串
     */
    public String getUrl() {
        return String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", host, port, database);
    }
}
