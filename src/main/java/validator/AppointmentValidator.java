package validator;

import domain.Appointment;
import exceptions.ValidationException;

public class AppointmentValidator{

    private String date;
    private String type;
    private String doctor;
    private String price;
    private String status;
    private String patient;

    public AppointmentValidator(){}

    public void valid(Appointment appointment){
        try{
            this.date = appointment.getDate().getYear() + "/" + appointment.getDate().getMonth() + "/" + appointment.getDate().getDate();
            this.type = appointment.getType();
            this.doctor = appointment.getDoctor();
            this.price = String.valueOf(appointment.getPrice());
            this.status = String.valueOf(appointment.getStatus());
            this.patient = String.valueOf(appointment.getPatient());

            this.validType();
            this.validPrice();
            this.validStatus();
            this.validPatient();
        }
        catch(ValidationException except){
            throw new ValidationException(except);
        }
    }

    public void validData(String date,String type,String doctor,String price, String status,String patient){
        try{
            this.date = date;
            this.type = type;
            this.doctor = doctor;
            this.price = price;
            this.status = status;
            this.patient = patient;

            this.validDateData(date);
            this.validNameData(type);
            this.validNameData(doctor);
            this.validNumberData(price);
            this.validNumberData(status);
            this.validNumberData(patient);
        }
        catch(ValidationException except){
            throw new ValidationException(except);
        }
    }

    private void validNumberData(String num){
        try {
            if(utilitary.Check.checkNumber(num)) {
                throw new ValidationException("Appointment with invalid number." + num);
            }
            Integer.parseInt(num);
        }
        catch(ValidationException | NumberFormatException except){
            throw new ValidationException(except);
        }
    }

    private void validNameData(String name){
        try {
            if (utilitary.Check.checkName(name)) {
                throw new ValidationException("Appointment with invalid string.");
            }
        }
        catch(ValidationException except) {
            throw new ValidationException(except);
        }
    }

    private void validDateData(String date){
        try {
            if(utilitary.Check.checkDate(date))
                throw new ValidationException("Appointment with invalid date.");
        }
        catch(ValidationException | NumberFormatException except) {
            throw new ValidationException(except);
        }
    }

    private void validType(){
        String[] validTypes = Appointment.Types;
        try{
            boolean ok = false;
            for(String elem:validTypes){
                if(elem.equals(type)) {
                    ok = true;
                    break;
                }
            }
            if(!ok)
                throw new ValidationException("Appointment with invalid type.");
        }
        catch(ValidationException except){
            throw new ValidationException(except);
        }
    }

    private void validPrice(){
        try{
            Integer intPrice = Integer.parseInt(price);
            if(intPrice < 0)
                throw new ValidationException("Appointment with invalid price.");
        }
        catch(ValidationException | NumberFormatException except){
            throw new ValidationException(except);
        }
    }

    private void validStatus(){
        try{
            Integer intStatus = Integer.parseInt(status);
            if(intStatus < -1 || intStatus > 1)
                throw new ValidationException("Appointment with invalid status.");
        }
        catch(ValidationException | NumberFormatException except){
            throw new ValidationException(except);
        }
    }

    private void validPatient(){
        try{
            Integer intId = Integer.parseInt(patient);
            if(intId < 100 || intId > 999)
                throw new ValidationException("Appointment with invalid Patient id.");
        }
        catch(ValidationException | NumberFormatException except) {
            throw new ValidationException(except);
        }
    }

}
