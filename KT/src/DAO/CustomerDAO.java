package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Customer;

public class CustomerDAO extends BaseDAO<Customer> {

    private static final Logger LOGGER = Logger.getLogger(CustomerDAO.class.getName());

    public CustomerDAO() throws DataAccessException {
        super();
    }

    @Override
    public List<Customer> getAll() throws DataAccessException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT id, customer_name, contact_name, address, phone, email FROM Customers";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("contact_name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all customers: " + e.getMessage(), e);
            throw new DataAccessException("Error getting all customers", e);
        } finally {
            try {
                closeConnection();
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Error closing connection: " + e.getMessage(), e);
                // Xử lý hoặc log lỗi, nhưng không throw nữa để tránh che khuất lỗi gốc
            }
        }
        return customers;
    }

    @Override
    public Customer getById(int id) throws DataAccessException {
        String sql = "SELECT id, customer_name, contact_name, address, phone, email FROM Customers WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("customer_name"),
                            rs.getString("contact_name"),
                            rs.getString("address"),
                            rs.getString("phone"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting customer by id: " + e.getMessage(), e);
            throw new DataAccessException("Error getting customer by id", e);
        } finally {
            try {
                closeConnection();
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Error closing connection: " + e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public void add(Customer customer) throws DataAccessException {
        String sql = "INSERT INTO Customers (customer_name, contact_name, address, phone, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getCustomerName());
            pstmt.setString(2, customer.getContactName());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getEmail());
            pstmt.executeUpdate();
            LOGGER.info("Customer added: " + customer.getCustomerName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding customer: " + e.getMessage(), e);
            throw new DataAccessException("Error adding customer", e);
        } finally {
            try {
                closeConnection();
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Error closing connection: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void update(Customer customer) throws DataAccessException {
        String sql = "UPDATE Customers SET customer_name = ?, contact_name = ?, address = ?, phone = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getCustomerName());
            pstmt.setString(2, customer.getContactName());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getEmail());
            pstmt.setInt(6, customer.getId());
            pstmt.executeUpdate();
            LOGGER.info("Customer updated: " + customer.getCustomerName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating customer: " + e.getMessage(), e);
            throw new DataAccessException("Error updating customer", e);
        } finally {
            try {
                closeConnection();
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Error closing connection: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(int id) throws DataAccessException {
        String sql = "DELETE FROM Customers WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.info("Customer deleted with id: " + id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting customer: " + e.getMessage(), e);
            throw new DataAccessException("Error deleting customer", e);
        } finally {
            try {
                closeConnection();
            } catch (DataAccessException e) {
                LOGGER.log(Level.SEVERE, "Error closing connection: " + e.getMessage(), e);
            }
        }
    }
}