package repository;

import domain.Patient;

public class PatientRepositorySerialization extends AbstractSerialization<Patient,Integer> {

    public PatientRepositorySerialization(String filename){
        super(filename);
    }
}
