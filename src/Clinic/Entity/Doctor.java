/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Entity;

/**
 *
 * @author ASUS
 */
import ListADT.ListADT;
import ListADT.ListADTInterface;

public class Doctor implements Comparable<Doctor> {
    private String doctorId;
    private String name;
    private String specialization;
    private String phone;
    private String email;
    private String availability; // e.g. "Mon-Fri 10AM-5PM"
    private boolean isAvailable;

    // Constructor
    public Doctor(String doctorId, String name, String specialization,
                  String phone, String email, String availability, boolean isAvailable) {
        this.doctorId = doctorId;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
        this.availability = availability;
        this.isAvailable = isAvailable;
    }

    // Getters

    public String getDoctorId() {
        return doctorId;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAvailability() {
        return availability;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }
    
    
    
    // ETTERS
    public void setName(String name) {
        this.name = name;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    // Setters
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    // Sorting (by doctorId)
    @Override
    public int compareTo(Doctor other) {
        return this.doctorId.compareTo(other.doctorId);
    }

    // Equality (based on doctorId)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Doctor)) return false;

        Doctor other = (Doctor) obj;
        return this.doctorId.equals(other.doctorId);
    }

    // Display
    @Override
    public String toString() {
        return "Doctor ID: " + doctorId +
               ", Name: " + name +
               ", Specialization: " + specialization +
               ", Phone: " + phone +
               ", Email: " + email +
               ", Availability: " + availability +
               ", Status: " + (isAvailable ? "Available" : "Not Available");
    }
}