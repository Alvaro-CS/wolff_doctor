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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
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
                    try {
                        Socket socket = new Socket(com_data_client.getIp_address(), 9000);
                        if (socket == null) {
                            errorLabel.setText("Connection could not be established");
                        } else if (socket.isConnected()) {
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

                            openDoctorMenu(event);

                        }else{
                         errorLabel.setText("Connection could not be established");
  
                        }
                            
                    } catch (Exception e) {
                        System.err.println("No se pudo establecer conexión con: " + com_data_client.getIp_address() + " a travez del puerto: " + 9000);
                        errorLabel.setText("Connection could not be established");
                    }

                } else if (com_data_client.getSocket() == null) {
                    errorLabel.setText("Connection could not be established");
                } else if (com_data_client.isSocket_created()) {
                    openDoctorMenu(event);
                }

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
    @FXML
    private void openDoctorMenu(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("DoctorMenuView.fxml"));
        Parent doctorMenuViewParent = loader.load();
        Scene DoctorMenuViewScene = new Scene(doctorMenuViewParent);
        DoctorMenuController controller = loader.getController();
        controller.initData(com_data_client);
        //this line gets the Stage information
        Stage window = new Stage();
        window.setScene(DoctorMenuViewScene);
        window.centerOnScreen();
        window.setTitle("WOLFFGRAM");
        window.getIcons().add(new Image("/wolff_doctor/images/logo.png"));
        window.show();

        window.show();
        
        window.setOnCloseRequest(e->{
            try {
                controller.logOut(event);
            } catch (IOException ex) {
                Logger.getLogger(DoctorPassController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Stage myStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        myStage.close();
    }

    @FXML
    private void comDataMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ComDataView.fxml"));
        Parent comDataViewParent = loader.load();
        
        //Load the controller
        ComDataController controller = loader.getController();

        Scene ComDataViewScene = new Scene(comDataViewParent);

        //this line gets the Stage information
        Stage window = new Stage();
        window.setScene(ComDataViewScene);
        window.centerOnScreen();
        window.setTitle("WOLFFGRAM");
        window.getIcons().add(new Image("/wolff_doctor/images/logo.png"));
        window.show();

        window.show();
        
        window.setOnCloseRequest(e->{
            try {
                controller.saveIP(event);
            } catch (IOException ex) {
                Logger.getLogger(DoctorPassController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
           
        
        Stage myStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        myStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        com_data_client = new Com_data_client();

    }

}
