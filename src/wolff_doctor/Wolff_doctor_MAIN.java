/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wolff_doctor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author ALVARO
 */
public class Wolff_doctor_MAIN extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DoctorPassView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.centerOnScreen();        
        stage.setTitle("WOLFFGRAM");
        stage.getIcons().add(new Image("/wolff_doctor/images/logo.png"));
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
