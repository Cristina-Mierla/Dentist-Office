package domain;

public class PatientIdGenerator {

    private static Integer id = 100;

    public static Integer getNext(){ return id++;}

}


