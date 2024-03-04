package com.example.rezerva.repo;

import com.example.rezerva.domain.Flight;
import com.example.rezerva.domain.Ticket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketRepo implements Repository<Ticket>{
    private String url;
    private String username;
    private String passwd;

    public TicketRepo(String url, String username, String passwd) {
        this.url = url;
        this.username = username;
        this.passwd = passwd;
    }

    @Override
    public List<Ticket> getAll() {
        /**
         * Retrieves a list of all tickets from the database.
         * @return A list of Ticket objects representing all clients in the database.
         * @throws exception If an error occurs while retrieving tickets from the database.
         */
        List<Ticket> all = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"ticket\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idFlight=resultSet.getLong("idflight");
                String usrname = resultSet.getString("username");
                LocalDateTime data=resultSet.getTimestamp("data").toLocalDateTime();

                Ticket tckt=new Ticket(usrname,idFlight,data);
                all.add(tckt);
            }
            return all;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return all;
    }

    public void adauga(Ticket ticket) {
        /**
         * Adaugă un obiect de tip Ticket în baza de date.
         * Metoda inserează un nou rând în tabela "ticket" a bazei de date, folosind informațiile din obiectul Ticket furnizat.
         * Se utilizează conexiunea la baza de date specificată prin URL, numele de utilizator și parola.
         * param ticket Obiectul de tip {@code Ticket} care va fi adăugat în baza de date.
         * throws RuntimeException Dacă apare o eroare la adăugarea unui ticket în baza de date.
         */
        String sql = "insert into \"ticket\" (username,idflight,data) values (?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, passwd);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, ticket.getUsername());
            ps.setLong(2, ticket.getIdFlight());
            ps.setTimestamp(3,Timestamp.valueOf(ticket.getData()));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
