package com.lowcode.workflow.web.handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.bson.Document;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用来处理数据库中字符串类型的json数据转换为Document对象的处理器
 */
@MappedTypes(value = {Document.class})
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class JsonToDocumentHandler extends BaseTypeHandler<Object> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(
            PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        try {
            String s = null;
            if (parameter != null) {
                s = objectMapper.writeValueAsString(parameter);
            }
            ps.setString(i, s);
        } catch (JsonProcessingException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.getResult(rs.getString(columnName));
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.getResult(rs.getString(columnIndex));
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.getResult(cs.getString(columnIndex));
    }

    private Object getResult(String s) {
        if (isJson(s)) {
            try {
                return objectMapper.readValue(s, Document.class);
            } catch (JsonProcessingException e) {
                throw new InternalException(e.getMessage());
            }
        }
        return null;
    }

    private boolean isJson(String s) {
        return s != null && s.startsWith("{") && s.endsWith("}");
    }
}
