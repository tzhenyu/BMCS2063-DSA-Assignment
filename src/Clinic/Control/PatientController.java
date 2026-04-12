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
        if(patient !=null){
        patientList.insert(patient);
    }
    }
    
    public boolean deletePatientById(String patientId) {
    if (patientId == null || patientId.trim().isEmpty()) {
        return false;
    }

    Patient dummyPatient = new Patient();
    dummyPatient.setPatientId(patientId.trim());

    return patientList.remove(dummyPatient);
     }
    
    public Patient searchPatientById(String patientId) {
        if (patientId == null || patientId.trim().isEmpty()) {
            return null;
        }

        Patient dummyPatient = new Patient();
        dummyPatient.setPatientId(patientId.trim());
        return patientList.search(dummyPatient);
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
        if (patientId == null || updatedPatient == null) {
            return false;
        }

        Patient existingPatient = searchPatientById(patientId);
        if (existingPatient == null) {
            return false;
        }

        existingPatient.setName(updatedPatient.getName());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setGender(updatedPatient.getGender());
        existingPatient.setContactNumber(updatedPatient.getContactNumber());
        existingPatient.setNationality(updatedPatient.getNationality());
        existingPatient.setIdType(updatedPatient.getIdType());
        existingPatient.setIdNumber(updatedPatient.getIdNumber());
        existingPatient.setPatientType(updatedPatient.getPatientType());
        existingPatient.setAllergyInfo(updatedPatient.getAllergyInfo());
        existingPatient.setMedicalHistory(updatedPatient.getMedicalHistory());

        return true;
    }

    // MALAYSIA = insurance, non malaysian = oversea insurance
    public boolean isEligibleForLocalInsurance(Patient patient) {
        return patient != null &&
               patient.getNationality() != null &&
               patient.getNationality().equalsIgnoreCase("Malaysian");
    }
    
    //CHECK for REPETITION for AppointmentBoundary
    public boolean isPatientExists(String patientId) {
    Patient dummy = new Patient();
    dummy.setPatientId(patientId);
    return patientList.search(dummy) != null;
    }
    
   public String generateNextPatientId() {
        int max = 0;

        for (Patient patient : patientList) {
            String patientId = patient.getPatientId();

            if (patientId != null && patientId.matches("P\\d{4}")) {
                int number = Integer.parseInt(patientId.substring(1));
                if (number > max) {
                    max = number;
                }
            }
        }

        return String.format("P%04d", max + 1);
    }
    
    public boolean nameExists(String name) {
    for (Patient patient : patientList) {
        if (patient.getName() != null && patient.getName().equalsIgnoreCase(name)) {
            return true;
        }
    }
    return false;
    }
    
    //To check whether a patient with the same IC / passport number already exists in the system
    public boolean idNumberExists(String idNumber) {
        if (idNumber == null) {
            return false;
        }

        for (Patient patient : patientList) {
            if (patient.getIdNumber() != null &&
                patient.getIdNumber().equalsIgnoreCase(idNumber)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean contactNumberExists(String contactNumber) {
    for (Patient patient : patientList) {
        if (patient.getContactNumber() != null &&
            patient.getContactNumber().equals(contactNumber)) {
            return true;
        }
    }
    return false;
    }
    
    //For update Patient's details
    public boolean idNumberExistsExcept(String idNumber, String patientId) {
        if (idNumber == null || patientId == null) {
            return false;
        }

        for (Patient patient : patientList) {
            if (!patient.getPatientId().equalsIgnoreCase(patientId) &&
                patient.getIdNumber() != null &&
                patient.getIdNumber().equalsIgnoreCase(idNumber)) {
                return true;
            }
        }
        return false;
    }
    
    //for update patient's details
    public boolean contactNumberExistsExcept(String contactNumber, String patientId) {
        if (contactNumber == null || patientId == null) {
            return false;
        }

        for (Patient patient : patientList) {
            if (!patient.getPatientId().equalsIgnoreCase(patientId) &&
                patient.getContactNumber() != null &&
                patient.getContactNumber().equals(contactNumber)) {
                return true;
            }
        }
        return false;
    }
    
    //Export Medical Report
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
    patientList.clear();
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
