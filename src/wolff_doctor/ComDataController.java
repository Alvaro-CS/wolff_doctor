package wolff_doctor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ComDataController implements Initializable {

    @FXML
    private TextField ipAdressField;

    public void saveIP(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("DoctorPassView.fxml"));

        Parent DoctorPassViewParent = loader.load();
        Scene DoctorPassViewScene = new Scene(DoctorPassViewParent);
        DoctorPassController controller = loader.getController();
        System.out.println(ipAdressField.getText());
        controller.initData(ipAdressField.getText());

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
    
    

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
