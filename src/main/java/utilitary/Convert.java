package utilitary;

import java.util.*;

public class Convert{

    public static Date convertDate (String string){
        String d = string.replaceAll("\\D","/");
        String[] elem = d.split("/");

        Integer day = Integer.parseInt(elem[0]);
        Integer month = Integer.parseInt(elem[1]);
        Integer year = Integer.parseInt(elem[2]);

        Date date = new Date(year,month,day);
        return date;
    }

    public static String statusToString(Integer status){
        String statusString = "";
        switch(status){
            case(0):
                statusString = "UNFULFILLED";
                break;
            case(-1):
                statusString = "CANCELED";
                break;
            case(1):
                statusString = "FULFILLED";
                break;
            default:
                statusString = "UNKNOWN";
        }
        return statusString;
    }

    public static String dateToString(Date date){
        StringBuffer result = new StringBuffer();
        result.append(date.getDate()).append("/").append(date.getMonth()).append("/").append(date.getYear());
        return result.toString();
    }

    public static String printableDate(Date date){
        StringBuffer result = new StringBuffer();
        result.append(date.getDate()).append("/").append(date.getMonth() + 1).append("/").append(date.getYear() + 1900);
        return result.toString();
    }
}
