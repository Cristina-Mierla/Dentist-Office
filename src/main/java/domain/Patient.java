package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Patient implements Identifiable<Integer>, Serializable {

    private Integer iD;
    private String last_name;
    private String first_name;
    private Integer birthYear;

    private Integer age;
    public Integer getAge(){return age;}

    public Patient(){}

    public Patient(String last_name,String first_name,Integer year){
        this.last_name = last_name;
        this.first_name = first_name;
        this.birthYear = year;
        //pid = (int)Math.floor(Math.random() * 8999 + 1001);
        iD = PatientIdGenerator.getNext();
        age = LocalDate.now().getYear() - birthYear;
        //age = 2020 - birthYear;
    }

    public Integer getBirthYear(){
        return birthYear;
    }
    public void setBirthYear(Integer age){
        this.birthYear = age;
    }

    public String getLast_name(){
        return last_name;
    }
    public void setLast_name(String last_name){
        this.last_name = last_name;
    }

    public String getFirst_name(){
        return first_name;
    }
    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }

    public Integer getID(){
        return iD;
    }
    public void setID(Integer id){ iD = id; }

    public String toString(){
        StringBuffer result = new StringBuffer();
        Integer age = 0;
        Date currentDate = new Date();
        age = 1900 + currentDate.getYear() - birthYear;
        //result.append(pid).append(" ").append(last_name).append(" ").append(first_name).append(" ").append(age);
        //return result.toString();

        String res = String.format("Id: %d   %-13s %-13s Age: %-2d",iD,last_name,first_name,age);
        return res;
    }

    public boolean equals(Object obj){
        if(obj instanceof Patient){
            Patient p = (Patient) obj;
            return p.last_name.equals(last_name) && p.first_name.equals(first_name) && p.birthYear.equals(birthYear);
        }
        return false;
    }

    public String fileConvert(){
        StringBuffer result = new StringBuffer();
        result.append(last_name).append("|").append(first_name).append("|").append(birthYear);
        return result.toString();
    }

}
