package com.example.sem7;

import com.example.sem7.java.domain.Prietenie;
import com.example.sem7.java.domain.Utilizator;
import com.example.sem7.java.domain.Message;
import com.example.sem7.java.service.Service;
import com.example.sem7.java.utils.MessageAlert;

import com.example.sem7.java.utils.Observer.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer {

    public TableView<Utilizator> tableView;
    public TableView<Utilizator> tableViewAll;
    public TableColumn tableColumnUtilizatorIdall;
    public TableColumn tableColumnFirstnameall;
    public TableColumn tableColumnLastNameall;
    public Pane chat;

    public ScrollPane scrollPane1;
    public VBox chatLog1;
    public TextArea messageBox1;
    public Button sendButton1;
    public TableColumn<Utilizator, Long> tableColumnUtilizatorIdEmail;
    public TableColumn<Utilizator, String> tableColumnFirstnameEmail;
    public TableColumn<Utilizator, String> tableColumnLastNameEmail;
    public TableView<Utilizator> tableViewEmail;
    public TextArea messageBoxEmail;
    public Button sendButtonEmail;
    public TableView<Prietenie> tableViewFrienships;
    public TableColumn<Prietenie, String> tableColumnUtilizatorIdFriendships;
    public TableColumn<Prietenie, String> tableColumnFirstnameFriendships;
    public TableColumn<Prietenie, String> tableColumnLastNameFrienships;
    public TableColumn<Prietenie, String> tableColumnStatusFriendships;
    public Button acceptFriendRequestButton;
    public Button rejectFriendRequestButton;
    public Label CurrentPageFriends;
    public TextField NumberOfFriendsShown;
    public Label CurrentPageFriendsEmail;
    public TextField NumberOfFriendsShownEmail;
    public Label CurrentPageUsers;

    public TextField numberOfUsersText;
    public Label CurrentPageFriendships;
    public TextField numberOfFriendshipsText;


    @FXML
    TableColumn<Utilizator, Long> tableColumnUtilizatorId;
    @FXML
    TableColumn<Utilizator, String> tableColumnFirstname;
    @FXML
    TableColumn<Utilizator, String> tableColumnLastName;
    private Utilizator utilizator;
    private Service service;
    ObservableList<Utilizator> modelFriends = FXCollections.observableArrayList();
    ObservableList<Utilizator> modelAll = FXCollections.observableArrayList();
    ObservableList<Prietenie> modelFrenships = FXCollections.observableArrayList();
    private UUID specialUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    private int pageNumberFriends;
    private int pageSizeFriends;
    private int numberOfPagesFriends;
    private int pageNumberUsers;
    private int pageSizeUsers;
    private int numberOfPagesUsers;
    private int pageNumberFriendships;
    private int pageSizeFriendships;
    private int numberOfPagesFriendships;
    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 5;
    private Utilizator userInChat = null;

    public void setService(Service service, Utilizator user) {
        this.utilizator = user;
        this.service = service;
        this.scrollPane1.setVisible(false);


        tableViewEmail.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        pageNumberFriendships = pageNumberFriends = pageNumberUsers = DEFAULT_PAGE_NUMBER;
        pageSizeFriendships = pageSizeFriends = pageSizeUsers = DEFAULT_PAGE_SIZE;

        CurrentPageFriends.setText(String.valueOf(pageNumberFriends));
        numberOfPagesFriends = service.getNumberOfPagesFriends(utilizator.getId(), pageSizeFriends);
        numberOfPagesUsers = service.getNumberOfPagesUsers(pageSizeUsers);
        numberOfPagesFriendships = service.getNumberOfPagesFriendhsips(utilizator.getId(), pageSizeFriendships);

        initModel(modelFriends, service.friendsOfUserPage(user.getId(), pageNumberFriends, pageSizeFriends));
        initModel(modelAll, service.getPageOfUsers(pageNumberUsers, pageSizeUsers));
        initModelFriendships(modelFrenships, service.getPageFriendships(user.getId(), pageNumberFriendships, pageSizeFriendships));

    }

    private void initModelFriendships(ObservableList<Prietenie> model, Iterable<Prietenie> allFriendships) {
        List<Prietenie> usersList = StreamSupport.stream(allFriendships.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(usersList);
    }

    @FXML
    private void initialize() {
        initializeTableColumns(tableColumnUtilizatorId, tableColumnFirstname, tableColumnLastName);
        initializeTableColumns(tableColumnUtilizatorIdall, tableColumnFirstnameall, tableColumnLastNameall);
        initializeTableColumns(tableColumnUtilizatorIdEmail, tableColumnFirstnameEmail, tableColumnLastNameEmail);
        tableView.setItems(modelFriends);
        tableViewAll.setItems(modelAll);
        tableViewEmail.setItems(modelFriends);
        tableColumnUtilizatorIdFriendships.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().prietenComun(utilizator.getId())));
        tableColumnFirstnameFriendships.setCellValueFactory(cellData -> new SimpleStringProperty(getFriendshipPropertyValue(cellData.getValue().prietenComun(utilizator.getId()), Utilizator::getFirstName)));
        tableColumnLastNameFrienships.setCellValueFactory(cellData -> new SimpleStringProperty(getFriendshipPropertyValue(cellData.getValue().prietenComun(utilizator.getId()), Utilizator::getLastName)));
        tableColumnStatusFriendships.setCellValueFactory(new PropertyValueFactory<Prietenie, String>("status"));
        tableViewFrienships.setItems(modelFrenships);
    }

    private void initializeTableColumns(TableColumn<Utilizator, Long> idColumn,
                                        TableColumn<Utilizator, String> firstNameColumn, TableColumn<Utilizator, String> lastNameColumn) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }


    private String getFriendshipPropertyValue(String prietenComunResult, Function<Utilizator, String> propertyExtractor) {
        Utilizator user = service.findUser(prietenComunResult);
        return user != null ? propertyExtractor.apply(user) : "";
    }

    protected void initModel(ObservableList<Utilizator> model, Iterable<Utilizator> users) {

        List<Utilizator> usersList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(usersList);
    }

    public void openChat() {
        getUserInChat();
        showMessages();
    }

    public void showMessages() {
        scrollPane1.setVisible(true);
        messageBox1.clear();
        chatLog1.getChildren().clear();
        List<Message> messages = service.getAllMessBetweenTwoUsers(utilizator.getId(), userInChat.getId());
        messages.forEach(this::addMessageInChat);
    }

    void getUserInChat() {
        userInChat = tableView.getSelectionModel().getSelectedItem();
    }

    private void addMessageInChat(Message message) {

        Label newLabelMessage = new Label(service.getFullMessage(message));
        Button newButton = new Button("Reply");

        if (!Objects.equals(message.getReply(), specialUUID) && message.getReply() != null)
            newLabelMessage.setStyle("-fx-text-fill: blue;");
        setButtonEventHandler(newButton, message.getMessage(), message.getId());
        HBox newHBox = new HBox();
        newHBox.getChildren().addAll(newLabelMessage, newButton);
        chatLog1.getChildren().add(newHBox);
    }

    private void setButtonEventHandler(Button button, String message, UUID idMessage) {

        button.setOnAction(event -> {
            try {
                String replyMessage = messageBox1.getText().replaceAll("\\s+$", " ");

                if (replyMessage.isEmpty()) throw new RuntimeException("empty message");
                messageBox1.clear();
                Utilizator user = tableView.getSelectionModel().getSelectedItem();
                List<String> users = new ArrayList<>();
                users.add(user.getId());
                Message mess = service.addReplyMessage(utilizator.getId(), users, LocalDateTime.now(), replyMessage, idMessage);

                addMessageInChat(mess);
            } catch (RuntimeException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            }
        });

    }


    public void setSendButtonAction() {
        try {

            String message = messageBox1.getText().replaceAll("\\s+$", " ");

            if (message.isEmpty()) throw new RuntimeException("empty message");
            messageBox1.clear();
            Utilizator user = userInChat;
            List<String> users = new ArrayList<>();

            users.add(user.getId());
            Message mes = service.addMessage(utilizator.getId(), users, LocalDateTime.now(), message);

            addMessageInChat(mes);

        } catch (RuntimeException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void sendEmail() {
        try {
            String message = messageBoxEmail.getText();
            messageBoxEmail.clear();
            if (message.isEmpty()) throw new RuntimeException("empty message");
            List<String> users = tableViewEmail.getSelectionModel().getSelectedItems().stream().map(u -> u.getId()).toList();
            service.addMessage(utilizator.getId(), users, LocalDateTime.now(), message);

        } catch (RuntimeException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }


    public void SendFriendRequest() {
        try {
            Utilizator user = tableViewAll.getSelectionModel().getSelectedItem();
            service.addFriendship(utilizator.getId(), user.getId(), LocalDateTime.now());
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());

        } finally {
            initModelFriendships(modelFrenships, service.getAllUserFriendships(utilizator.getId()));
        }
    }

    private void handleFriendRequest(String status) {
        service.updateFriendship(tableViewFrienships.getSelectionModel().getSelectedItem().getId(), status);
        initModelFriendships(modelFrenships, service.getAllUserFriendships(utilizator.getId()));
    }

    public void AcceptFriendRequest() {
        handleFriendRequest("accepted");
        initModel(modelFriends, service.friendsOfUser(utilizator.getId()));
    }

    public void RejectFriendRequest() {
        handleFriendRequest("rejected");
    }

    public void FrienshipSelected() {
        Prietenie friendship = tableViewFrienships.getSelectionModel().getSelectedItem();
        if (Objects.equals(friendship.getStatus(), "pending") && friendship.getId().getRight().equals(utilizator.getId())) {
            acceptFriendRequestButton.setVisible(true);
            rejectFriendRequestButton.setVisible(true);
        } else {
            acceptFriendRequestButton.setVisible(false);
            rejectFriendRequestButton.setVisible(false);
        }
    }

    public void PreviousPage(ActionEvent actionEvent) {
        movePageFriends(-1);
    }

    public void NextPage(ActionEvent actionEvent) {
        movePageFriends(1);
    }

    private void movePageFriends(int deplasament) {
        if (pageNumberFriends + deplasament <= numberOfPagesFriends && pageNumberFriends + deplasament >= 1) {
            pageNumberFriends = pageNumberFriends + deplasament;
            initModel(modelFriends, service.friendsOfUserPage(utilizator.getId(), pageNumberFriends, pageSizeFriends));
            CurrentPageFriends.setText(String.valueOf(pageNumberFriends));
            CurrentPageFriendsEmail.setText(String.valueOf(pageNumberFriends));
        }
    }

    public void SetNumberOfFriends(ActionEvent actionEvent) {
        pageNumberFriends = 1;
        String pageSizeString = NumberOfFriendsShown.getText();
        if (pageSizeString.isEmpty()) pageSizeString = NumberOfFriendsShownEmail.getText();
        pageSizeFriends = Integer.parseInt(pageSizeString);

        NumberOfFriendsShown.clear();
        NumberOfFriendsShownEmail.clear();
        CurrentPageFriends.setText(String.valueOf(pageNumberFriends));
        CurrentPageFriendsEmail.setText(String.valueOf(pageNumberFriends));
        numberOfPagesFriends = service.getNumberOfPagesFriends(utilizator.getId(), pageSizeFriends);

        initModel(modelFriends, service.friendsOfUserPage(utilizator.getId(), pageNumberFriends, pageSizeFriends));
    }

    public void NextPageUsers(ActionEvent actionEvent) {
        movePagesUsers(1);
    }

    public void PreviousPageUsers(ActionEvent actionEvent) {
        movePagesUsers(-1);
    }

    public void movePagesUsers(int deplasament) {
        if (pageNumberUsers + deplasament >= 1 && pageNumberUsers + deplasament <= numberOfPagesUsers) {
            pageNumberUsers = pageNumberUsers + deplasament;
            initModel(modelAll, service.getPageOfUsers(pageNumberUsers, pageSizeUsers));
            CurrentPageUsers.setText(String.valueOf(pageNumberUsers));
        }
    }

    public void SetNumberOfUsersShown(ActionEvent actionEvent) {
        pageNumberUsers = 1;
        pageSizeUsers = Integer.parseInt(numberOfUsersText.getText());
        numberOfUsersText.clear();

        CurrentPageUsers.setText(String.valueOf(pageNumberUsers));
        numberOfPagesUsers = service.getNumberOfPagesUsers(pageSizeUsers);
        initModel(modelAll, service.getPageOfUsers(pageNumberUsers, pageSizeUsers));
    }

    public void movePageFriendships(int deplasament) {
        if (pageNumberFriendships + deplasament <= numberOfPagesFriendships && pageNumberFriendships + deplasament >= 1) {
            pageNumberFriendships = pageNumberFriendships + deplasament;
            initModelFriendships(modelFrenships, service.getPageFriendships(utilizator.getId(), pageNumberFriendships, pageSizeFriendships));
            CurrentPageFriendships.setText(String.valueOf(pageNumberFriendships));
        }
    }

    public void SetNumberOfFriendshipsShown(ActionEvent actionEvent) {
        pageNumberFriendships = 1;
        pageSizeFriendships = Integer.parseInt(numberOfFriendshipsText.getText());
        numberOfFriendshipsText.clear();

        CurrentPageFriendships.setText(String.valueOf(pageNumberFriendships));
        numberOfPagesFriendships = service.getNumberOfPagesFriendhsips(utilizator.getId(), pageSizeFriendships);
        initModelFriendships(modelFrenships, service.getPageFriendships(utilizator.getId(), pageNumberFriendships, pageSizeFriendships));
    }

    public void PreviousPageFriendships(ActionEvent actionEvent) {
        movePageFriendships(-1);
    }

    public void NextPageFriendships(ActionEvent actionEvent) {
        movePageFriendships(1);
    }


    @Override
    public void updateMessage(Message message) {
        message.getTo().forEach(System.out::println);
        if (message.getTo().contains(utilizator.getId()) && Objects.equals(userInChat.getId(), message.getFrom()))
            showMessages();
    }

    @Override
    public void updateFriendship(Prietenie friendship) {

        if (!Objects.equals(friendship.prietenComun(utilizator.getId()), "")) {
            initModelFriendships(modelFrenships, service.getPageFriendships(utilizator.getId(), pageNumberFriendships, pageSizeFriendships));
            initModel(modelFriends, service.friendsOfUserPage(utilizator.getId(), pageNumberFriends, pageSizeFriends));
        }
    }

}
