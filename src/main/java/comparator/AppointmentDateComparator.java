package comparator;

import domain.Appointment;
import java.util.Comparator;

public class AppointmentDateComparator implements Comparator<Appointment> {

    public AppointmentDateComparator(){}

    public int compare(Appointment a1, Appointment a2){
        int yr = a1.getDate().getYear() - a2.getDate().getYear();
        int mo = a1.getDate().getMonth() - a2.getDate().getMonth();
        int dy = a1.getDate().getDate() - a2.getDate().getDate();
        if(yr == 0)
            if(mo == 0)
                if(dy == 0)
                    return dy;
                else
                    return dy;
            else
                return mo;
        else
            return yr;
    }
}
