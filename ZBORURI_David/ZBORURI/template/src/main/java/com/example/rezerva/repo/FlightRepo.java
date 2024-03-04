package com.example.rezerva.repo;

import com.example.rezerva.domain.Client;
import com.example.rezerva.domain.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightRepo implements Repository<Flight>{
    private String url;
    private String username;
    private String passwd;

    public FlightRepo(String url, String username, String passwd) {
        this.url = url;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public List<Flight> getAll() {
        /**
         * Retrieves a list of all flights from the database.
         * @return A list of Flight objects representing all clients in the database.
         * @throws exception If an error occurs while retrieving flights from the database.
         */
        List<Flight> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"flight\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id=resultSet.getLong("id");
                String from = resultSet.getString("fromc");
                String to = resultSet.getString("toc");
                LocalDateTime departure=resultSet.getTimestamp("departure").toLocalDateTime();
                LocalDateTime landing=resultSet.getTimestamp("landing").toLocalDateTime();
                Integer seats=resultSet.getInt("seats");

                Flight fl=new Flight(id,from,to,departure,landing,seats);
                all.add(fl);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }
}
