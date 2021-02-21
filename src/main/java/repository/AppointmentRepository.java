package repository;

import domain.*;
import utilitary.Check;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentRepository extends AbstractRepository<Appointment,Integer> implements AppointmentRepo{

    public AppointmentRepository(){};

    public ArrayList<Appointment> sort(Comparator comp){
        ArrayList<Appointment> sorted = new ArrayList<>();

        for (Appointment appointment : repo.values()) {
            sorted.add(appointment);
        }

        Collections.sort(sorted, comp);
        return sorted;
    }

    @Override
    public List<Appointment> filterDoctor(String doctor) {
        return repo.values().stream().filter(app -> app.getDoctor().equals(doctor)).collect(Collectors.toList());
    }

    @Override
    public List<Appointment> filterPrice(Integer price){
        return repo.values().stream().filter(app -> app.getPrice() >= price).collect(Collectors.toList());
    }

    @Override
     public List<Appointment> filterType(String type){
        List<Appointment> type_filtered_list = new ArrayList<>();
        if(Check.checkType(type)) {
            type_filtered_list = repo.values().stream().filter(app -> app.getType().equals(type)).collect(Collectors.toList());
        }
        return type_filtered_list;
    }

    @Override
     public List<Appointment> filterDate(LocalDate date){
        Date d = new Date(date.getYear() - 1900, date.getMonthValue()+1,date.getDayOfMonth());
        return repo.values().stream().filter(app -> app.getDate().compareTo(d) >= 0).collect(Collectors.toList());
    }

    @Override
    public List<Appointment> filterStatus(Integer status) {
        List<Appointment> status_list = new ArrayList<Appointment>();
        if (status == 0 || status == 1 || status == -1)
            status_list = repo.values().stream().filter(app -> app.getStatus() == status).collect(Collectors.toList());
        return status_list;
    }

    @Override
    public List<Appointment> filterPatient(Integer patient) {
        return repo.values().stream().filter(app -> app.getPatient() == patient).collect(Collectors.toList());
    }
}
