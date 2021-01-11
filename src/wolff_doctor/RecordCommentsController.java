/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wolff_doctor;

import POJOS.Clinical_record;
import POJOS.Com_data_client;
import POJOS.Patient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author susan
 */
public class RecordCommentsController implements Initializable {

    @FXML
    private TextArea commentsArea;
    private Com_data_client com_data_client;
    private Patient patientMoved;
    private Clinical_record clinical_record;
    private boolean connected;
    
    @FXML
    private Label errorLabel;

    /**
     * This method gets the comments to show them.
     *
     * @param clinical_record
     * @param com_data_client
     * @param patient
     * @param connected
     */
    public void initData(Clinical_record clinical_record, Com_data_client com_data_client, Patient patient,Boolean connected) {
        this.connected=connected;
        this.clinical_record = clinical_record;
        this.com_data_client = com_data_client;
        this.patientMoved = patient;
        commentsArea.setText(clinical_record.getComments());
    }

    @FXML
    private void saveComments(ActionEvent event) throws IOException, InterruptedException {

        try {
            ObjectOutputStream objectOutputStream = com_data_client.getObjectOutputStream();
            ObjectInputStream objectInputStream = com_data_client.getObjectInputStream();

            //Sending order
            String order = "UPDATE";
            objectOutputStream.writeObject(order);
            objectOutputStream.reset();

            System.out.println("Order" + order + "sent");
            Thread.sleep(100); //Time for receiving the signal that checks server is active.
            int signal = objectInputStream.available();
            System.out.println("Signal: " + signal);
            if (signal == 0) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Connection to the server lost.\nPlease log out and try again.");
                connected=false;
            } else {
                System.out.println(objectInputStream.readByte());

                int id = clinical_record.getId(); //the id of the record we want to change
                patientMoved.getClinical_record_list().get(id - 1).setComments(commentsArea.getText());

                Patient p = new Patient(patientMoved); //for not getting troubles with streams

                //Sending patient
                objectOutputStream.writeObject(p);
                objectOutputStream.reset();
                System.out.println("Patient data sent to register in server");
                backHistory(event);
            }
        } catch (IOException ex) {
            System.out.println("Unable to write the object on the server.");
            Logger.getLogger(RecordCommentsController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void backHistory(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("MedicalHistoryView.fxml"));

        Parent medicalHistoryViewParent = loader.load();
        Scene MedicalHistoryViewScene = new Scene(medicalHistoryViewParent);

        MedicalHistoryController controller = loader.getController();
        controller.initData(patientMoved, com_data_client,connected);
        //this line gets the Stage information
        Stage window = new Stage();
        window.setScene(MedicalHistoryViewScene);
        window.centerOnScreen();
        window.setTitle("WOLFFGRAM");
        window.getIcons().add(new Image("/wolff_doctor/images/logo.png"));
        window.show();

        window.show();

        window.setOnCloseRequest(e -> {
            try {
                controller.backToMenu(event);
            } catch (IOException ex) {
                Logger.getLogger(RecordCommentsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Stage myStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        myStage.close();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
