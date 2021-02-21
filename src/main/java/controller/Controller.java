package controller;

import comparator.AppointmentIdComparator;
import comparator.PatientIdComparator;
import domain.Appointment;
import domain.Patient;
import exceptions.ServiceException;
import exceptions.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import service.Service;
import undo.Element;
import utilitary.Convert;
import validator.AppointmentValidator;
import validator.PatientValidator;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Controller {
    
    @FXML
    private TableView<Appointment> aptable;
    @FXML
    private TableColumn<Appointment,Integer> idAppCol, priceAppCol;
    @FXML
    private TableColumn<Appointment,String> doctorAppCol, statusAppCol, dateAppCol, typeAppCol, patientAppCol;
    @FXML
    private TableColumn<Patient,Integer> idPatCol, agePatCol;
    @FXML
    private TableColumn<Patient,String> lnamePatCol, fnamePatCol;
    @FXML
    private TableView<Patient> ptable;
    @FXML
    private TextField idAppointment, dateAppointment, typeAppointment, doctorAppointment, priceAppointment, statusAppointment, patientAppointment;
    @FXML
    private TextField idPatient, lnamePatient, fnamePatient, birthPatient, filterFieldP, filterFieldA;
    @FXML
    private DatePicker filterDate;
    @FXML
    private Slider filterPrice;
    @FXML
    private TextField unLogin, pLogin;
    @FXML
    private Button logBut;


    private Service service;
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private ObservableList<Appointment> filteredAppointmentList = FXCollections.observableArrayList();
    private ObservableList<Patient>  filteredPatientList = FXCollections.observableArrayList();

    public Controller(){}

    @FXML
    public void initialize(){
        idAppCol.setCellValueFactory(new PropertyValueFactory<>("iD"));
        dateAppCol.setCellValueFactory(new PropertyValueFactory<>("strDate"));
        doctorAppCol.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        typeAppCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceAppCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusAppCol.setCellValueFactory(new PropertyValueFactory<>("strStatus"));
        patientAppCol.setCellValueFactory(new PropertyValueFactory<>("patName"));
        aptable.getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> showAppointment(newItem));
        aptable.setItems(appointmentList);

        idPatCol.setCellValueFactory(new PropertyValueFactory<>("iD"));
        lnamePatCol.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        fnamePatCol.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        agePatCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ptable.getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> showPatient(newItem));
        ptable.setItems(patientList);

        filterPrice.setMin(100);
        filterPrice.setMax(1500);

        filterDate.setValue(LocalDate.now());
        filterDate.setEditable(false);
    }

    public void setService(Service serv){
        this.service = serv;
        List<Appointment> apList = serv.getAllAppointment();
        appointmentList.clear();
        appointmentList.addAll(apList);
        appointmentList.sort(new AppointmentIdComparator());
        List<Patient> pList = serv.getAllPatient();
        patientList.clear();
        patientList.addAll(pList);
        patientList.sort(new PatientIdComparator());
    }

    private void showAppointment(Appointment newItem){
        if(newItem == null)
            this.clearAppointmentFields();
        else{
            idAppointment.setText("" + newItem.getID());
            dateAppointment.setText("" + Convert.dateToString(newItem.getDate()));
            typeAppointment.setText(newItem.getType());
            doctorAppointment.setText(newItem.getDoctor());
            priceAppointment.setText("" + newItem.getPrice());
            statusAppointment.setText("" + newItem.getStatus());
            patientAppointment.setText("" + newItem.getPatient());
        }
    }

    private void showPatient(Patient pat){
        if(pat == null)
            this.clearPatientFields();
        else{
            idPatient.setText("" + pat.getID());
            lnamePatient.setText(pat.getLast_name());
            fnamePatient.setText(pat.getFirst_name());
            birthPatient.setText("" +  pat.getBirthYear());
        }
    }

    private void clearPatientFields() {
        idPatient.setText("");
        lnamePatient.setText("");
        fnamePatient.setText("");
        birthPatient.setText("");
    }

    @FXML
    private void clearPatient(ActionEvent e){
        clearPatientFields();
        ptable.getSelectionModel().clearSelection();
    }

    private void clearAppointmentFields(){
        idAppointment.setText("");
        dateAppointment.setText("" );
        typeAppointment.setText("");
        doctorAppointment.setText("");
        priceAppointment.setText("");
        statusAppointment.setText("");
        patientAppointment.setText("");
    }

    @FXML
    private void clearAppointment(ActionEvent e){
        clearAppointmentFields();
        aptable.getSelectionModel().clearSelection();
    }

    @FXML
    private void addPatient(ActionEvent e){
        String lname = lnamePatient.getText();
        String fname = fnamePatient.getText();
        String birth = birthPatient.getText();
        if ("".equals(lname)||"".equals(fname)||"".equals(birth)){
            showErrorMessage("Complete all fields!");
            return;
        }
        try{
            PatientValidator validator = new PatientValidator();
            validator.validData(lname,fname,birth);
            int birthYear = Integer.parseInt(birth);
            Patient p = service.addPatient(lname,fname,birthYear);
            patientList.add(p);
            showNotification("Add successful!");
        }
        catch(ValidationException | ServiceException except){
           showErrorMessage("" + except);

        }
        finally {
            clearPatientFields();
            ptable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void deletePatient(ActionEvent e){
        String id = idPatient.getText();
        if ("".equals(id)){
            showErrorMessage("Complete all fields!");
            return;
        }
        try {
            Integer ID = Integer.parseInt(id);
            service.deletePatient(ID);
            this.setService(service);
            showNotification("Delete successful!");
        }
        catch(ValidationException | ServiceException except){
            showErrorMessage("" + except);
        }
        finally {
            clearPatientFields();
            ptable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void updatePatient(ActionEvent e){
        String id = idPatient.getText();
        String lname = lnamePatient.getText();
        String fname = fnamePatient.getText();
        String birth = birthPatient.getText();
        if ("".equals(id)||"".equals(lname)||"".equals(fname)||"".equals(birth)){
            showErrorMessage("Complete all fields!");
            return;
        }
        try{
            PatientValidator validator = new PatientValidator();
            validator.validData(lname,fname,birth);
            int birthYear = Integer.parseInt(birth);
            Integer ID = Integer.parseInt(id);
            Patient p = service.updatePatient(ID,lname,fname,birthYear);
            int selectedIndex = ptable.getSelectionModel().getSelectedIndex();
            patientList.remove(selectedIndex);
            patientList.add(p);
            patientList.sort(new PatientIdComparator());
            showNotification("Update successful!");
        }
        catch(ValidationException | ServiceException except){
            showErrorMessage("" + except);
        }
        finally {
            clearPatientFields();
            ptable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void addAppointment(ActionEvent e){
        String date = dateAppointment.getText();
        String doctor = doctorAppointment.getText();
        String type = typeAppointment.getText();
        String price = priceAppointment.getText();
        String status = statusAppointment.getText();
        String patient = patientAppointment.getText();
        if ("".equals(date)||"".equals(doctor)||"".equals(type)||"".equals(price)||"".equals(status)||"".equals(patient)){
            showErrorMessage("Complete all fields!");
            return;
        }
        try{
            AppointmentValidator validator = new AppointmentValidator();
            validator.validData(date,doctor,type,price,status,patient);
            Date dateD = Convert.convertDate(date);
            Integer statusI = Integer.parseInt(status);
            Integer patientI = Integer.parseInt(patient);
            Integer priceI = Integer.parseInt(price);
            Appointment app = service.addAppointment(dateD,type,doctor,priceI,statusI,patientI);
            appointmentList.add(app);
            showNotification("Add successful!");
        }
        catch(ValidationException | ServiceException except){
            showErrorMessage("" + except);
        }
        finally {
            clearPatientFields();
            aptable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void deleteAppointment(ActionEvent e){
        String id = idAppointment.getText();
        if ("".equals(id)){
            showErrorMessage("Complete all fields!");
            return;
        }
        try{
            Integer ID = Integer.parseInt(id);
            service.deleteAppointment(ID);
            int selectedIndex = aptable.getSelectionModel().getSelectedIndex();
            appointmentList.remove(selectedIndex);
            showNotification("Delete successful!");
        }
        catch(ValidationException | ServiceException except){
            showErrorMessage("" + except);
        }
        finally {
            clearAppointmentFields();
            aptable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void updateAppointment(ActionEvent e){
        String id = idAppointment.getText();
        String date = dateAppointment.getText();
        String doctor = doctorAppointment.getText();
        String type = typeAppointment.getText();
        String price = priceAppointment.getText();
        String status = statusAppointment.getText();
        String patient = patientAppointment.getText();
        if ("".equals(date)||"".equals(doctor)||"".equals(type)||"".equals(price)||"".equals(status)||"".equals(patient)||"".equals(id)){
            showErrorMessage("Complete all fields!");
            return;
        }
        try{
            AppointmentValidator validator = new AppointmentValidator();
            validator.validData(date,doctor,type,price,status,patient);
            Integer ID = Integer.parseInt(id);
            Date dateD = Convert.convertDate(date);
            Integer statusI = Integer.parseInt(status);
            Integer patientI = Integer.parseInt(patient);
            Integer priceI = Integer.parseInt(price);
            Appointment app = service.updateAppointment(ID,dateD,type,doctor,priceI,statusI,patientI);
            int selectedIndex = aptable.getSelectionModel().getSelectedIndex();
            appointmentList.remove(selectedIndex);
            appointmentList.add(app);
            appointmentList.sort(new AppointmentIdComparator());
            showNotification("Update successful!");
        }
        catch(ValidationException | ServiceException except){
            showErrorMessage("" + except);
        }
        finally {
            clearAppointmentFields();
            aptable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void filterAgePatient(ActionEvent e){
        try {
            filteredPatientList.clear();
            String filter = filterFieldP.getText();
            if("".equals(filter)) throw new ServiceException();
            Integer age = 2020 - Integer.parseInt(filter);
            filteredPatientList.addAll(service.filterAge(age));
            ptable.setItems(filteredPatientList);
            filterFieldP.clear();
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void filterLnamePatient(ActionEvent e){
        try {
            filteredPatientList.clear();
            String filter = filterFieldP.getText();
            if("".equals(filter)) throw new ServiceException();
            filteredPatientList.addAll(service.filterLname(filter));
            ptable.setItems(filteredPatientList);
            filterFieldP.clear();
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void filterFnamePatient(ActionEvent e){
        try {
            filteredPatientList.clear();
            String filter = filterFieldP.getText();
            if("".equals(filter)) throw new ServiceException();
            filteredPatientList.addAll(service.filterFname(filter));
            ptable.setItems(filteredPatientList);
            filterFieldP.clear();
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void filterDateAppointment(ActionEvent e){
        try {
            filteredAppointmentList.clear();
            LocalDate date = filterDate.getValue();
            filteredAppointmentList.addAll(service.filterDate(date));
            aptable.setItems(filteredAppointmentList);
            filterFieldA.clear();
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void filterDoctorAppointment(ActionEvent e){
        try {
            filteredAppointmentList.clear();
            String filter = filterFieldA.getText();
            if("".equals(filter)) throw new ServiceException();
            filteredAppointmentList.addAll(service.filterDoctor(filter));
            aptable.setItems(filteredAppointmentList);
            filterFieldA.clear();
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void filterTypeAppointment(ActionEvent e){
        try {
            filteredAppointmentList.clear();
            String filter = filterFieldA.getText();
            if("".equals(filter)) throw new ServiceException();
            filteredAppointmentList.addAll(service.filterType(filter));
            aptable.setItems(filteredAppointmentList);
            filterFieldA.clear();
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void filterPriceAppointment(MouseEvent e){
        try {
            filteredAppointmentList.clear();
            int price = (int) filterPrice.getValue();
            filteredAppointmentList.addAll(service.filterPrice(price));
            aptable.setItems(filteredAppointmentList);
            filterFieldA.setText(Integer.toString(price));
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void filterPatientAppointment(ActionEvent e){
        try{
            filteredAppointmentList.clear();
            String filter = filterFieldA.getText();
            if("".equals(filter)) throw new ServiceException();
            Integer patient = Integer.parseInt(filter);
            filteredAppointmentList.addAll(service.filterPatient(patient));
            aptable.setItems(filteredAppointmentList);
            filterFieldA.clear();
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void filterStatusAppointment(ActionEvent e){
        try{
            filteredAppointmentList.clear();
            String filter = filterFieldA.getText();
            if("".equals(filter)) throw new ServiceException();
            Integer status = Integer.parseInt(filter);
            filteredAppointmentList.addAll(service.filterStatus(status));
            aptable.setItems(filteredAppointmentList);
            filterFieldA.clear();
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void noFilterPatient(ActionEvent e){
        ptable.setItems(patientList);
        filterFieldP.clear();
    }

    @FXML
    private void noFilterAppointment(ActionEvent e){
        aptable.setItems(appointmentList);
        filterFieldA.clear();
        filterPrice.setValue(100);
        filterDate.setValue(LocalDate.now());
    }

    @FXML
    private void todayAppointment(ActionEvent e){
        try {
            filteredAppointmentList.clear();
            filteredAppointmentList.addAll(service.todayDateAppointment());
            aptable.setItems(filteredAppointmentList);
            filterFieldA.clear();
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void thisDateAppointment (ActionEvent e){
        try {
            filteredAppointmentList.clear();
            LocalDate date = filterDate.getValue();
            filteredAppointmentList.addAll(service.thisDateAppointment(date));
            aptable.setItems(filteredAppointmentList);
            filterFieldA.clear();
        }
        catch(ServiceException except){
            showErrorMessage("" + except);
        }
    }

    @FXML
    private void undoPress (KeyEvent e){
        KeyCodeCombination key = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        if(key.match(e)){
            try {
                Element elem = service.undo();
                this.setService(service);
                showNotification("" + elem);
            }
            catch (ServiceException except){
                showErrorMessage("" + except);
            }
        }
    }

    void showErrorMessage(String text){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Big A$$ Error");
        message.setContentText(text);
        message.showAndWait();
    }

    void showNotification(String text){
        Alert message = new Alert(Alert.AlertType.CONFIRMATION);
        message.setTitle("GJ");
        message.setContentText(text);;
        message.showAndWait();
    }

}
