package com.gl.accesscontrolsystem.pojo;

import java.util.ArrayList;

public class PersonList {

    Result result;
    boolean success;
}

class Result {
    ArrayList<Person> persons;
}

class Person {
    String name;
}
