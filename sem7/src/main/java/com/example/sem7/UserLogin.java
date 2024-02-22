package com.example.sem7;


import com.example.sem7.java.repository.FriendshipDBPagingRepository;
import com.example.sem7.java.repository.UserDBPagingRepository;
import com.example.sem7.java.domain.Utilizator;
import com.example.sem7.java.paging.FriendshipsPagingRepository;
import com.example.sem7.java.paging.PagingRepository;
import com.example.sem7.java.repository.*;
import com.example.sem7.java.service.Service;
import com.example.sem7.java.utils.MessageAlert;
import com.example.sem7.java.validators.MessageValidator;
import com.example.sem7.java.validators.UtilizatorValidator;
import com.example.sem7.java.validators.ValidarePrietenie;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserLogin {
    public TextField textUserName;
    public PasswordField textPassword;
    public TextField passwordViewText;
    public CheckBox passwordButtonLogin;
    private Service service = getService();
    public void loginUser() {
        String username = textUserName.getText();
        String password = textPassword.getText();
        passwordViewText.clear();
        textPassword.clear();

        try {
            Utilizator user = service.verifyUser(username, password);

            if (user != null) {

                Stage mainStage = new Stage();
                mainStage.setTitle("Main");

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("main.fxml"));
                AnchorPane newRoot = (AnchorPane) loader.load();
                Scene scene = new Scene(newRoot);

                mainStage.setScene(scene);

                // Pass the service and user to the controller
                UserController userController = loader.getController();
                userController.setService(service, user);
                service.addObserve(userController);
                System.out.println("gata sa afiseze");
                mainStage.show();
                System.out.println("dada");
                // Close the current login window
                Stage currentStage = (Stage) textUserName.getScene().getWindow();

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            MessageAlert.showErrorMessage(null, "login fail");
        }
    }


    private Service getService() {
        String url = "jdbc:postgresql://localhost:5432/Social_Network";
        String username = "postgres";
        String password = "coco";

        PagingRepository userDBRepository = new UserDBPagingRepository(url, username, password, new UtilizatorValidator());
        FriendshipsPagingRepository friendshipDBRepository = new FriendshipDBPagingRepository(url, username, password, new ValidarePrietenie());
        DbMessageRepository messageDbRepository = new DbMessageRepository(url, username, password, new MessageValidator());
        return new Service(userDBRepository, friendshipDBRepository, messageDbRepository);
    }

    public void addUser(ActionEvent actionEvent) throws IOException {

        Stage newStage = new Stage();
        newStage.setTitle("Register");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("EditUser.fxml"));
        AnchorPane newRoot = (AnchorPane) loader.load();
        Scene newScene = new Scene(newRoot);
        newStage.setScene(newScene);
        EditUserController editUserController = loader.getController();
        editUserController.setService(getService(), newStage, null);
        newStage.show();
    }

    public void managePassword(ActionEvent actionEvent) {
        if (passwordButtonLogin.isSelected()) {
            passwordViewText.setVisible(true);
            textPassword.setVisible(false);
            passwordViewText.setText(textPassword.getText());
        } else {
            passwordViewText.setVisible(false);
            textPassword.setVisible(true);
            textPassword.setText(passwordViewText.getText());

        }

    }
}
