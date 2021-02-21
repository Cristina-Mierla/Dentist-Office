package undo;

import java.util.ArrayList;
import java.util.List;

public class Stack {

    private List<Element> stack;

    public Stack(){stack = new ArrayList<>(); }

    public List<Element> getStack(){
        return stack;
    }

    public void push (Element obj){
        stack.add(obj);
    }

    public Element pop (){
        Element obj = stack.get(stack.size()-1);
        stack.remove(obj);
        return obj;
    }

    public Element getLast(){
        Element obj = stack.get(stack.size()-1);
        return obj;
    }
}
