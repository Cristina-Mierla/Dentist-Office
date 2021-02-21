package repository;

import domain.Appointment;
import domain.Patient;
import exceptions.FileException;
import exceptions.RepositoryException;
import exceptions.ValidationException;
import validator.AppointmentValidator;

import java.io.*;
import java.util.Date;

public class AppointmentRepositoryFile extends AppointmentRepository{
    private final String filename;
    private final PatientRepo patients;

    public AppointmentRepositoryFile(String filename,PatientRepo patients){
        this.filename = filename;
        this.patients = patients;

        this.readFromFile();
    }

    private void readFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){

            String line;
            while((line = reader.readLine()) != null) {
                String[] elem = line.split("[|]");
                try {
                    if (elem.length < 6) {
                        throw new FileException("Not enough elements on this line!");
                    }
                    AppointmentValidator validator = new AppointmentValidator();
                    validator.validData(elem[0],elem[1],elem[2],elem[3],elem[4],elem[5]);
                    Integer pat = Integer.parseInt(elem[5]);
                    Patient patient = patients.findById(pat);
                    Date date = utilitary.Convert.convertDate(elem[0]);
                    Appointment appointment = new Appointment(date,elem[1],elem[2],Integer.parseInt(elem[3]),Integer.parseInt(elem[4]),pat,patient);
                    validator.valid(appointment);

                    super.add(appointment);
                }
                catch(FileException except){
                    throw new RepositoryException("File error! " + except);
                }
                catch(ValidationException except){
                    throw new RepositoryException("File error! Invalid arguments!" + except);
                }
                catch(NumberFormatException except){
                    throw new RepositoryException("File error! Incorrect arguments!" + except);
                }
            }
        }
        catch(FileNotFoundException except){
            throw new RepositoryException("Error file not found! " + except);
        }
        catch(FileException except){
            throw new RepositoryException("File error! " + except);
        }
        catch(IOException except){
            System.err.println("Other file error! " + except);
        }
    }

    private void writeToFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for(Appointment appointment:super.getAll()){
                writer.write(appointment.fileConvert());
                writer.newLine();
            }
        }
        catch(FileNotFoundException except){
            System.err.println("Error file not found! " + except);
        }
        catch(FileException except){
            System.err.println("File error! " + except);
        }
        catch(IOException except){
            System.err.println("Other file error! " + except);
        }
    }

    public void add(Appointment appointment){
        try{
            super.add(appointment);
            writeToFile();
        }
        catch(RepositoryException except) {
            throw new RepositoryException("Error adding a new element: " + except);
        }
    }

    public void delete(Appointment appointment){
        try{
            super.delete(appointment);
            writeToFile();
        }
        catch(RepositoryException except) {
            throw new RepositoryException("Error deleting an element: " + except);
        }
    }

    public void update(Appointment appointment,Integer id){
        try{
            super.update(appointment,id);
            writeToFile();
        }
        catch(RepositoryException except) {
            throw new RepositoryException("Error updating the element with the id " + id + ": " + except);
        }
    }

}
