/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Entity;

/**
 *
 * @author ASUS
 */
public class Availability implements Comparable<Availability> {
    private String doctor;
    private String date;
    private String time;
    private boolean booked;

    public Availability(String doctor, String date, String time) {
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.booked = false;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isBooked() {
        return booked;
    }

    public void setBooked(boolean booked) {
        this.booked = booked;
    }

    

    // Implement for sorting in your ListADT
    @Override
    public int compareTo(Availability other) {
        int dateCompare = this.date.compareTo(other.date);
        if (dateCompare != 0) {
            return dateCompare;
        }
        return this.time.compareTo(other.time);
    }

    @Override
    public String toString() {
        return String.format("%-12s | %s %s %s", doctor, date, time, booked ? "BOOKED" : "AVAILABLE");
    }
}
