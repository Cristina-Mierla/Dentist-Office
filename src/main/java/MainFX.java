import exceptions.ServiceException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import repository.AppointmentRepository;
import repository.AppointmentRepositoryFile;
import repository.PatientRepository;
import repository.PatientRepositoryFile;
import service.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
       try{
            FXMLLoader logLoader = new FXMLLoader(getClass().getResource("logDoc.fxml"));
            LoginController logctrl = new LoginController();
            Parent login = logLoader.load();
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("dentist.fxml"));
            //loader.setController(ctrl);
            //Parent root = loader.load();
            //Controller ctrl = loader.getController();
            //ctrl.setService(fileService());
            //Scene scene = new Scene(root);

           Scene logScene = new Scene(login);
           primaryStage.close();
           primaryStage.setScene(logScene);
           primaryStage.setTitle("Dentist Login");
           primaryStage.show();
           if(logctrl.loged) {
               Scene scene = new Scene(login);
               primaryStage.setScene(scene);
               primaryStage.setTitle("My Dentist Application");
               primaryStage.show();
           }
        }
        catch(Exception except){
            Alert alert =  new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Big A$$ Error");
            alert.setContentText("Error while starting the application " + except);
            alert.setWidth(500);
            alert.setHeight(700);
            alert.showAndWait();
        }
    }
    public static void main(String[] args) {
        launch (args);
    }

    static Service myService(){
        try {
            PatientRepository patientRepo = new PatientRepository();
            AppointmentRepository appointmentRepo = new AppointmentRepository();
            Service serv = new Service(patientRepo, appointmentRepo);
            serv.randomPatient(15);
            serv.randomAppointment(25);

            return serv;
        }
        catch(ServiceException except){
            throw new ServiceException("Error starting the app" + except);
        }
    }

    private Service fileService(){
        try {
        Properties prop = new Properties();
        prop.load(new FileInputStream("Dentist.properties"));
            String patientFile = prop.getProperty("PatientFile");
            if (patientFile == null){
                patientFile = "Patient_File.txt";
                System.err.println("Patient file not found. Using default " + patientFile);
            }
            String appointmentFile = prop.getProperty("AppointmentFile");
            if (appointmentFile == null){
                appointmentFile = "Appointments_File.txt";
                System.err.println("Appointmnets file not found. Using default "+ appointmentFile);
            }
        PatientRepositoryFile patientRepoFile = new PatientRepositoryFile(patientFile);
        AppointmentRepositoryFile appointmentRepoFile = new AppointmentRepositoryFile(appointmentFile,patientRepoFile);
        Service servFile = new Service(patientRepoFile,appointmentRepoFile);

        return servFile;
        }
        catch(ServiceException | IOException except){
            throw new ServiceException("Error starting the app" + except);
        }
    }
}