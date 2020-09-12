package com.ort.ortfirebasetutorial.Entities;

public class Notas {
    String name, subject, mark;

    public Notas(String name, String subject, String mark) {

        this.name = name;
        this.subject = subject;
        this.mark = mark;
    }


    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getMark() {
        return mark;
    }

    @Override
    public String toString() {
        return "Notas{" +
                "name='" + name + '\'' +
                ", subject='" + subject + '\'' +
                ", mark='" + mark + '\'' +
                '}';
    }
}
