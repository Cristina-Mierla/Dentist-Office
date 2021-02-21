package repository;

import domain.Patient;
import java.util.List;

public interface PatientRepo extends Repository<Patient,Integer>{

    List<Patient> filterAge (int year);
    List<Patient> filterLname (String name);
    List<Patient> filterFname (String name);
}
