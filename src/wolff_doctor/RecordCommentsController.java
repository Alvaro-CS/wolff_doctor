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
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TextArea;
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
    /**
     * This method gets the comments to show them.
     *
     * @param clinical_record
     * @param com_data_client
     * @param patient
     */
    public void initData(Clinical_record clinical_record, Com_data_client com_data_client, Patient patient) {
        this.clinical_record=clinical_record;
        this.com_data_client = com_data_client;
        this.patientMoved = patient;
        commentsArea.setText(clinical_record.getComments());
    }

    @FXML
    private void saveComments(ActionEvent event) throws IOException {
        int id= clinical_record.getId(); //the id of the record we want to change
        patientMoved.getClinical_record_list().get(id-1).setComments(commentsArea.getText());
        try {
            ObjectOutputStream objectOutputStream = com_data_client.getObjectOutputStream();
            //Sending order
            String order = "UPDATE";
            objectOutputStream.writeObject(order);
            objectOutputStream.reset();

            System.out.println("Order" + order + "sent");

            Patient p= new Patient(patientMoved); //for not getting troubles with streams

            //Sending patient
            objectOutputStream.writeObject(p);
            objectOutputStream.reset();
            System.out.println("Patient data sent to register in server");

        } catch (IOException ex) {
            System.out.println("Unable to write the object on the server.");
            Logger.getLogger(RecordCommentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        backHistory(event);
    }

    private void backHistory(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("MedicalHistoryView.fxml"));

        Parent medicalHistoryViewParent = loader.load();
        Scene MedicalHistoryViewScene = new Scene(medicalHistoryViewParent);

        MedicalHistoryController controller = loader.getController();
        controller.initData(patientMoved, com_data_client);
        //this line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(MedicalHistoryViewScene);
        window.centerOnScreen();

        window.show();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

}
