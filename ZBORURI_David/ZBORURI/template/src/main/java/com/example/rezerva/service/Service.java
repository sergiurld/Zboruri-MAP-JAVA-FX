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
        return clientRepo.getAll();
    }
    public List<Flight> getFlights() {
        return flightRepo.getAll();
    }
    public List<Ticket> getTickets() {return ticketRepo.getAll();}

    public Client getClientByUsername(String username){
        for(Client c: clientRepo.getAll())
            if(c.getUsername().equals(username))
                return c;
        return null;
    }

    public void adaugaTicket(String username, Long idFlight){
        Ticket ticket=new Ticket(username,idFlight,LocalDateTime.now());//punem si data curenta a cumparaturii
        ticketRepo.adauga(ticket);//adaugam ticket
    }


}
