package org.libraryexpress.infrastructure.repository.jdbc;

import org.libraryexpress.domain.customer.entity.Customer;
import org.libraryexpress.domain.customer.repository.CustomerRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CustomerDbRepository implements CustomerRepository {

    private final DataSource dataSource;

    public CustomerDbRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(Customer customer) {
        String query = "INSERT INTO tb_customer (id, name, email) VALUES (?, ?, ?)";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, customer.getId());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getEmail().value());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Fatal database error during customer record insertion: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Customer customer) {
        String query = "UPDATE tb_customer SET name = ?, email = ? WHERE id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getEmail().value());
            statement.setString(3, customer.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Fatal database error during customer record modifications: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Customer> getById(String id) {
        String query = "SELECT * FROM tb_customer WHERE id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToCustomer(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fatal database error executing customer payload query by ID: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Customer> getByEmail(String email) {
        String query = "SELECT * FROM tb_customer WHERE email = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToCustomer(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fatal database error executing customer payload query by E-MAIL: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public Set<Customer> all() {
        String query = "SELECT * FROM tb_customer";

        Set<Customer> customers = new HashSet<>();

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                customers.add(mapRowToCustomer(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fatal database error executing unrestricted customers catalog query: " + e.getMessage(), e);
        }

        return customers;
    }

    /**
     * Map a customer tuple back into its respective clean domain model abstraction.
     */
    private Customer mapRowToCustomer(ResultSet resultSet) throws SQLException {
        return new Customer.Builder()
                .setId(resultSet.getString("id"))
                .setName(resultSet.getString("name"))
                .setEmail(resultSet.getString("email"))
                .build();
    }
}
