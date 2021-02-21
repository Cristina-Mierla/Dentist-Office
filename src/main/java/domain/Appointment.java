package domain;

import utilitary.Check;
import utilitary.Convert;

import java.io.Serializable;
import java.util.Date;

public class Appointment implements Identifiable<Integer>, Serializable {

    public static String[] Types = {"Extraction","Bonding","Gum Surgery","Root Canals","Braces","Routine Visit","Implants","Repairs","Fillings","Whitening"};

    private Integer iD = 0;
    private Date date = new Date();
    private String type;
    private String doctor;
    private Integer price = 0;
    private Integer status = 0; //0 - unfulfilled  -1 - canceled  1 - fulfilled
    private Integer patient = 0; //patient ID


    private Patient pat;

    private String strDate;
    public String getStrDate(){ return Convert.printableDate(date);}

    private String strStatus;
    public String getStrStatus(){
        return Convert.statusToString(status);
    }

    private String patName;
    public String getPatName(){
        StringBuffer result = new StringBuffer();
        result.append(pat.getLast_name()).append(" ").append(pat.getFirst_name());
        return result.toString();
    }


    public Appointment(){
    }

    public Appointment(Date date, String type, String doctor, Integer price, Integer status, Integer patient){
        this.date = date;
        this.type = type;
        this.doctor = doctor;
        this.price = price;
        this.patient = patient;
        this.status = status;
        iD = AppointmentIdGenerator.getNext();
    }

    public Appointment(Date date, String type, String doctor, Integer price, Integer status, Integer patient, Patient pati){
        this.date = date;
        this.type = type;
        this.doctor = doctor;
        this.price = price;
        this.patient = patient;
        this.status = status;
        iD = AppointmentIdGenerator.getNext();
        pat = pati;
    }

    public Integer getID(){ return iD; }
    public void setID(Integer id){ iD = id; }

    public Date getDate(){
        return date;
    }
    public void setDate(Date date){
        this.date = date;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }

    public String getDoctor(){
        return doctor;
    }
    public void setDoctor(String doctor){
        this.doctor = doctor;
    }

    public Integer getPrice(){
        return price;
    }
    public void setPrice(Integer price){
        this.price = price;
    }

    public Integer getPatient(){
        return patient;
    }
    public void setPatient(Integer patient){
        this.patient = patient;
    }

    public Integer getStatus(){return status; }
    public void setStatus(Integer status) {this.status = status;}

    public String toString(){
        String statusString = utilitary.Convert.statusToString(status);
        StringBuffer result = new StringBuffer();
        String[] d = date.toLocaleString().split(",");

        String res = String.format("Id: %d  %12s  %-13s %5d RON  by Dr. %-15s Patient id: %d - %-3s",iD,d[0],type,price,doctor,patient,statusString);
        return res;

        //result.append("Id: ").append(aid).append(" ").append(d[0]).append(" ").append(type).append(" ").append(price).append(" RON by Dr. ").append(doctor).append(" patient id: ").append(patient).append(" - ").append(statusString);
        //return result.toString();
    }

    public boolean equals(Object obj){
        if(obj instanceof Appointment){
            Appointment a = (Appointment) obj;
            if (Check.checkEqualDate(a.getDate(), date))
                if (a.type.equals(type))
                    if (a.doctor.equals(doctor))
                        if (a.price.equals(price))
                            if (a.patient.equals(patient))
                                if (a.status.equals(status))
                                    return true;
            return false;
        }
        return false;
    }

    public String fileConvert(){
        StringBuffer result = new StringBuffer();
        String dateString = utilitary.Convert.dateToString(date);
        result.append(dateString).append("|").append(type).append("|").append(doctor).append("|").append(price).append("|").append(status).append("|").append(patient);
        return result.toString();
    }
}
