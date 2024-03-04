package com.example.rezerva.service;

import com.example.rezerva.domain.Client;
import com.example.rezerva.domain.Flight;
import com.example.rezerva.domain.Ticket;
import com.example.rezerva.repo.ClientRepo;
import com.example.rezerva.repo.FlightRepo;
import com.example.rezerva.repo.TicketRepo;
import com.example.rezerva.utils.events.EntityChangeEvent;
import com.example.rezerva.utils.observer.Observable;
import com.example.rezerva.utils.observer.ObservableImplementat;
import com.example.rezerva.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Service extends ObservableImplementat {
    private ClientRepo clientRepo;
    private FlightRepo flightRepo;
    private TicketRepo ticketRepo;
    //private List<Observer<EntityChangeEvent>> observers= new ArrayList<>();

    public Service(ClientRepo clientRepo,FlightRepo flightRepo,TicketRepo ticketRepo) {
        this.clientRepo = clientRepo;
        this.flightRepo=flightRepo;
        this.ticketRepo=ticketRepo;
    }

    public  List<Ticket> getTicketsForClient(Client client) {
        /**
         * Retrieve a list of tickets associated with a specific client from the database.
         * This method filters all tickets in the database based on the provided  Client object.
         * It retrieves all tickets for the client with a matching username.
         * The result is a list containing all tickets associated with the specified client.
         * param client: Client object for which to retrieve tickets.
         * return A list of Ticket objects representing tickets associated with the specified client.
         * An empty list is returned if no tickets are found for the client.
         */
        List<Ticket> clientTickets = new ArrayList<>();
        System.out.println(client.getUsername());
        List<Ticket> allTickets = ticketRepo.getAll(); // Replace with your actual method

        for (Ticket ticket : allTickets) {
            if (ticket.getUsername().equals(client.getUsername())) {
                clientTickets.add(ticket);
            }
        }

        return clientTickets;
    }
    public List<Ticket> getTicketsFromDate() {
        /**
         * Retrieve a list of tickets for a specific date from the database.
         * This method filters all tickets in the database based on a target date.
         * It retrieves all tickets with a date equal to the specified target date.
         * return A list of Ticket objects representing tickets for the specified date.
         *         An empty list is returned if no tickets are found for the date.
         */
        List<Ticket> dateTickets = new ArrayList<>();

        List<Ticket> allTickets = ticketRepo.getAll(); // Replace with your actual method

        // Specify the target date (24.01.2024) as a LocalDateTime object
        LocalDateTime targetDate = LocalDateTime.of(2024, 1, 24, 6, 0);

        for (Ticket ticket : allTickets) {
            // Assuming getData() returns a LocalDateTime object
            System.out.println(ticket.getData().toLocalDate());
            System.out.println(targetDate.toLocalDate());
            if (ticket.getData().toLocalDate().isEqual(targetDate.toLocalDate())) {
                dateTickets.add(ticket);
            }
        }

        return dateTickets;
    }


    public List<Client> getLocations() {
        /**
         * Retrieve a list of all clients' locations from the database.
         * return A list of Client objects representing the locations of all clients in the database.
         *          An empty list is returned if no clients are found.
         */
        return clientRepo.getAll();
    }


    public List<Flight> getFlights() {
        /**
         * Retrieve a list of all flights from the database.
         * return A list of Flight objects representing all flights in the database.
         *         An empty list is returned if no flights are found.
         */
        return flightRepo.getAll();
    }


    public List<Ticket> getTickets() {
        /**
         * Retrieve a list of all tickets from the database.
         * return A list of Ticket objects representing all tickets in the database.
         *         An empty list is returned if no tickets are found.
         */
        return ticketRepo.getAll();
    }


    public Client getClientByUsername(String username) {
        /**
         * Retrieve a client by their username from the database.
         * param username The username of the client to retrieve.
         * return The Client object representing the client with the specified username.
         *         Returns  null if no client is found with the given username.
         */
        for (Client c : clientRepo.getAll()) {
            if (c.getUsername().equals(username)) {
                return c;
            }
        }
        return null;
    }


    public void adaugaTicket(String username, Long idFlight){
        /**
         * Adds a new ticket to the database for a specified client and flight.
         * This method creates a new Ticket object with the provided username, flight ID, and the current purchase date.
         * The new ticket is then added to the database using the ticketRepo.adauga method.
         * param username The username of the client purchasing the ticket.
         * param idFlight The ID of the flight for which the ticket is being purchased.
         * throws RuntimeException If an error occurs while adding the ticket to the database.
         */
        Ticket ticket=new Ticket(username,idFlight,LocalDateTime.now());
        ticketRepo.adauga(ticket);
    }


}
