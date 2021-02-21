package comparator;

import domain.Patient;

import java.util.Comparator;

public class PatientAgeComparator implements Comparator<Patient> {

    public PatientAgeComparator(){}

    public int compare(Patient p1, Patient p2){
        return p1.getBirthYear() - p2.getBirthYear();
    }
}
