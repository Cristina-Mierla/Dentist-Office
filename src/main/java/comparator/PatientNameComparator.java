package comparator;

import domain.Patient;
import java.util.Comparator;

public class PatientNameComparator implements Comparator<Patient>{

    public PatientNameComparator(){}

    public int compare(Patient p1, Patient p2){
        return p1.getLast_name().compareTo(p2.getLast_name());
    }
}
