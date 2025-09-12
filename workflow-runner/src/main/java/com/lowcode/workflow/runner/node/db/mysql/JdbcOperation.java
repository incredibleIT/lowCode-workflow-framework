package com.lowcode.workflow.runner.node.db.mysql;

import org.bson.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcOperation {


    public static Object executeQueryOrUpdate(Connection connection, String sql) {
        if (!sql.toLowerCase().startsWith("select")) {
            executeUpdate(connection, sql);
        }

        List<Document> results = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                Document result = new Document();

                int columnCount = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    Object value = resultSet.getObject(columnName);
                    result.append(columnName, value);
                }
                results.add(result);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return results;
    }



    private static void executeUpdate(Connection connection, String sql) {

    }
}
