/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wolff_doctor;

import POJOS.Com_data_client;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 *
 * @author ALVARO
 */
public class DoctorPassController implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private PasswordField passwordField;

    private Com_data_client com_data_client;

    Integer ip_address;
    private final String password = "doctor";

    void initData(String ipaddress) {
        com_data_client.setIp_address(ipaddress);
        System.out.println(ipaddress);
    }

    @FXML
    private void enterButtonOnAction(ActionEvent event) throws IOException {
        if (com_data_client.getIp_address() == null) {
            errorLabel.setText("Please click on the settings button and introduce server's IP");
        } else {
            if (!passwordField.getText().isEmpty() && passwordField.getText().equals(password)) {
                if (!com_data_client.isSocket_created()) {
                    Socket socket = new Socket(com_data_client.getIp_address(), 9000);
                    com_data_client.setSocket(socket);
                    OutputStream outputStream = socket.getOutputStream();
                    com_data_client.setOutputStream(outputStream);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    com_data_client.setObjectOutputStream(objectOutputStream);
                    InputStream inputStream = socket.getInputStream();
                    com_data_client.setInputStream(inputStream);
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    com_data_client.setObjectInputStream(objectInputStream);

                    com_data_client.setSocket_created(true);

                }
                openDoctorMenu(event);

            } else {
                //if Fields are empty
                errorLabel.setText("Please enter the correct password.");
            }
        }
    }

    /**
     * This method opens the main menu for the doctor
     *
     * @param event
     * @throws IOException
     */
    private void openDoctorMenu(ActionEvent event) throws IOException {
        Parent doctorMenuViewParent = FXMLLoader.load(getClass().getResource("DoctorMenuView.fxml"));
        Scene doctorMenuViewScene = new Scene(doctorMenuViewParent);
        //this line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(doctorMenuViewScene);
        window.centerOnScreen();
        window.show();
    }

    @FXML
    private void comDataMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ComDataView.fxml"));
        Parent comDataViewParent = loader.load();

        Scene ComDataViewScene = new Scene(comDataViewParent);

        //this line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(ComDataViewScene);
        window.centerOnScreen();

        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        com_data_client = new Com_data_client();

    }

}
