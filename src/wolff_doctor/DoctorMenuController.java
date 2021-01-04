/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wolff_doctor;

import POJOS.Com_data_client;
import POJOS.Patient;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author ALVARO
 */
public class DoctorMenuController implements Initializable {

    private Com_data_client com_data_client;

    @FXML
    private Label ecgLabel;

    @FXML
    private TableView<Patient> table;
    
    private ObservableList<Patient> list;
    /*
    @FXML
    private TableColumn<Clinical_record, Integer> idColumn;

    @FXML
    private TableColumn<Clinical_record, String> dateColumn;

    @FXML
    private TableColumn<Clinical_record, Boolean> palpitationsColumn;

    @FXML
    private TableColumn<Clinical_record, Boolean> dizzinessColumn;

    @FXML
    private TableColumn<Clinical_record, Boolean> fatigueColumn;

    @FXML
    private TableColumn<Clinical_record, Boolean> anxietyColumn;

    @FXML
    private TableColumn<Clinical_record, Boolean> chest_painColumn;

    @FXML
    private TableColumn<Clinical_record, Boolean> difficulty_breathingColumn;

    @FXML
    private TableColumn<Clinical_record, Boolean> faintingColumn;

    @FXML
    private TableColumn<Clinical_record, String> ecgColumn;

    @FXML
    private TableColumn<Clinical_record, Button> extra_infoColumn;
*/
    @FXML
    private Label nameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        //addViewButton();
    }
    
    /**
     * This method is used for passing parameters between screens.
     *
     * @param com_data_client
     */
    public void initData(Com_data_client com_data_client) {
        this.com_data_client = com_data_client;
        loadPatients();

    }
    
    public void loadPatients() {
        list = FXCollections.observableArrayList();

        ArrayList<Patient> patients = null;//TODO GETPATIENTS
        list.addAll(patients);
        table.setItems(list);

    }

    private void initTable() {
        //Here we connect the columns with the atributes
/*
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        palpitationsColumn.setCellValueFactory(new PropertyValueFactory<>("palpitations"));
        dizzinessColumn.setCellValueFactory(new PropertyValueFactory<>("dizziness"));
        fatigueColumn.setCellValueFactory(new PropertyValueFactory<>("fatigue"));
        anxietyColumn.setCellValueFactory(new PropertyValueFactory<>("anxiety"));
        chest_painColumn.setCellValueFactory(new PropertyValueFactory<>("chest_pain"));
        difficulty_breathingColumn.setCellValueFactory(new PropertyValueFactory<>("difficulty_breathing"));
        faintingColumn.setCellValueFactory(new PropertyValueFactory<>("fainting"));
        extra_infoColumn.setCellValueFactory(new PropertyValueFactory<>("extra_info"));
        ecgColumn.setCellValueFactory(new PropertyValueFactory<>("RANDOM"));*/

    }

    /**
     * This method adds the View ECG button to a column of the table, with its
     * behaviour.
     *
     * @param patient
     * @param com_data_client
     */
    /*
    private void addViewButton() {
        Callback<TableColumn<Clinical_record, String>, TableCell<Clinical_record, String>> cellFactory
                = new Callback<TableColumn<Clinical_record, String>, TableCell<Clinical_record, String>>() {
            @Override
            public TableCell call(final TableColumn<Clinical_record, String> param) {
                final TableCell<Clinical_record, String> cell = new TableCell<Clinical_record, String>() {

                    final Button btn = new Button("VIEW");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                Clinical_record clinical_record = getTableView().getItems().get(getIndex());
                                buttonAction(clinical_record);
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        ecgColumn.setCellFactory(cellFactory);
    }

    private void buttonAction(Clinical_record clinical_record) {

        Integer[] ecg_data = clinical_record.getECG();
        ECGplot e = new ECGplot(ecg_data);
        if (ecg_data != null) {
            try {
                ecgLabel.setText("");//We clean if previously there was no ecg and msg appeared.
                e.openECGWindow();
            } catch (IOException ex) {
                Logger.getLogger(MedicalHistoryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ecgLabel.setText("No ECG found in this record.");
        }

    }*/



    public ObservableList<Patient> getList() {
        return list;
    }

    /**
     * This method takes the user back to the main menu
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void logOut(ActionEvent event) throws IOException {
        releaseResources(com_data_client);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("DoctorPassView.fxml"));

        Parent doctorPassViewParent = loader.load();
        Scene DoctorPassViewScene = new Scene(doctorPassViewParent);

        //this line gets the Stage information
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(DoctorPassViewScene);
        window.centerOnScreen();

        window.show();
    }

    private static void releaseResources(Com_data_client c) {
        try {
            c.getInputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(DoctorMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.getObjectInputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(DoctorMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.getOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(DoctorMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.getObjectOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(DoctorMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            c.getSocket().close();
        } catch (IOException ex) {
            Logger.getLogger(DoctorMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
