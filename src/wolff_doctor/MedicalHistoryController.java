package wolff_doctor;

import POJOS.Clinical_record;
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
import javafx.scene.control.TableColumn;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import utilities.ECGplot;

public class MedicalHistoryController implements Initializable {

    private Com_data_client com_data_client;
    private Patient patientMoved;
    
    private boolean connected;
    @FXML
    private Label buttonsLabel;

    @FXML
    private TableView<Clinical_record> table;

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
    private TableColumn<Clinical_record, String> commentsColumn;

    @FXML
    private TableColumn<Clinical_record, String> extra_infoColumn;

    private ObservableList<Clinical_record> list;

    @FXML
    private Label nameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        addViewButton();
        addCommentsButton();

    }

    public void loadClinical_records() {
        list = FXCollections.observableArrayList();

        ArrayList<Clinical_record> recordsP = patientMoved.getClinical_record_list();
        list.addAll(recordsP);
        table.setItems(list);

    }

    private void initTable() {
        //Here we connect the columns with the atributes

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
        ecgColumn.setCellValueFactory(new PropertyValueFactory<>("RANDOM"));
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("RANDOM"));

    }

    /**
     * This method adds the View ECG button to a column of the table, with its
     * behaviour.
     *
     * @param patient
     * @param com_data_client
     */
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
                                buttonECGAction(clinical_record);
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

    private void buttonECGAction(Clinical_record clinical_record) {

        Integer[] ecg_data = clinical_record.getECG();
        ECGplot e = new ECGplot(ecg_data);
        if (ecg_data != null) {
            try {
                buttonsLabel.setText("");//We clean if previously there was no ecg and msg appeared.
                e.openECGWindow();
            } catch (IOException ex) {
                Logger.getLogger(MedicalHistoryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            buttonsLabel.setText("No ECG found in this record.");
        }

    }

    /**
     * This method adds the View comments button to a column of the table, with
     * its behaviour.
     *
     * @param patient
     * @param com_data_client
     */
    private void addCommentsButton() {
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
                            setAlignment(Pos.CENTER);
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                Clinical_record clinical_record = getTableView().getItems().get(getIndex());
                                buttonCommentsAction(clinical_record, event);
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

        commentsColumn.setCellFactory(cellFactory);
    }

    private void buttonCommentsAction(Clinical_record clinical_record, ActionEvent event) {

            try {
                buttonsLabel.setText("");//We clean if previously there was no ecg and msg appeared.
                openCommentsEditor(clinical_record, event);
            } catch (IOException ex) {
                Logger.getLogger(MedicalHistoryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        

    }

    /**
     * This method is used for passing parameters between screens.
     *
     * @param patient
     * @param com_data_client
     * @param connected
     */
    public void initData(Patient patient, Com_data_client com_data_client, Boolean connected) {
        this.com_data_client = com_data_client;
        this.connected=connected;
        this.patientMoved = patient;
        nameLabel.setText("Patient's name:\n " + patientMoved.getName());
        loadClinical_records();

    }

    public ObservableList<Clinical_record> getList() {
        return list;
    }

    /**
     * This method takes the user back to the main menu
     *
     * @param event
     * @throws IOException
     */
    @FXML
    public void backToMenu(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("DoctorMenuView.fxml"));
        Parent doctorMenuViewParent = loader.load();
        Scene DoctorMenuViewScene = new Scene(doctorMenuViewParent);
        DoctorMenuController controller = loader.getController();
        controller.initData(com_data_client,connected);
        //this line gets the Stage information
        Stage window = new Stage();
        window.setScene(DoctorMenuViewScene);
        window.centerOnScreen();
        window.setTitle("WOLFFGRAM");
        window.getIcons().add(new Image("/wolff_doctor/images/logo.png"));
        window.show();
        
        window.setOnCloseRequest(e -> {
            try {
                controller.logOut(event);
            } catch (IOException ex) {
                Logger.getLogger(MedicalHistoryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        Stage myStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        myStage.close();
        
        
    }

    public void openCommentsEditor(Clinical_record clinical_record, ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RecordCommentsView.fxml"));
        Parent recordCommentsViewParent = loader.load();
        Scene RecordCommentsViewScene = new Scene(recordCommentsViewParent);
        RecordCommentsController controller = loader.getController();
        controller.initData(clinical_record,com_data_client,patientMoved,connected);

        Stage window = new Stage();
        window.setScene(RecordCommentsViewScene);
        window.centerOnScreen();
        window.setTitle("WOLFFGRAM");
        window.getIcons().add(new Image("/wolff_doctor/images/logo.png"));
        window.show();

        window.show();
        
        window.setOnCloseRequest(e->{
            try {
                controller.backHistory(event);
            } catch (IOException ex) {
                Logger.getLogger(MedicalHistoryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
                
        Stage myStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        myStage.close();
    }
}
