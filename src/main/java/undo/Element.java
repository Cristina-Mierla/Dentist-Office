package undo;

import domain.Appointment;
import domain.Patient;
import exceptions.ServiceException;

public class Element {

    public enum obj {pat,app};
    public enum type {add,update,delete};

    private obj element;
    private type operation;
    private Object object;

    public Element(String newType, Object newObject){
        switch (newType) {
            case "add":
                operation = type.valueOf("add");
                break;
            case "update":
                operation = type.valueOf("update");
                break;
            case "delete":
                operation = type.valueOf("delete");
                break;
            default:
                throw new ServiceException("Invalid type for undo ");
        }
        if(newObject instanceof Patient){
            element = obj.valueOf("pat");
            object = newObject;
        } else if(newObject instanceof Appointment){
            element = obj.valueOf("app");
            object = newObject;
        } else {
            throw new ServiceException("Invalid object for undo ");
        }
    }

    public obj getElement(){return element;}
    public void setElement(obj Elem){element = Elem;}
    public type getOperation(){return operation;}
    public void setOperation(type Op){operation = Op;}
    public Object getObject(){return object;}
    public void setObject(Object obj){object = obj;}

    public String toString(){
        String op = "";
        String ob = "";
        if(operation.equals(type.add))
            op = "ADD";
        else if(operation.equals(type.update))
            op = "UPDATE";
        else if(operation.equals(type.delete))
            op = "DELETE";
        if(element.equals(obj.pat))
                ob = "Patient";
        else if(element.equals(obj.app))
                ob= "Appointment";
        StringBuffer result = new StringBuffer();
        result.append("Undo operation: ").append(op).append(" with element of type ").append(ob);
        return result.toString();
    }
}
