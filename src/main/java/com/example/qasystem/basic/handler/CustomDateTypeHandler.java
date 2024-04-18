package com.example.qasystem.basic.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Value;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class CustomDateTypeHandler extends BaseTypeHandler<Date> {

    @Value("${custom.datetime-format}")
    private String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date date, JdbcType jdbcType) {
        // Not needed for insert/update operations
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String dateTimeString = resultSet.getString(columnName);
        return convertToDate(dateTimeString);
    }

    @Override
    public Date getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String dateTimeString = resultSet.getString(columnIndex);
        return convertToDate(dateTimeString);
    }

    @Override
    public Date getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        String dateTimeString = callableStatement.getString(columnIndex);
        return convertToDate(dateTimeString);
    }

    private Date convertToDate(String dateTimeString) {
        if (dateTimeString != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(datetimeFormat);
                return dateFormat.parse(dateTimeString);
            } catch (ParseException e) {
                // 处理解析异常
                log.error("Error parsing date string: {}", dateTimeString, e);
            }
        }
        return null;
    }
}
