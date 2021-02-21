package repository;

import domain.Patient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PatientRepository extends AbstractRepository<Patient,Integer> implements PatientRepo{

    public PatientRepository(){}

    public ArrayList<Patient> sort(Comparator comp){
        ArrayList<Patient> sorted = new ArrayList<Patient>();

        sorted.addAll(repo.values());

        sorted.sort(comp);
        return sorted;
    }

    public List<Patient> filterAge(int year) {
        return repo.values().stream().filter(pat -> pat.getBirthYear() <= year).collect(Collectors.toList());
    }

    public List<Patient> filterLname(String lname){
        return repo.values().stream().filter(pat -> pat.getLast_name().equals(lname)).collect(Collectors.toList());
    }

    public List<Patient> filterFname(String fname){
        return repo.values().stream().filter(pat -> pat.getFirst_name().equals(fname)).collect(Collectors.toList());
    }
}
