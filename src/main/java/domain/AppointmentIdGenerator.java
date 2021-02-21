package domain;

public class AppointmentIdGenerator {

    private static Integer idGen = 1000;

    public static Integer getNext(){return idGen++; }
}
