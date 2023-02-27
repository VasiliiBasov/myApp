package org.example;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "person", catalog = "myApp")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private LocalDate birthday;
    private boolean sex;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = LocalDate.parse(birthday);
    }

    public String isSex() {
        if (sex) return "male";
        else return "female";
    }

    public void setSex(String sex) {
        switch (sex) {
            case ("male") -> this.sex = true;
            case ("female") -> this.sex = false;

        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", sex=" + isSex() +
                '}';
    }

    public Person() {
    }
}
