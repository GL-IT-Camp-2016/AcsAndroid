package com.gl.accesscontrolsystem.pojo;

import java.util.ArrayList;

public class PersonList {

    Result result;
    boolean success;

    public ArrayList<Person> getPersons(){
        return result.getpersons();
    }

    public ArrayList<String> getPersonNames()
    {
        ArrayList<String> names = new ArrayList<>();
        for (Person p:getPersons()) {
            names.add(p.name);
        }
        return names;
    }

}

class Result {
    ArrayList<Person> persons;

    public ArrayList<Person> getpersons(){
        return persons;
    }
}

class Person {
    String name;
}
