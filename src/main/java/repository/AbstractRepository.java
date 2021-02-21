package repository;

import domain.Identifiable;
import exceptions.RepositoryException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AbstractRepository <T extends Identifiable<Tid>,Tid> implements Repository<T,Tid>{

    protected Map<Tid, T> repo;

    public AbstractRepository(){
        repo = new HashMap<>();
    }

    public void add(T obj){
        for(T elem: repo.values()){
            if(elem.equals(obj))
                throw new RepositoryException("Element already exists!");
        }
        if(repo.containsKey(obj.getID()))
            throw new RepositoryException("Element already exists!");
        else
            repo.put(obj.getID(),obj);

    }

    public void delete(T obj){
        if(repo.containsKey(obj.getID()))
            repo.remove(obj.getID());
        else
            throw new RepositoryException("Element was not found!");
    }

    public void update(T obj,Tid id){
        if(repo.containsKey(id))
            repo.put(id,obj);
        else
            throw new RepositoryException("Element was not found!");
    }

    public T findById(Tid id){
        if(repo.containsKey(id))
            return (T) repo.get(id);
        else
            throw new RepositoryException("Element was not found!");
    }

    public Iterable<T> getAll(){
        return repo.values();
    }

    public Collection<T> getCollection(){return repo.values();}

    public String toString() {
        StringBuffer result = new StringBuffer();
        for (T elem : repo.values()) {
            result.append(elem);
            result.append("\n\n");
        }
        return result.toString();
    }
}
