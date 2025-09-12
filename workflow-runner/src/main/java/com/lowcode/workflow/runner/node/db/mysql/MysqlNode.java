package com.lowcode.workflow.runner.node.db.mysql;

import com.lowcode.workflow.runner.node.CallbackFunction;
import com.lowcode.workflow.runner.node.DefaultNode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * mysql节点 v1
 */
@Slf4j
@Getter
public class MysqlNode extends DefaultNode {

    /**
     * 数据库连接url
     */
    private String url;
    /**
     * 数据库连接用户名
     */
    private String username;
    /**
     * 数据库连接密码
     */
    private String password;
    /**
     * sql语句
     */
    private String sql;



    /**
     * 构造当前节点参数
     *
     * @param params 参数
     */
    public MysqlNode(Document params) {
        super(params);
    }


    @Override
    protected void verify(Document params) {

        String url = params.getString("url");

        // jdbc:mysql://localhost:3306/mydatabase
        // ?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&characterEncoding=utf8
        if (!url.contains("useSSL")) {
            if (url.contains("?")) {
                url += "&useSSL=false";
            } else {
                url += "?useSSL=false";
            }
        } else if (!url.contains("serverTimezone")) {
            url += "&serverTimezone=UTC";
        } else if (!url.contains("allowPublicKeyRetrieval")) {
            url += "&allowPublicKeyRetrieval=true";
        } else if (!url.contains("characterEncoding")) {
            url += "&characterEncoding=utf8";
        }
        String username = params.getString("username");
        String password = params.getString("password");
        String sql = params.getString("sql");
        this.url = url;
        this.username = username;
        this.password = password;
        this.sql = sql;
    }

    @Override
    public void run(CallbackFunction callback) {
        log.info("mysql running........... {} {} {} {}", url, username, password, sql);
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            Object results = JdbcOperation.executeQueryOrUpdate(connection, sql);
            log.info("sql: {} mysql connection results : {}", sql, results);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

//        callback.callback(putToNextNodeInput());

    }
}
