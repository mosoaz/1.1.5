package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnectionDB;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        Statement statement;
        try (Connection connection = getConnectionDB()) {
            statement = connection.createStatement();
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users " +
                    "(id INT PRIMARY KEY AUTO_INCREMENT NOT NULL, " +
                    "name VARCHAR(30) NOT NULL, " +
                    "lastName VARCHAR(30) NOT NULL, " +
                    "age TINYINT(3) NOT NULL)";
            statement.executeUpdate(createUsersTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        Statement statement;
        try (Connection connection = getConnectionDB()) {
            statement = connection.createStatement();
            String dropUsersTable = "DROP TABLE IF EXISTS users";
            statement.executeUpdate(dropUsersTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String str = "insert into users(name, lastName, age) values(?,?,?)";
        Statement statement = null;

        try (Connection connect = getConnectionDB()) {
            statement = connect.createStatement();
            statement.executeUpdate("start transaction");
            PreparedStatement st = connect.prepareStatement(str);
            st.setString(1, name);
            st.setString(2, lastName);
            st.setByte(3, age);
            st.executeUpdate();
            statement.executeUpdate("commit");
            System.out.printf("User с именем – %s добавлен в базу данных \n", name);
        } catch (SQLException e) {
            try {
                if (statement != null) {
                    statement.executeUpdate("rollback");
                }
                throw new RuntimeException(e);
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }


    public void removeUserById(long id) {
        String remove = "DELETE FROM users WHERE id = ?;";
        Statement statement = null;

        try (Connection connect = getConnectionDB()) {
            statement = connect.createStatement();
            statement.executeUpdate("start transaction");
            PreparedStatement st = connect.prepareStatement(remove);
            st.setLong(1, id);
            st.executeUpdate();
            statement.executeUpdate("commit");
        } catch (SQLException e) {
            try {
                if (statement != null) {
                    statement.executeUpdate("rollback");
                }
                throw new RuntimeException(e);
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }

    public List<User> getAllUsers() {

        List<User> list = new ArrayList<>();

        try (Statement statement = getConnectionDB().createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while ((resultSet.next())) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void cleanUsersTable() {
        String clean = "DELETE FROM users";
        Statement statement = null;

        try (Connection connection = getConnectionDB()) {
            statement = connection.createStatement();
            statement.executeUpdate("start transaction");
            statement.executeUpdate(clean);
            statement.executeUpdate("commit");
        } catch (SQLException e) {
            try {
                if (statement != null) {
                    statement.executeUpdate("rollback");
                }
                throw new RuntimeException(e);
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
        }
    }
}