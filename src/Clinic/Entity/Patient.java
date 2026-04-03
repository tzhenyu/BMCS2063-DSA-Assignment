/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Entity;

/**
 *
 * @author ASUS
 */
public class Patient implements Comparable<Patient>{
    
    private String patientID;
    private String name;
    private String ic;
    private String phone;
    private String age;
    private String gmail;

    public Patient(String patientID, String name, String ic, String phone, String age, String gmail) {
        this.patientID = patientID;
        this.name = name;
        this.ic = ic;
        this.phone = phone;
        this.age = age;
        this.gmail = gmail;
    }

    public String getPatientID() { 
        return patientID; 
    }
    public String getName() { 
        return name; 
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    
    @Override
    public String toString() {
        return "Patient{" + "patientID=" + patientID + ", name=" + name + "ic "+ ic + ", phone=" + phone + ", age=" + age + ", gmail=" + gmail + '}';
    }

    @Override
    public int compareTo(Patient o) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}

