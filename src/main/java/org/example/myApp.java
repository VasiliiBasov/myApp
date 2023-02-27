package org.example;


import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;

public class myApp {
    public static void main(String[] args) throws SQLException {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        switch (Integer.parseInt(args[0])) {
            case (1) -> {
                createTable();
            }
            case (2) -> {
                createPerson(args[1], args[2], args[3]);
            }
            case (3) -> selectAllDistinct();
            case (4) -> createOneMillion();
            case (5) -> getFNames();
            case (6) -> getQuickFNames();
            default -> throw new IllegalStateException("Unexpected value: " + args[0]);
        }
    }
    public static void createPerson(String name, String birthday, String sex) {
        MyAppRepositoryDB repositoryDB = new MyAppRepositoryDB();
        var person = new Person();
        person.setName(name);
        person.setBirthday(birthday);
        person.setSex(sex);
        repositoryDB.createPerson(person);
        System.out.println(person.toString() + " was created!");
    }
    public static void createTable() {
        MyAppRepositoryDB repositoryDB = new MyAppRepositoryDB();
        System.out.println("Table persons was created!");
    }

    public static void selectAllDistinct() {
        MyAppRepositoryDB repositoryDB = new MyAppRepositoryDB();
        repositoryDB.selectAll();
    }

    public static void createOneMillion() {
        MyAppRepositoryDB repositoryDB = new MyAppRepositoryDB();
        repositoryDB.create1MilStr();
        repositoryDB.createFNames();
    }

    public static void getFNames() {
        Instant start = Instant.now();
        MyAppRepositoryDB repositoryDB = new MyAppRepositoryDB();
        repositoryDB.getFNames();
        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        System.out.println("Прошло времени, мс: " + elapsed);
    }

    public static void getQuickFNames() throws SQLException {
        Instant start = Instant.now();
        MyAppRepositoryDB repositoryDB = new MyAppRepositoryDB();
        repositoryDB.getQuickFNames();
        Instant finish = Instant.now();
        long elapsed = Duration.between(start, finish).toMillis();
        System.out.println("Прошло времени, мс: " + elapsed);
    }
}