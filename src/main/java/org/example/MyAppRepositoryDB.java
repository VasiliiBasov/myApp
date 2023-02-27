package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.Date;

public class MyAppRepositoryDB {

    private final SessionFactory sessionFactory;

    public MyAppRepositoryDB() {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.put(Environment.URL, "jdbc:mysql://localhost:3306/myApp");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "root");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        properties.put(Environment.HBM2DDL_AUTO, "update");

        sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(org.example.Person.class)
                .buildSessionFactory();
    }

    public MyAppRepositoryDB(String hbm2ddl_auto) {
        Properties properties = new Properties();
        properties.put(Environment.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.put(Environment.URL, "jdbc:p6spy:mysql://localhost:3306/ptmk");
        properties.put(Environment.USER, "root");
        properties.put(Environment.PASS, "root");
        properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        properties.put(Environment.HBM2DDL_AUTO, hbm2ddl_auto);

        sessionFactory = new Configuration()
                .setProperties(properties)
                .addAnnotatedClass(org.example.Person.class)
                .buildSessionFactory();
    }

    public void createPerson(Person person) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(person);
            transaction.commit();

        }
    }

    public void selectAll(){
        try (Session session = sessionFactory.openSession()) {

            List<Object[]> list = session.createNativeQuery("select distinct name, birthday, sex, birthday from person").list();
            list.forEach(s -> {
                String sex;
                if (s[2].equals(true)) sex = "male";
                else sex = "female";
                Date currentDate = new Date();
                Date date = (Date) s[3];
                System.out.print(s[0] + " " + s[1] + " " + sex + " " + (currentDate.getTime() - date.getTime())/(360L * 24 * 60 * 60 * 1000));
                System.out.println();
            });
        }
    }

    public void create1MilStr() {
        try (Session session = sessionFactory.openSession()) {
            int x = 0;
            for (int j = 0; j < 100; j++) {
                Transaction transaction = session.beginTransaction();
                for (int i = 0; i < 10000; i++) {
                    Person person = new Person();
                    randomPers(person);
                    session.persist(person);
                }
                transaction.commit();
                System.out.println(++x + " x10000 persons was inserted!");
            }

        }

    }

    public void createFNames() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            for (int i = 0; i < 100; i++) {
                Person person = new Person();
                randomPers(person, 'F');
                session.save(person);
            }
            transaction.commit();
            System.out.println("FName was created!");
        }
    }

    private void randomPers(Person person) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder stringBuilder = new StringBuilder(alphabet);
        StringBuilder stringBuilder1 = new StringBuilder();
        for (int j = 0; j<3; j++) {
            for (int i = 0; i < 6; i++) {
                stringBuilder1.append(stringBuilder.charAt((int)(Math.random()*26)));
            }
            stringBuilder1.append(" ");
        }
        stringBuilder1.deleteCharAt(stringBuilder1.length()-1);
        person.setName(String.valueOf(stringBuilder1));

        long leftLimit = 0L; // 01/01/1970, 00:00
        long rightLimit = Instant.now().toEpochMilli(); // now
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        Date randomDate = Date.from(Instant.ofEpochMilli(generatedLong));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        person.setBirthday(dateFormat.format(randomDate));

        if (Math.random()<0.5) person.setSex("male");
        else person.setSex("female");
    }

    private void randomPers(Person person, Character first) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder stringBuilder = new StringBuilder(alphabet);
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(first);
        for (int j = 0; j<3; j++) {
            for (int i = 0; i < 6; i++) {
                stringBuilder1.append(stringBuilder.charAt((int)(Math.random()*26)));
            }
            stringBuilder1.append(" ");
        }
        stringBuilder1.deleteCharAt(stringBuilder1.length()-1);
        person.setName(String.valueOf(stringBuilder1));

        long leftLimit = 0L; // 01/01/1970, 00:00
        long rightLimit = Instant.now().toEpochMilli(); // now
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        Date randomDate = Date.from(Instant.ofEpochMilli(generatedLong));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        person.setBirthday(dateFormat.format(randomDate));

        person.setSex("male");

    }

    public void getFNames() {
        try (Session session = sessionFactory.openSession()) {
            session.createQuery("from Person where name like 'F%' and sex = true").list().forEach(System.out::println);
        }
    }

    public void getQuickFNames() throws SQLException {

        Connection connection  = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/myapp",
                "root", "root");

        Statement statement = connection.createStatement();
        ResultSet results = statement.executeQuery("select name, birthday, sex\n" +
                "from myapp.person\n" +
                "where name like 'F%'\n" +
                "  and sex = true");
        while (results.next()) {

            String name = results.getString(1);
            String date = results.getString(2);
            String sex;
            if (results.getString(3).equals("1")) sex = "male";
            else sex = "female";
            System.out.println(results.getRow() + " " + name + "\t"+ date + "\t"+ sex);
        }
    }
}
