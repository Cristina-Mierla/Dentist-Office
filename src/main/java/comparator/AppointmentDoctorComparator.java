package comparator;

import domain.Appointment;

import java.util.Comparator;

public class AppointmentDoctorComparator implements Comparator<Appointment> {

    public AppointmentDoctorComparator(){}

    public int compare(Appointment a1, Appointment a2){
        return a1.getDoctor().compareTo(a2.getDoctor());
    }
}
