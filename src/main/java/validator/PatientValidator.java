package validator;

import domain.Patient;
import exceptions.ValidationException;
import utilitary.Check;
import java.util.Date;

public class PatientValidator{

    private String lastName;
    private String firstName;
    private String birthYear;

    public PatientValidator(){
    }

    public void validData(String lastName, String firstName, String birthYear){
        try{
            this.lastName = lastName;
            this.firstName = firstName;
            this.birthYear = birthYear;

            this.validNameData(lastName);
            this.validNameData(firstName);
            this.validNumberData(birthYear);
        }
        catch(ValidationException | NumberFormatException except){
            throw new ValidationException(except);
        }
    }

    public void valid(Patient patient){
        try{
            this.lastName = patient.getLast_name();
            this.firstName = patient.getFirst_name();
            this.birthYear = String.valueOf(patient.getBirthYear());

            this.validYear();
        }
        catch(ValidationException except){
            throw new ValidationException(except);
        }
    }

    private void validNumberData(String num){
        if(Check.checkNumber(num)){
            throw new ValidationException("Patient with invalid number.");
        }
        try {
            Integer.parseInt(num);
        }
        catch(NumberFormatException except){
            throw new ValidationException(except);
        }
    }

    private void validNameData(String name){
        if(Check.checkName(name)){
            throw new ValidationException("Patient with invalid string.");
        }
    }

    private void validYear(){
        try{
            Integer intBirthYear = Integer.parseInt(birthYear);
            Date currentDate = new Date();
            Integer currentYear = 1900 + currentDate.getYear();
            if(intBirthYear > currentYear)
                throw new ValidationException("Patient with invalid year.");
        }
        catch(ValidationException | NumberFormatException except){
            throw new ValidationException(except);
        }
    }
}
