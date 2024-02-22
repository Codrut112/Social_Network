package com.example.sem7;

import com.example.sem7.java.domain.Utilizator;
import com.example.sem7.java.service.Service;
import com.example.sem7.java.utils.MessageAlert;
import com.example.sem7.java.validators.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUserController {
    public TextField emailText;
    public TextField passwordText;
    public PasswordField passwordField;
    public CheckBox passwordButton;
    @FXML
    private TextField firstNameText;
    @FXML
    private TextField lastNameText;
    @FXML
    private Stage stage;
    private Service service;
    private Utilizator user;


    public void setService(Service service, Stage newStage, Utilizator user) {
        this.service = service;
        this.stage = newStage;
        this.user = user;

        passwordText.setVisible(false);
        if (user != null) {
            firstNameText.setText(user.getFirstName());
            lastNameText.setText(user.getLastName());
            emailText.setText(user.getId());
            emailText.setEditable(false);

        }
    }

    public void handleSave() {
        String firstName = firstNameText.getText();
        String lastName = lastNameText.getText();
        String password =passwordField.getText();
        String email=emailText.getText();
        try {
            if (user == null) {
                addUser(firstName, lastName,email,password);
                stage.close();
            } else {
                updateUser(firstName, lastName);
            }
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }

    private void updateUser(String firstName, String lastName) {
        service.updateUtiliator(user.getId(),firstName,lastName);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Succes", "User Modified");
    }


    private void addUser(String firstName, String lastName,String email,String password) {
        service.addUtilizator(firstName, lastName, email,password);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Succes", "User Saved");
    }

    public void handleCancel() {
        stage.close();
    }

    public void managePassword(ActionEvent mouse){
        if(passwordButton.isSelected()) {
            passwordField.setVisible(false);
            passwordText.setText(passwordField.getText());
            passwordText.setVisible(true);
        }
        else {
            passwordField.setVisible(true);
            passwordText.setVisible(false);
            passwordField.setText(passwordText.getText());

        }
    }


}
