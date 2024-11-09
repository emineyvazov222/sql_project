package org.spring;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCApp {

    private static final String url = "jdbc:postgresql://localhost:3333/postgres";
    private static final String user = "postgres";
    private static final String password = "emin563";

    public static void main(String[] args) throws SQLException {

        Connection connection = getConnection();
        System.out.println("Connected to database");
        createTable(connection);

        insertUsers(connection);
        insertPosts(connection);
        insertComments(connection);
        insertLikes(connection);

        getUsersById(connection, 1);
        deleteLikesById(connection, 3);


    }

    private static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(url, user, password);
    }

    private static void createTable(Connection con) throws SQLException {

        Statement stmt = con.createStatement();

        stmt.executeUpdate(tableForUsers());
        System.out.println("Users table created");


        stmt.executeUpdate(tableForPosts());
        System.out.println("Posts table created");


        stmt.executeUpdate(tableForComments());
        System.out.println("Comments table created");


        stmt.executeUpdate(tableForLikes());
        System.out.println("Likes table created");


    }

    private static String tableForUsers() {

        return "CREATE TABLE IF NOT EXISTS users ("
                + "id           BIGSERIAL PRIMARY KEY,"
                + "user_name    VARCHAR(255) NOT NULL,"
                + "user_surname VARCHAR (255) NOT NULL,"
                + "age INT CHECK(age > 20),"
                + "pin VARCHAR(7) NOT NULL UNIQUE CHECK (LENGTH(pin) BETWEEN 5 AND 7),"
                + "created_at TIMESTAMP (3) NOT NULL DEFAULT NOW (),"
                + "updated_at TIMESTAMP(3) NOT NULL DEFAULT NOW (),"
                + "created_by BIGINT REFERENCES users(id),"
                + "updated_by BIGINT REFERENCES users(id))";

    }

    private static String tableForPosts() {

        return "CREATE TABLE IF NOT EXISTS posts ("
                + "post_id          BIGSERIAL PRIMARY KEY,"
                + "post_description VARCHAR(255) NOT NULL,"
                + "history_date     DATE,"
                + "created_at       TIMESTAMP(5) NOT NULL DEFAULT NOW(),"
                + "updated_at       TIMESTAMP(5) NOT NULL DEFAULT NOW(),"
                + "created_by   BIGINT REFERENCES users (id),"
                + "updated_by   BIGINT REFERENCES users (id),"
                + "user_id          BIGINT REFERENCES users (id) ON DELETE CASCADE)";
    }

    private static String tableForComments() {

        return "CREATE TABLE IF NOT EXISTS comments ("
                + "comment_id          BIGSERIAL PRIMARY KEY,"
                + "comment_text VARCHAR(255) NOT NULL,"
                + "status VARCHAR(20) DEFAULT 'ACTIVE',"
                + "created_at       TIMESTAMP(5) NOT NULL DEFAULT NOW(),"
                + "updated_at       TIMESTAMP(5) NOT NULL DEFAULT NOW(),"
                + "created_by   BIGINT REFERENCES users (id),"
                + "updated_by   BIGINT REFERENCES users (id),"
                + "post_id          BIGINT REFERENCES posts (post_id) ON DELETE CASCADE,"
                + "user_id          BIGINT REFERENCES users (id) ON DELETE CASCADE )";


    }

    private static String tableForLikes() {
        return "CREATE TABLE IF NOT EXISTS likes ("
                + "like_id    BIGSERIAL PRIMARY KEY,"
                + "user_id    BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,"
                + "post_id    BIGINT REFERENCES posts (post_id) ON DELETE CASCADE,"
                + "comment_id BIGINT REFERENCES comments (comment_id) ON DELETE CASCADE,"
                + "created_at TIMESTAMP(3) NOT NULL DEFAULT NOW(),"
                + "updated_at TIMESTAMP(3) NOT NULL DEFAULT NOW(),"
                + "created_by   BIGINT REFERENCES users (id),"
                + "updated_by   BIGINT REFERENCES users (id))";

    }

    private static void insertUsers(Connection con) throws SQLException {

        PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO users(user_name, user_surname,age,pin) VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, "Emin");
        preparedStatement.setString(2, "Eyvazov");
        preparedStatement.setInt(3, 25);
        preparedStatement.setString(4, "2222232");
        int i = preparedStatement.executeUpdate();
        System.out.println("Inserted " + i + " rows");


    }

    private static void insertPosts(Connection con) throws SQLException {

        PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO posts(post_description, created_by, updated_by, user_id) VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, "This is the first post");
        preparedStatement.setInt(2, 1);
        preparedStatement.setInt(3, 1);
        preparedStatement.setInt(4, 1);
        int i = preparedStatement.executeUpdate();
        System.out.println("Inserted " + i + " rows");

    }

    private static void insertComments(Connection con) throws SQLException {

        PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO comments(comment_text, created_by, updated_by, post_id) VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, "Great post!");
        preparedStatement.setInt(2, 1);
        preparedStatement.setInt(3, 1);
        preparedStatement.setInt(4, 1);
        int i = preparedStatement.executeUpdate();
        System.out.println("Inserted " + i + " rows");


    }

    private static void insertLikes(Connection con) throws SQLException {

        PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO likes(user_id, post_id,created_by,updated_by) VALUES (?, ?, ?, ?)");
        preparedStatement.setInt(1, 1);
        preparedStatement.setInt(2, 1);
        preparedStatement.setInt(3, 1);
        preparedStatement.setInt(4, 1);
        int i = preparedStatement.executeUpdate();
        System.out.println("Inserted " + i + " rows");


    }

    private static void getUsersById(Connection con, int id) throws SQLException {

        PreparedStatement preparedStatement1 = con.prepareStatement("SELECT * FROM users WHERE id = ?");
        preparedStatement1.setInt(1, id);
        ResultSet resultSet = preparedStatement1.executeQuery();
        if (resultSet.next()) {
            int userId = resultSet.getInt("id");
            String userName = resultSet.getString("user_name");
            String userSurname = resultSet.getString("user_surname");
            int age = resultSet.getInt("age");
            String pin = resultSet.getString("pin");
            System.out.println("ID: " + userId + " " + "Name: " + userName + " " + "Surname: " + userSurname + " " + "Age: " + age + " " + "Pin: " + pin);

        } else {
            System.out.println("User not found");
        }


    }

    private static void deleteLikesById(Connection con, int id) throws SQLException {

        PreparedStatement preparedStatement1 = con.prepareStatement("DELETE FROM likes WHERE like_id = ?");
        preparedStatement1.setInt(1, id);
        int i = preparedStatement1.executeUpdate();
        if (i > 0) {
            System.out.println("Deleted " + i + " rows");

        } else {
            System.out.println("Likes_id not found");
        }

    }
}
