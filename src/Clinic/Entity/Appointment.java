/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Entity;

import StorageHandler.CSVHandler;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author ASUS
 */

public class Appointment implements Comparable<Appointment>, CSVHandler<Appointment> {
    private String appointmentId;
    private String patientId;
    private LocalDate date;
    private String time;
    private String purpose; 
    private String remarks;

    public Appointment() {}

    
    public Appointment(String appointmentId, String patientId, LocalDate date, String time, String purpose, String remarks) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.date = date;
        this.time = time;
        this.purpose = purpose;
        this.remarks = remarks;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        return appointmentId + ";" + patientId + ";" + date.format(formatter) + ";" + time +  ";" + purpose + ";" + remarks;
    }

    public Appointment fromCSV(String[] fields) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        String appointmentId = fields[0].trim();
        String patientId = fields[1].trim();
        LocalDate date = LocalDate.parse(fields[2].trim(), formatter);
        String time = fields[3].trim();
        String purpose = fields[4].trim();
        String remarks = fields[5].trim();
        
        return new Appointment(appointmentId, patientId, date, time, purpose, remarks);
    }

    @Override
    public int compareTo(Appointment other) {
        return this.appointmentId.compareToIgnoreCase(other.appointmentId);
    }

    @Override
    public String toString() {
        return String.format("| %-8s | %-8s | %-10s | %-5s | %-10s | %-15s |\n",appointmentId, patientId, date, time,purpose, remarks);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Appointment other = (Appointment) obj;

        return this.appointmentId != null && this.appointmentId.equals(other.appointmentId);
    }
    
    
    
    
   
}