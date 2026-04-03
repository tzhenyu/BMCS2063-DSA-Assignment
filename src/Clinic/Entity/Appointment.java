/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Entity;

/**
 *
 * @author ASUS
 */

public class Appointment implements Comparable<Appointment> {

    private String patientId;
    private String doctorId;
    private String date; // format: YYYY-MM-DD
    private String time; // format: 10-11

    public Appointment(String patientId, String doctorId,
                       String date, String time) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
    }

    // ==========================
    // GETTERS
    // ==========================
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDate() { return date; }
    public String getTime() { return time; }

    // ==========================
    // COMPARABLE (IMPORTANT)
    // ==========================
    @Override
    public int compareTo(Appointment other) {

        // 1. Compare date first
        int dateCompare = this.date.compareTo(other.date);
        if (dateCompare != 0) {
            return dateCompare;
        }

        // 2. Compare time (start hour only)
        int thisTime = extractStartHour(this.time);
        int otherTime = extractStartHour(other.time);

        return Integer.compare(thisTime, otherTime);
    }

    // helper method
    private int extractStartHour(String time) {
        // "10-11" → 10
        return Integer.parseInt(time.split("-")[0]);
    }

    // ==========================
    // toString
    // ==========================
    @Override
    public String toString() {
        return patientId + " -> " + doctorId + " (" + date + " " + time + ")";
    }

    // ==========================
    // equals (VERY IMPORTANT)
    // ==========================
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Appointment)) return false;

        Appointment other = (Appointment) obj;

        return this.patientId.equals(other.patientId)
                && this.date.equals(other.date)
                && this.time.equals(other.time);
    }
}