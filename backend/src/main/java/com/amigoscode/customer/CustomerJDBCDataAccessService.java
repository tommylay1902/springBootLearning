package com.amigoscode.customer;

import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO{
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomer() {
        var sql = "SELECT id, name, email, age, gender FROM customer;";


        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                       SELECT id, name, email, age, gender
                       FROM customer
                       WHERE id = ?;
                   """;

        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();

    }

    @Override
    public void insertCustomer(Customer customer) {

        var sql = """
                INSERT INTO customer(name, email, age, gender)
                VALUES(?,?,?,?);
                """;
        jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender()
        );
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql =
                    """
                        SELECT count(id)
                        FROM customer
                        WHERE email = ?
                     """;

        Long count = jdbcTemplate.queryForObject(sql, Long.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsPersonWithId(Long id) {

        return selectCustomerById(id).isPresent();

    }

    @Override
    public void deleteCustomerWithId(Long id) {
       var sql =
               """
                    DELETE
                    FROM customer
                    WHERE id = ?;
               """;
       jdbcTemplate.update(sql, id);

    }

    @Override
    public void updateCustomer(Customer customer) {
        var sql =
                """
                    UPDATE customer
                    SET
                        age = ?,
                        email = ?,
                        name = ?,
                        id = ?,
                        gender = ?
                    WHERE id = ?;
                """;

        jdbcTemplate.update(
                sql,
                customer.getAge(),
                customer.getEmail(),
                customer.getName(),
                customer.getId(),
                customer.getGender(),
                customer.getId()
        );
    }
}
