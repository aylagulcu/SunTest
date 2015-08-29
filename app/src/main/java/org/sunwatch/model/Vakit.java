package org.sunwatch.model;

import org.sunwatch.view.DayViewLegacy;

import java.util.Date;
import org.sunwatch.model.Enums;



class Reader {
    String[] dayA = { "Bugun", "21/03/2015", "21/06/2015", "22/09/2015", "21/12/2015" };
    Enums.Location[] locA = Enums.Location.values();
    Enums.Method[] metA = Enums.Method.values();
    public Reader(String fn) {}
    public String[] days() { return dayA; }
    public Enums.Location[] locations() { return locA; }
    public Enums.Method[] methods() { return metA; }
}

//verilen bir gun ve yerdeki namaz vakitlerini secilen metotla hesaplar
public class Vakit {
    
    String date = "17/12/2015";
    Enums.Location loc = Enums.Location.Istanbul;
    Enums.Method meth = Enums.Method.Normal;
     
    // g�n ortas� ile namaz vakitleri aras�ndaki dakika fark� 
    float[] time = { -517, -415, 0, 207, 415, 511 };
    public int day = 5706;
    public int twelve = -68;  // g�n ortas� ile ��le 12 aras�ndaki dakika fark�

    
    public Vakit() { }
    public void setMethod(Enums.Method m) {
        meth = m; setDate(day, false);
    }
    public void setLocation(Enums.Location a) {
        loc = a; setDate(day, false);
    }
    public void setTime(long t) { }
    public void setDate(Date d) { }
    public void setDate(double d, boolean report) {

    }
    public int sunset()   { return Math.round(time[4]); }
    public int v_imsak()  { return Math.round(time[0]); }
    public int v_gunes()  { return Math.round(time[1]); }
    public int v_ogle()   { return Math.round(time[2]); }
    public int v_ikindi() { return Math.round(time[3]); }
    public int v_aksam()  { return Math.round(time[4]); }
    public int v_yatsi()  { return Math.round(time[5]); }
    public void report() {
        System.out.print(date); 
        System.out.println(); 
    }
    public String ddMMyyyy() { return date; }
    String HHmm() { return HHmmss().substring(0, 5); }
    String HHmmss() { return new Date().toString().split(" ")[3]; }
    public String toString() { return date +" "+ loc; }

    public int getTwelve(){
        return twelve;
    }
}
