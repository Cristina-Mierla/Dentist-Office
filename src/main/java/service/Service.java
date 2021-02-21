package service;

import comparator.PatientIdComparator;
import domain.Appointment;
import domain.Patient;
import exceptions.RepositoryException;
import exceptions.ServiceException;
import exceptions.ValidationException;
import repository.AppointmentRepo;
import repository.PatientRepo;
import undo.Element;
import undo.Stack;
import validator.AppointmentValidator;
import validator.PatientValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Service {

    private final PatientRepo patient_list;
    private final AppointmentRepo appointment_list;
    private Stack myStack;

    public Service(PatientRepo patient_list, AppointmentRepo appointment_list){

        this.patient_list = patient_list;
        this.appointment_list = appointment_list;
        myStack = new Stack();
        //this.populate();
    }

    public Patient addPatient(String last_name,String first_name,Integer year){
        try{
            Patient newPatient = new Patient(last_name,first_name,year);
            PatientValidator validator = new PatientValidator();
            validator.valid(newPatient);
            patient_list.add(newPatient);

            Element elem = new Element("add",newPatient);
            myStack.push(elem);

            return newPatient;
        }
        catch(ValidationException except){
            throw new ValidationException("Invalid arguments! " + except);
        }
        catch(RepositoryException except){
            throw new RepositoryException("Could not add the new patient! " + except);                                         //new Service Exception
        }
    }

    public Appointment addAppointment(Date date, String type, String doctor, Integer price, Integer status, Integer patient){
        try{
            Patient newPat = patient_list.findById(patient);
            Appointment newAppointment = new Appointment (date,type,doctor,price,status,patient,newPat);
            AppointmentValidator validator = new AppointmentValidator();
            validator.valid(newAppointment);
            appointment_list.add(newAppointment);

            Element elem = new Element("add",newAppointment);
            myStack.push(elem);

            return newAppointment;
        }
        catch(ValidationException except){
            throw new ValidationException("Invalid arguments! " + except);
        }
        catch(RepositoryException except) {
            throw new ServiceException("Could not add the new appointment! " + except);
        }
    }

    public void deletePatient(Integer id){
        try{
            Patient p = patient_list.findById(id);
            patient_list.delete(p);

            for(int i = appointment_list.getCollection().size()-1;i>=0;i--){
                Appointment app = appointment_list.findById(1000+i);
                if(app.getPatient().equals(id)) {
                    this.deleteAppointment(app.getID());
                }
            }

            Element elem = new Element("delete",p);
            myStack.push(elem);

        }
        catch(RepositoryException except) {
            throw new ServiceException("Could not delete the patient with the given id! " + except);
        }
    }

    public void deleteAppointment(Integer id){
        try{
            Appointment a = appointment_list.findById(id);
            appointment_list.delete(a);

            Element elem = new Element("delete",a);
            myStack.push(elem);

        }
        catch(RepositoryException except){
            throw new ServiceException("Could not delete the appointment with the given id! " + except);
        }
    }

    public Patient updatePatient(Integer id, String last_name,String first_name,Integer year){
        try{
            Patient newPatient = new Patient(last_name,first_name,year);
            Patient oldPatient = patient_list.findById(id);
            newPatient.setID(id);
            PatientValidator validator = new PatientValidator();
            validator.valid(newPatient);
            patient_list.update(newPatient,id);

            Element elem = new Element("update",oldPatient);
            myStack.push(elem);

            return newPatient;
        }
        catch(ValidationException except){
            throw new ValidationException("Invalid arguments! " + except);
        }
        catch(RepositoryException except){
            throw new ServiceException("Could not update the patient with the given id! " + except);
        }
    }

    public Appointment updateAppointment(Integer id, Date date, String type, String doctor, Integer price, Integer status, Integer patient){
        try{
            Patient p = patient_list.findById(patient);
            Appointment newAppointment = new Appointment (date,type,doctor,price,status,patient,p);
            Appointment oldAppointment = appointment_list.findById(id);
            newAppointment.setID(id);
            AppointmentValidator validator = new AppointmentValidator();
            validator.valid(newAppointment);
            appointment_list.update(newAppointment,id);

            Element elem = new Element("update",oldAppointment);
            myStack.push(elem);

            return newAppointment;
        }
        catch(ValidationException except){
            throw new ValidationException("Invalid arguments! " + except);
        }
        catch(RepositoryException except){
            throw new ServiceException("Could not update the appointment with the given id! " + except);
        }
    }

    public Element undo(){
        try {
            Element elem = myStack.pop();
            if (elem.getElement() == Element.obj.pat) {
                Patient pat = (Patient) elem.getObject();
                if (elem.getOperation() == Element.type.add) {
                    patient_list.delete(pat);
                }
                if (elem.getOperation() == Element.type.delete) {
                    patient_list.add(pat);
                    Appointment apoint = (Appointment) myStack.getLast().getObject();
                    while(apoint.getPatient().equals(pat.getID()) && myStack.getStack().size() > 0){
                        undo();
                        if(myStack.getStack().size() > 0)
                            apoint = (Appointment) myStack.getLast().getObject();
                    }
                }
                if (elem.getOperation() == Element.type.update){
                    patient_list.update(pat,pat.getID());
                }
            }
            else if (elem.getElement() == Element.obj.app){
                Appointment app = (Appointment) elem.getObject();
                if(elem.getOperation() == Element.type.add){
                    appointment_list.delete(app);
                }
                if(elem.getOperation() == Element.type.delete){
                    appointment_list.add(app);
                }
                if(elem.getOperation() == Element.type.update){
                    appointment_list.update(app,app.getID());
                }
            }
            return elem;
        }
        catch (ServiceException except){
            throw new ServiceException("Undo failed " + except);
        }
    }

    public String printPatient(){
        return patient_list.toString();
    }

    public String printAppointment(){
        return appointment_list.toString();
    }

    public List<Patient> getAllPatient(){
        List<Patient> patients = new ArrayList<Patient>();
        for(Patient p: patient_list.getAll()){
            patients.add(p);
        }
        return patients;
    }

    public List<Appointment> getAllAppointment(){
        List<Appointment> appointments = new ArrayList<Appointment>();
        for(Appointment a: appointment_list.getAll()){
            appointments.add(a);
        }
        return appointments;
    }

    public List<Patient> filterAge(Integer year){
        return patient_list.filterAge(year);
    }

    public List<Patient> filterLname(String lname){
        return patient_list.filterLname(lname);
    }

    public List<Patient> filterFname(String fname){
        return patient_list.filterFname(fname);
    }

    public List<Appointment> filterDoctor(String doctor){
         return appointment_list.filterDoctor(doctor);
    }

    public List<Appointment> filterPrice(Integer price){
        return appointment_list.filterPrice(price);
    }

    public List<Appointment> filterType(String type){
        return appointment_list.filterType(type);
    }

    public List<Appointment> filterDate (LocalDate date){
        return appointment_list.filterDate(date);
    }

    public List<Appointment> filterStatus (Integer status) {
        return appointment_list.filterStatus(status);
    }

    public List<Appointment> filterPatient (Integer patient){
        return appointment_list.filterPatient(patient);
    }

    public List<Appointment> todayDateAppointment(){
        LocalDate currentDate = LocalDate.now();
        return appointment_list.getCollection().stream().filter(app -> app.getDate().getYear()+1900==currentDate.getYear() && app.getDate().getMonth()+1==currentDate.getMonthValue() && app.getDate().getDate()== currentDate.getDayOfMonth()).collect(Collectors.toList());
    }

    public List<Appointment> thisDateAppointment(LocalDate date) {
        return appointment_list.getCollection().stream().filter(app -> app.getDate().getYear()+1900==date.getYear() && app.getDate().getMonth()+1==date.getMonthValue() && app.getDate().getDate()== date.getDayOfMonth()).collect(Collectors.toList());
    }

    public void randomPatient(Integer number){
        int i = 1,j;
        String lowerLetter = "abcdefghijklmnopqrstuvxyz";
        String upperLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        int nrLetters;
        int letter;

        while(i<=number){
            String lname = "";
            String fname = "";
            letter = rand.ints(0,25).findAny().getAsInt();
            lname += upperLetter.charAt(letter);
            letter = rand.ints(0,25).findAny().getAsInt();
            fname += upperLetter.charAt(letter);
            nrLetters = rand.ints(3,12).findAny().getAsInt();
            j = 1;
            while(j<=nrLetters){
                letter = rand.ints(0,25).findAny().getAsInt();
                lname += lowerLetter.charAt(letter);
                j++;
            }
            nrLetters = rand.ints(3,12).findAny().getAsInt();
            j = 1;
            while(j<=nrLetters){
                letter = rand.ints(0,25).findAny().getAsInt();
                fname += lowerLetter.charAt(letter);
                j++;
            }

            Integer birthYear = 1900 + rand.ints(20,120).findAny().getAsInt();

            this.addPatient(lname,fname,birthYear);

            i++;
        }
    }

    public void randomAppointment(Integer number){
        int i = 0,j;
        String lowerLetter = "abcdefghijklmnopqrstuvxyz";
        String upperLetter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Integer[] stas = {-1,0,1};
        Random rand = new Random();
        int t = 0, y = 0, m = 0, d = 0;
        int nrLetters;
        int letter;

        int nrDoc = rand.ints(1,11).findAny().getAsInt();
        List<String> doctors = appointment_list.getCollection().stream().map(app -> app.getDoctor()).distinct().collect(Collectors.toList());
        while(i<nrDoc){
            String docName = "";
            letter = rand.ints(0,25).findAny().getAsInt();
            docName += upperLetter.charAt(letter);
            nrLetters = rand.ints(3,12).findAny().getAsInt();
            j = 1;
            while(j<=nrLetters){
                letter = rand.ints(0,25).findAny().getAsInt();
                docName += lowerLetter.charAt(letter);
                j++;
            }
            doctors.add(docName);
            i++;
        }
        nrDoc = doctors.size();

        i = 1;
        while(i<=number){
            y = rand.ints(117,123).findAny().getAsInt();
            m = rand.ints(0,12).findAny().getAsInt();
            d = rand.ints(1,32).findAny().getAsInt();
            Date date = new Date(y,m,d);

            t = rand.ints(0,10).findAny().getAsInt();
            String type = Appointment.Types[t];

            String doctor = doctors.get(rand.ints(0,nrDoc).findAny().getAsInt());

            Integer price = rand.ints(2,31).findAny().getAsInt() * 50;

            Integer status = stas[rand.ints(0,3).findAny().getAsInt()];

            Integer maxId = patient_list.getCollection().stream().max(new PatientIdComparator()).get().getID();
            Integer patient = rand.ints(100,maxId+1).findAny().getAsInt();

            this.addAppointment(date,type,doctor,price,status,patient);

            i++;
        }
    }

    public void populate(){

        Patient p1 = new Patient("Mierla","Cristina",2000);
        Patient p2 = new Patient("Negrea","Cosmin",1998);
        Patient p3 = new Patient("Dumitru","Mihai",2008);
        Patient p4 = new Patient("Popescu","Alexa",2013);
        Patient p5 = new Patient("Moraru","Daniela",1985);
        Patient p6 = new Patient("Ciuciu","Andrei",1995);
        Patient p7 = new Patient("Negrea","Diana",1998);
        Patient p8 = new Patient("Doica","Razvan",1999);
        Patient p9 = new Patient("Dimitrie","Mihai",1890);
        Patient p10 = new Patient("Bambi","Girl",1978);
        Patient p11 = new Patient("Barbie","Leonora",1997);
        Patient p12 = new Patient("Gica","Hagi",1987);
        Patient p13 = new Patient("Valeriu","Flaviu",2012);

        patient_list.add(p1);
        patient_list.add(p2);
        patient_list.add(p3);
        patient_list.add(p4);
        patient_list.add(p5);
        patient_list.add(p6);
        patient_list.add(p7);
        patient_list.add(p8);
        patient_list.add(p9);
        patient_list.add(p10);
        patient_list.add(p11);
        patient_list.add(p12);
        patient_list.add(p13);


        Date d1 = new Date(120,9,10);
        Date d2 = new Date(119,0,15);
        Date d3 = new Date(120,3,22);
        Date d4 = new Date(121,8,13);
        Date d5 = new Date(118,11,21);
        Date d6 = new Date(117,1,2);
        Date d7 = new Date(122,0,1);
        Date d8 = new Date(121,2,12);
        Date d9 = new Date(120,4,26);
        Date d10 = new Date(119,7,19);
        Date d11 = new Date(123,10,16);
        Date d12 = new Date();


        Appointment ap1 = new Appointment(d1,"Extraction","Richard",300,1,100,p1);
        Appointment ap2 = new Appointment(d2,"Whitening","Richard",400,1,101,p2);
        Appointment ap3 = new Appointment(d3,"Gum Surgery","Rodrighez",200,-1,102,p3);
        Appointment ap4 = new Appointment(d4,"Root Canals","Dominic",500,0,103,p4);
        Appointment ap5 = new Appointment(d5,"Bonding","Dominic",700,-1,104,p5);
        Appointment ap6 = new Appointment(d6,"Braces","Patricia",1100,1,105,p6);
        Appointment ap7 = new Appointment(d7,"Implants","Monica",1400,-1,106,p7);
        Appointment ap8 = new Appointment(d8,"Routine Visit","Rodrighez",600,0,107,p8);
        Appointment ap9 = new Appointment(d9,"Repairs","Kimiqo",150,0,108,p9);
        Appointment ap10 = new Appointment(d10,"Braces","Patricia",950,1,109,p10);
        Appointment ap11 = new Appointment(d11,"Fillings","Dominic",750,1,110,p11);
        Appointment ap12 = new Appointment(d12,"RootCanals","Danken",400,-1,111,p12);
        Appointment ap13 = new Appointment(d4,"Whitening","Felticiuc",200,0,112,p13);

        appointment_list.add(ap1);
        appointment_list.add(ap2);
        appointment_list.add(ap3);
        appointment_list.add(ap4);
        appointment_list.add(ap5);
        appointment_list.add(ap6);
        appointment_list.add(ap7);
        appointment_list.add(ap8);
        appointment_list.add(ap9);
        appointment_list.add(ap10);
        appointment_list.add(ap11);
        appointment_list.add(ap12);
        appointment_list.add(ap13);
    }


}
