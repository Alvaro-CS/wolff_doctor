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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 *
 * @author ALVARO
 */
public class DoctorMenuController implements Initializable {

    private ClientThreadsServer clientThreadsServer; //we create a reference for accesing different methods
    private boolean connected;

    private Com_data_client com_data_client;
    @FXML
    private Label recordLabel;
    @FXML
    private TableView<Patient> table;

    private ObservableList<Patient> list;

    @FXML
    private TableColumn<Patient, Integer> idColumn;

    @FXML
    private TableColumn<Patient, String> nameColumn;

    @FXML
    private TableColumn<Patient, String> lastNameColumn;

    @FXML
    private TableColumn<Patient, String> genderColumn;

    @FXML
    private TableColumn<Patient, String> birthdateColumn;
    @FXML
    private TableColumn<Patient, String> addressColumn;
    @FXML
    private TableColumn<Patient, Integer> ssnColumn;
    @FXML
    private TableColumn<Patient, Integer> telfColumn;
    @FXML
    private TableColumn<Patient, String> recordsColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        addViewButton();
    }

    /**
     * This method is used for passing parameters between screens.
     *
     * @param com_data_client
     * @param connected
     */
    public void initData(Com_data_client com_data_client, Boolean connected) {
        this.connected = connected;
        this.com_data_client = com_data_client;
        if (connected) {
            loadPatients();
        } else {
            recordLabel.setTextFill(Color.RED);
            recordLabel.setText("Server connection failed...");
        }

    }

    public void loadPatients() {
        list = FXCollections.observableArrayList();

        ArrayList<Patient> patients = getPatients();
        list.addAll(patients);
        table.setItems(list);

    }

    private ArrayList<Patient> getPatients() {
        try {
            //We send the order to server that we want to search for patients
            String order = "GET_PATIENTS";
            ObjectOutputStream objectOutputStream = com_data_client.getObjectOutputStream();
            objectOutputStream.writeObject(order);
            System.out.println("Order " + order + " sent to server");

            //We here need to receive from the server the patient found.
            clientThreadsServer = new ClientThreadsServer();
            clientThreadsServer.setCom_data_client(com_data_client);
            new Thread(clientThreadsServer).start();

            synchronized (clientThreadsServer) {
                clientThreadsServer.wait();
            }
            return clientThreadsServer.getPatients();
        } catch (IOException ex) {
            Logger.getLogger(DoctorMenuController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(DoctorMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void initTable() {
        //Here we connect the columns with the atributes

        idColumn.setCellValueFactory(new PropertyValueFactory<>("DNI"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        ssnColumn.setCellValueFactory(new PropertyValueFactory<>("SSNumber"));
        telfColumn.setCellValueFactory(new PropertyValueFactory<>("telf"));
        recordsColumn.setCellValueFactory(new PropertyValueFactory<>("RANDOM"));

    }

    /**
     * This method adds the View records button to a column of the table, with
     * its behaviour.
     *
     * @param patient
     * @param com_data_client
     */
    private void addViewButton() {
        Callback<TableColumn<Patient, String>, TableCell<Patient, String>> cellFactory
                = new Callback<TableColumn<Patient, String>, TableCell<Patient, String>>() {
            @Override
            public TableCell call(final TableColumn<Patient, String> param) {
                final TableCell<Patient, String> cell = new TableCell<Patient, String>() {

                    final Button btn = new Button("VIEW");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setAlignment(Pos.CENTER);
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                Patient patient = getTableView().getItems().get(getIndex());
                                buttonAction(patient, event);
                            });
                            setAlignment(Pos.CENTER);
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        recordsColumn.setCellFactory(cellFactory);
    }

    private void buttonAction(Patient p, ActionEvent event) {

        ArrayList<Clinical_record> records = p.getClinical_record_list();
        if (records != null) {
            try {
                recordLabel.setText("");//We clean if previously there was no ecg and msg appeared.
                openMedicalHistory(p, event);
            } catch (IOException ex) {
                Logger.getLogger(DoctorMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            recordLabel.setText("No records found in this patient.");
        }

    }

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
        Stage window = new Stage();
        window.setScene(DoctorPassViewScene);
        window.centerOnScreen();
        window.setTitle("WOLFFGRAM");
        window.getIcons().add(new Image("/wolff_doctor/images/logo.png"));
        window.show();

        window.show();

        Stage myStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        myStage.close();
    }

    /**
     * This method opens the Medical History
     *
     * @param patient
     * @param event
     * @throws IOException
     */
    public void openMedicalHistory(Patient patient, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("MedicalHistoryView.fxml"));

        Parent medicalHistoryViewParent = loader.load();
        Scene MedicalHistoryViewScene = new Scene(medicalHistoryViewParent);

        MedicalHistoryController controller = loader.getController();
        controller.initData(patient, com_data_client, connected);
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
                Logger.getLogger(DoctorMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Stage myStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        myStage.close();

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
