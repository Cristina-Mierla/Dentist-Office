package comparator;

import domain.Patient;

import java.util.Comparator;

public class PatientIdComparator implements Comparator<Patient> {

    public PatientIdComparator(){}

    public int compare(Patient p1, Patient p2){
        return (int) (p1.getID()-p2.getID());
    }
}
