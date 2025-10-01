package com.lowcode.workflow.llm.tools.mysql;


import com.lowcode.workflow.llm.tools.BaseTool;
import com.lowcode.workflow.llm.tools.http.MySqlConnectionInfo;
import com.lowcode.workflow.llm.tools.http.ToolContext;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * agent的mysql工具，用于执行mysql相关的操作
 */
public class MysqlTool extends BaseTool {



    public MysqlTool(ToolContext toolContext) {
        super(toolContext);
    }

    @Tool("列出当前数据库中的所有表名")
    public String getTables() {
        return executeQuery("SHOW TABLES");
    }

    @Tool("查看指定表的表结构(字段名, 字段类型, 是否主键, 是否允许为空, 字段默认值等). 参数是表名, 例如: user")
    public String describeTable(
            @P("表名") String tableName
    ) {
        return executeQuery(String.format("DESCRIBE %s", tableName));
    }


    @Tool("查询指定表的所有数据. 参数是查询sql语句, 例如: SELECT * FROM user")
    public String queryTable(
            @P("查询sql语句") String querySql
    ) {
        return executeQuery(querySql);
    }





    private String executeQuery(String sql) {
        try (Connection conn = DriverManager.getConnection(
                this.toolContext.getMySqlConnectionInfo().getUrl(),
                this.toolContext.getMySqlConnectionInfo().getUsername(),
                this.toolContext.getMySqlConnectionInfo().getPassword())) {

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (ps.execute()) {
                    return formatResult(ps.getResultSet());
                } else {
                    return "此次查询无返回结果";
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("执行mysql查询失败", e);
        }

    }





    /**
     * 格式化查询结果，将结果转换为字符串
     * @param resultSet 查询结果集
     * @return 格式化后的字符串
     * @throws SQLException 格式化结果时发生的异常
     */
    private String formatResult(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        StringBuilder builder = new StringBuilder();
        int columnCount = metaData.getColumnCount();
        if (columnCount == 0) return "无数据";
        // 第一行是表头
        for (int i = 1;i <= columnCount;i ++) {
            builder.append(metaData.getColumnName(i)).append("\t");
        }
        builder.append("\n");
        // 后续的行是数据行
        while (resultSet.next()) {
            for (int i = 1;i <= columnCount;i ++) {
                builder.append(resultSet.getObject(i)).append("\t");
            }
            builder.append("\n");
        }

        return builder.toString();
    }





}
