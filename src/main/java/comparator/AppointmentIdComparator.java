package comparator;

import domain.Appointment;

import java.util.*;

public class AppointmentIdComparator implements Comparator<Appointment>{

    public AppointmentIdComparator(){}

    public int compare(Appointment a1, Appointment a2){
        return (int) a1.getID()-a2.getID(); //downcast
    }
}
