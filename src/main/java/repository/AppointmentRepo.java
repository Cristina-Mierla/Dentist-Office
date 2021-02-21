package repository;

import domain.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepo extends Repository <Appointment,Integer> {

    List<Appointment> filterDoctor (String doctor);
    List<Appointment> filterType (String type);
    List<Appointment> filterDate (LocalDate date);
    List<Appointment> filterPrice (Integer price);
    List<Appointment> filterStatus (Integer status);
    List<Appointment> filterPatient (Integer patient);
}
