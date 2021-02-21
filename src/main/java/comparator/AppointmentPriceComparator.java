package comparator;

import domain.Appointment;

import java.util.Comparator;

public class AppointmentPriceComparator implements Comparator<Appointment> {

    public AppointmentPriceComparator(){}

    public int compare(Appointment a1, Appointment a2){
        return (int) a1.getPrice()-a2.getPrice();
    }
}
