package com.example.rezerva.controller;

import com.example.rezerva.domain.Client;
import com.example.rezerva.domain.Flight;
import com.example.rezerva.domain.Ticket;
import com.example.rezerva.service.Service;
import com.example.rezerva.utils.events.ChangeEventType;
import com.example.rezerva.utils.events.EntityChangeEvent;
import com.example.rezerva.utils.observer.Observer;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientController implements Observer {
    private Service service;
    private Client client;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableColumn<Flight, LocalDateTime> departureColoana;

    @FXML
    private ComboBox<String> fromCombo;

    @FXML
    private TableColumn<Flight, LocalDateTime> landingColoana;

    @FXML
    private TableColumn<Flight, Integer> seatsColoana;

    @FXML
    private TableView<Flight> tableView;

    @FXML
    private ComboBox<String> toCombo;

    @FXML
    private Button cautaButton;
    @FXML
    private Button buyButton;
    @FXML
    private TableColumn<Flight,Integer> availableColoana;

    @FXML
    private TableView<Ticket> comenziTableView;
    @FXML
    private TableColumn<Ticket,Long>idcomenziTableView;
    @FXML
    private TableColumn<Ticket,LocalDateTime>datacomenziTableView;
    @FXML
    private TableView<Ticket> soldTableView;
    @FXML
    private TableColumn<Ticket,Long>idsoldTableView;
    @FXML
    private TableColumn<Ticket,LocalDateTime>datasoldTableView;
    @FXML


    ObservableList<Flight> model= FXCollections.observableArrayList();
    ObservableList<Ticket> modelticket = FXCollections.observableArrayList();
    ObservableList<Ticket> modelsold = FXCollections.observableArrayList();

    public void setService(Service service,Client client) {
        this.service = service;
        this.client=client;
        initModel();
        service.addObserver(this);
    }

    @FXML
    public void initialize() {

        tableView.setItems(model);
        //tabel zboruri
        landingColoana.setCellValueFactory(new PropertyValueFactory<Flight, LocalDateTime>("landing"));
        departureColoana.setCellValueFactory(new PropertyValueFactory<Flight, LocalDateTime>("departure"));
        seatsColoana.setCellValueFactory(new PropertyValueFactory<Flight, Integer>("seats"));

        //tabel comenzi din istoricul lui
        idcomenziTableView.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIdFlight()));
        datacomenziTableView.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getData()));
        //tabel comenzi din 24 ianuarie 2024
        idsoldTableView.setCellValueFactory(new PropertyValueFactory<>("username"));
        datasoldTableView.setCellValueFactory(new PropertyValueFactory<>("data"));
        //calculam locurile disponibile
        availableColoana.setCellValueFactory(c -> {
            Flight fl=c.getValue();
            Integer cnt=0;
            for(Ticket t: service.getTickets())
            {
                if(t.getIdFlight().equals(fl.getId()))
                    cnt++; //numaram cate tickete sunt deja cumparate pentru acest zbor
            }
            return new ReadOnlyObjectWrapper<Integer>(fl.getSeats()-cnt); // numarul total de locuri - tichetele cumparate pentru zbor
        });

        tableView.setItems(model);
        comenziTableView.setItems(modelticket);
        soldTableView.setItems(modelsold);
    }

    private void initModel() {
        setCombo();
        handleSearch();
        loadClientTickets();
        loadSoldTickets();
    }

    public void setCombo(){
        Set<String> from = new HashSet<>();
        Set<String> to = new HashSet<>();
        for(Flight flight : service.getFlights()) {
            from.add(flight.getFrom()); //adaug orasele de plecare
            to.add(flight.getTo());//adaug orasele de destinatie
        }
        fromCombo.getItems().addAll(from); //setez combobox pentru plecare
        toCombo.getItems().addAll(to); //setez combobox pentru destinatie
    }
    private void loadClientTickets() {
       //incarc toate tichetele pentru clientul curent
        if (client != null) {
            //iau ticehtele clientului curent
            List<Ticket> clientTickets = service.getTicketsForClient(client);
            modelticket.setAll(clientTickets);
            // setez in tableview

        }
    }
    private void loadSoldTickets(){
        List<Ticket> soldTickets = service.getTicketsFromDate();
        modelsold.setAll(soldTickets);

    }
    public void handleSearch() {
        //cautam un zbor pentru datele selectate
        LocalDate start = datePicker.getValue(); // data zborului
        String from = fromCombo.getValue(); // locatia de plecare
        String to=toCombo.getValue(); // destinatia
        if(start!=null && from!=null && to!=null) { //daca sunt selectate valorile necesare
            model.clear();//data from the table
            for (Flight fl : service.getFlights()) { //luam zborurile cu datele selectate de noi
                if (fl.getDeparture().toLocalDate().compareTo(start) == 0 && fl.getFrom().equals(from) && fl.getTo().equals(to)) {
                        model.add(fl);//daca imi convine il adaug in model, care e primul tabel
                }
            }
        }
    }

    public void handleBuy(ActionEvent event) {
        Flight flight= tableView.getSelectionModel().getSelectedItem();//luam zborul selectat
        service.adaugaTicket(client.getUsername(),flight.getId()); // adaugam un ticket(username  si id)

        service.notifyObservers();//modificam si la restul
    }

    @Override
    public void update() {
        initModel();
    }


}
