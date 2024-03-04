package com.example.rezerva.repo;

import com.example.rezerva.domain.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepo implements Repository<Client>{
    private String url;
    private String usernamee;
    private String passwd;

    public ClientRepo(String url, String usernamee, String passwd) {
        this.url = url;
        this.usernamee = usernamee;
        this.passwd = passwd;
    }

    @Override
    public List<Client> getAll() {
        /**
         * Retrieves a list of all clients from the database.
         * @return A list of Client objects representing all clients in the database.
         * @throws exception If an error occurs while retrieving clients from the database.
         */
        List<Client> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, usernamee, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"client\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String name = resultSet.getString("name");

                Client client=new Client(username,name);
                all.add(client);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }

}
