import controller.Controller;
import exceptions.FileException;
import exceptions.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import repository.AppointmentRepository;
import repository.AppointmentRepositoryFile;
import repository.PatientRepository;
import repository.PatientRepositoryFile;
import service.Service;

import java.io.*;
import java.util.Properties;

public class LoginController {

    @FXML
    private TextField unLogin, pLogin;
    @FXML
    private Button logBut;
    public boolean loged = false;

    private boolean checkLog(String user,String pas){
        String filename = "Dentist_Database.txt";
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                String[] elem = line.split(",");
                String un = elem[2];
                String p = elem[3];
                if (user.equals(un) && pas.equals(p)){
                    return true;
                }
            }
        }
        catch(FileNotFoundException except){
            throw new ServiceException("File not found! " + except);
        }
        catch(FileException except){
            throw new ServiceException("File exception! " + except);
        }
        catch(IOException except){
            throw new ServiceException("Other file exception! " + except);
        }
        showErrorMessage("Unknown user");
        return false;
    }

    public void login(){
        loged = true;
    }

    @FXML
    private void onLogin(ActionEvent e){
        try{
            String user = unLogin.getText();
            String pas = pLogin.getText();
            if(checkLog(user,pas)){
                this.login();
                Stage stage;
                Parent root;
                stage = (Stage) logBut.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("dentist.fxml"));
                Controller ctrl = new Controller();
                ctrl.setService(fileService());
                loader.setController(ctrl);
                root = loader.load();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("My Dentist Application");
                stage.show();
            }
        }
        catch(Exception except) {
            showErrorMessage("" + except);
        }
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

    void showErrorMessage(String text){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Big A$$ Error");
        message.setContentText(text);
        message.setHeight(200);
        message.setWidth(200);
        message.showAndWait();
    }

    void showNotification(String text){
        Alert message = new Alert(Alert.AlertType.CONFIRMATION);
        message.setTitle("GJ");
        message.setContentText(text);;
        message.setHeight(400);
        message.setWidth(600);
        message.showAndWait();
    }


}
