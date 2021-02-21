package repository;

import exceptions.RepositoryException;
import domain.Identifiable;

import java.io.*;
import java.util.Map;

public class AbstractSerialization<T extends Identifiable<Tid>,Tid> extends AbstractRepository<T,Tid>{

    private final String filename;

    AbstractSerialization(String filename){
        this.filename = filename;

        this.readFromFile();
    }

    @SuppressWarnings("unchecked")
    private void readFromFile(){
        try(ObjectInputStream reader = new ObjectInputStream(new FileInputStream(filename))){
            repo = (Map<Tid, T>) reader.readObject();
        }
        catch(FileNotFoundException except){
            System.err.println("Error file not found! " + except);
        }
        catch(ClassNotFoundException except){
            System.err.println("Other file error! " + except);
        }
        catch(IOException except){
            System.err.println("Other file error! " + except);
        }

    }

    private void writeToFile(){
        try(ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(filename))){
            writer.writeObject(repo);
        }
        catch(FileNotFoundException except){
            System.err.println("Error file not found! " + except);
        }
        catch(IOException except){
            System.err.println("Other file error! " + except);
        }
    }

    public void add(T obj){
        try{
            super.add(obj);
            writeToFile();
        }
        catch(RepositoryException except) {
            throw new RepositoryException("Error adding a new element: " + except);
        }
    }

    public void delete(T obj){
        try{
            super.delete(obj);
            writeToFile();
        }
        catch(RepositoryException except) {
            throw new RepositoryException("Error deleting an element: " + except);
        }
    }

    public void update(T obj, Tid id){
        try{
            super.update(obj,id);
            writeToFile();
        }
        catch(RepositoryException except) {
            throw new RepositoryException("Error updating the element with the id " + id + ": " + except);
        }
    }
}
