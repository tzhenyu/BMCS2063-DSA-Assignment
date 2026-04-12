/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Entity;

import StorageHandler.CSVHandler;
/**
 *
 * @author chien
 */


    public class Patient implements Comparable<Patient> , CSVHandler<Patient> {
    
    private String patientId;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String nationality;
    private String idType;     //use for diff IC or passport
    private String idNumber;
    private String patientType;
    private String allergyInfo;
    private String medicalHistory;

    public Patient(){
    }
    
    public Patient(String patientId, String name, int age, String gender, String contactNumber, String nationality,
            String idType, String idNumber, String patientType, String allergyInfo, String medicalHistory){
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.nationality = nationality;
        this.idType = idType;
        this.idNumber = idNumber;
        this.patientType = patientType;
        this.allergyInfo = allergyInfo;
        this.medicalHistory = medicalHistory;  
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getAllergyInfo() {
        return allergyInfo;
    }

    public void setAllergyInfo(String allergyInfo) {
        this.allergyInfo = allergyInfo;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    @Override
    public int compareTo(Patient other) {
        return this.patientId.compareToIgnoreCase(other.patientId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        
        Patient other = (Patient) obj;
        return this.patientId != null && this.patientId.equalsIgnoreCase(other.patientId);
    }

   @Override
    public String toString() {
        return String.format(
                "| %-6s | %-20s | %-3d | %-6s | %-12s | %-12s | %-10s | %-15s | %-10s | %-20s | %-20s |\n",
                patientId,name,age,gender,contactNumber,nationality,idType,idNumber,patientType,allergyInfo,medicalHistory
        );
    }
    
    @Override  // use to store the object internally? just for completeness not need for our ADT
    public int hashCode() {
       return patientId == null ? 0 : patientId.toLowerCase().hashCode();
    } //same patientId -> same hashCode, consistent with equals()
   
    @Override
    public String toCSV() {
    return patientId + ";" + name + ";" + age + ";" + gender + ";" +
           contactNumber + ";" + nationality + ";" + idType + ";" +
           idNumber + ";" + patientType + ";" + allergyInfo + ";" + medicalHistory;
}

    @Override
    public Patient fromCSV(String[] fields) {
    String patientId = fields[0].trim();
    String name = fields[1].trim();
    
   int age = 0;
   try {
        age = Integer.parseInt(fields[2].trim());
        } catch (NumberFormatException e) {
        age = 0;
    }
    
    String gender = fields[3].trim();
    String contactNumber = fields[4].trim();
    String nationality = fields[5].trim();
    String idType = fields[6].trim();
    String idNumber = fields[7].trim();
    String patientType = fields[8].trim();
    String allergyInfo = fields[9].trim();
    String medicalHistory = fields[10].trim();

    return new Patient(patientId, name, age, gender, contactNumber,
            nationality, idType, idNumber, patientType, allergyInfo, medicalHistory);
}
           
    
}
