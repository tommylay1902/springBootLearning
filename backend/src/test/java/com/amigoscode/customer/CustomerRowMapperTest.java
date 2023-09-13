package com.amigoscode.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {



    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet= mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("tommy");
        when(resultSet.getString("email")).thenReturn("tommy@gmail.com");
        when(resultSet.getInt("age")).thenReturn(22);
        when(resultSet.getString("gender")).thenReturn("Male");

        //When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        Customer expected = new Customer(1L, "tommy", "tommy@gmail.com", "password", 22, "Male");

        //Then

        assertThat(actual).isEqualTo(expected);


    }
}