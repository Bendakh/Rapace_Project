package com.example.sbendakhlia.rapace;


import android.app.AlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

public class User {
    private boolean admin;
    private AlertModes alertMode;
    private String email;
    private boolean firstConnect;
    private String id;
    private String lastChangedPasswordDate;
    private int nDays;
    private String name;
    private String password;


    public enum AlertModes
    {
        ALARM,
        VIBRATION,
        FLASH,
        VISUALISATION
    }



    public AlertModes getAlertMode() {
        return alertMode;
    }

    public void setAlertMode(AlertModes alertMode) {
        this.alertMode = alertMode;
    }

    public User() {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = formater.format(todayDate);
        this.lastChangedPasswordDate = thisDate;

        this.nDays = 30;
        this.alertMode = AlertModes.ALARM;
        this.firstConnect = false;
    }

    public User(String name, String email, String password, boolean admin, String id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.admin = admin;
        this.id = id;

        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate = new Date();
        String thisDate = formater.format(todayDate);
        this.lastChangedPasswordDate = thisDate;

        this.nDays = 30;
        this.alertMode = AlertModes.ALARM;
        this.firstConnect = false;
    }

    public int getnDays() {
        return nDays;
    }

    public void setnDays(int nDays) {
        this.nDays = nDays;
    }

    public String getLastChangedPasswordDate() {
        return lastChangedPasswordDate;
    }

    public void setLastChangedPasswordDate(String lastChangedPasswordDate) {
        this.lastChangedPasswordDate = lastChangedPasswordDate;
    }

    public boolean isFirstConnect() {
        return firstConnect;
    }

    public void setFirstConnect(boolean firstConnect) {
        this.firstConnect = firstConnect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }


    public static long GetNumberOfDaysSinceLastPasswordChange(String lastDate) throws ParseException {
        Date tempDate = new SimpleDateFormat("dd/MM/yyyy").parse(lastDate);
        Date todayDate = new Date();
        long diff = (todayDate.getTime() - tempDate.getTime()) / 86400000;
        return Math.abs(diff);
    }
}
