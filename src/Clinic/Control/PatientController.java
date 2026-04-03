/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Control;

import Clinic.Entity.Patient;
import ListADT.ListADT;
import java.util.Comparator;
import StorageHandler.FileHandler;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author chien
 */

// use insert(), remove(), search(), display(), sort(), size()
public class PatientController implements FileHandler{
    private static final String FILENAME = "src/patient.csv";
    private ListADT<Patient> patientList;
    
    public PatientController(){
        patientList = new ListADT<>();
    }
    
    public void addPatient(Patient patient){
        patientList.insert(patient);
    }
    
    public boolean deletePatient(Patient patient){
        return patientList.remove(patient); 
    }
    
    public Patient searchPatient(Patient patient){
        return patientList.search(patient);
    }
    
    public String displayPatients(){
        return patientList.display();
    }
    
    public int getTotalPatients(){
        return patientList.size();
    }
    
    public void sortPatients(){
        patientList.sort();
    }
    
    public void sortPatients(Comparator<Patient> comparator){
        patientList.sort(comparator);
    }

    public boolean updatePatient(String patientId, Patient updatedPatient) {
        for (Patient patient : patientList) {
            if (patient.getPatientId().equalsIgnoreCase(patientId)) {
                patient.setName(updatedPatient.getName());
                patient.setAge(updatedPatient.getAge());
                patient.setGender(updatedPatient.getGender());
                patient.setContactNumber(updatedPatient.getContactNumber());
                patient.setNationality(updatedPatient.getNationality());
                patient.setIdType(updatedPatient.getIdType());
                patient.setIdNumber(updatedPatient.getIdNumber());
                patient.setPatientType(updatedPatient.getPatientType());
                patient.setAllergyInfo(updatedPatient.getAllergyInfo());
                patient.setMedicalHistory(updatedPatient.getMedicalHistory());
                return true;
            }
        }
        return false;
    }
    
    public boolean isInsuranceEligible(Patient patient){
        return patient.getNationality().equalsIgnoreCase("Malaysian");
    }
    
    public void exportMedicalReport(Patient patient) {
    if (patient == null) {
        System.out.println("Patient not found.");
        return;
    }

    String filename = "src/report_" + patient.getPatientId() + ".txt";
    File reportFile = new File(filename);

    try (PrintWriter writer = new PrintWriter(reportFile)) {
        writer.println("========== MEDICAL REPORT ==========");
        writer.println("Patient ID      : " + patient.getPatientId());
        writer.println("Name            : " + patient.getName());
        writer.println("Age             : " + patient.getAge());
        writer.println("Gender          : " + patient.getGender());
        writer.println("Contact Number  : " + patient.getContactNumber());
        writer.println("Nationality     : " + patient.getNationality());
        writer.println("ID Type         : " + patient.getIdType());
        writer.println("ID Number       : " + patient.getIdNumber());
        writer.println("Patient Type    : " + patient.getPatientType());
        writer.println("Allergy Info    : " + patient.getAllergyInfo());
        writer.println("Medical History : " + patient.getMedicalHistory());
        writer.println("===================================");

        System.out.println("Medical report saved successfully to: " + filename);
    } catch (IOException e) {
        System.out.println("Error saving medical report: " + e.getMessage());
    }
}
    
    @Override
public void loadFile() {
    System.out.println("Loading from file: " + FILENAME);
    File csvFile = new File(FILENAME);

    Patient p = new Patient();

    try (Scanner scanner = new Scanner(csvFile)) {
        if (scanner.hasNextLine()) {
            scanner.nextLine(); // skip header
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] fields = line.split(";", -1);

            Patient patient = p.fromCSV(fields);
            if (patient != null) {
                patientList.insert(patient);
            }
        }

        System.out.println("File loaded.");
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
}

@Override
public void saveFile() {
    System.out.println("Saving file to: " + FILENAME);
    File csvFile = new File(FILENAME);

    try (PrintWriter writer = new PrintWriter(csvFile)) {
        writer.println("PATIENT ID;NAME;AGE;GENDER;CONTACT NUMBER;NATIONALITY;ID TYPE;ID NUMBER;PATIENT TYPE;ALLERGY INFO;MEDICAL HISTORY");

        for (Patient patient : patientList) {
            writer.println(patient.toCSV());
        }

        writer.close();
        System.out.println("File saved.");
    } catch (IOException e) {
        System.out.println(e);
    }
}

}
