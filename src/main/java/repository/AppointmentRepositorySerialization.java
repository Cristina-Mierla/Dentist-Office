package repository;

import domain.Appointment;

public class AppointmentRepositorySerialization extends AbstractSerialization<Appointment,Integer> {

    public AppointmentRepositorySerialization(String filename){
        super(filename);
    }

}
