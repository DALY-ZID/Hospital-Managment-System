package com.example.miniprojet.home;

import java.util.Date;

public class Consultation {

    private int id;
    private int cinPat;
    private Date date;

    public Consultation(int id, int cinPat, Date date) {
        this.id = id;
        this.cinPat = cinPat;
        this.date = date;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getcinPat() {
        return cinPat;
    }

    public void setcinPat(int cinPat) {
        this.cinPat = cinPat;
    }
    public Date getDate() {
        return date;
    }

    public void setcinPat(Date date) {
        this.date = date;
    }

}
