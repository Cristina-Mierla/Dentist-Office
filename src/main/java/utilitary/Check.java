package utilitary;

import domain.Appointment;
import exceptions.ValidationException;

import java.util.Date;

public class Check {

    public static boolean checkName(String name){
        String f1 = name.replaceAll("\\d", "0");
        String f2 = name.replaceAll("\\p{Punct}","0");
        return f1.contains("0") || f2.contains("0");
    }

    public static boolean checkNumber(String number){
        if(number.charAt(0) == '-')
            number = number.replaceFirst("\\-","0");
        String num = number.replaceAll("\\D","_");
        return num.contains("_");
    }

    public static boolean checkDate(String date){
        String d1 = date.replaceAll("\\p{Alpha}", "Q");
        if(d1.contains("Q")){
            throw new ValidationException("Appointment with invalid date.");
        }
        String d = date.replaceAll("\\D","/");
        String[] elem = d.split("/");
        return elem.length != 3;
    }

    public static boolean checkType(String type){
        for (String elem: Appointment.Types){
            if(elem.equals(type)){
                return true;
            }
        }
        return false;
    }

    public static boolean checkEqualDate(Date d1, Date d2){
        return d1.getYear()==d2.getYear() && d1.getMonth()==d2.getMonth() && d1.getDate()==d2.getDate();
    }
}
