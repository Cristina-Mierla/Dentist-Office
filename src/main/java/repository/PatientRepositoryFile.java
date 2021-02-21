package repository;

import exceptions.*;
import domain.Patient;
import validator.PatientValidator;
import java.io.*;

public class PatientRepositoryFile extends PatientRepository {
    private final String filename;

    public PatientRepositoryFile(String filename){
        this.filename = filename;

        this.readFromFile();
    }

    private void readFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = reader.readLine()) != null){
                String[] elem = line.split("[|]");
                try{
                    if (elem.length < 3) {
                        throw new FileException("Not enough elements on this line!");
                    }
                    PatientValidator validator = new PatientValidator();
                    validator.validData(elem[0],elem[1],elem[2]);
                    Patient patient = new Patient(elem[0],elem[1],Integer.parseInt(elem[2]));
                    validator.valid(patient);

                    super.add(patient);
                }
                catch(FileException except){
                    System.err.println("File error! " + except);
                }
                catch(ValidationException except){
                    System.err.println("File error! Invalid arguments! " + except);
                }
                catch(NumberFormatException except){
                    System.err.println("File error! Incorrect arguments! " + except);
                }
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

    private void writeToFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for(Patient patient:super.getAll()){
                writer.write(patient.fileConvert());
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

    public void add (Patient patient)  {
        try{
            super.add(patient);
            writeToFile();
        }
        catch(RepositoryException except){
            throw new RepositoryException("Error adding a new patient: " + except);
        }
    }

    public void delete(Patient patient){
        try{
            super.delete(patient);
            writeToFile();
        }
        catch(RepositoryException except){
            throw new RepositoryException("Error deleting a patient: " + except);
        }
    }

    public void update(Patient patient, Integer id){
        try{
            super.update(patient,id);
            writeToFile();
        }
        catch(RepositoryException except){
            throw new RepositoryException("Error updating the patient with the id " + id + ": " + except);
        }
    }

}
