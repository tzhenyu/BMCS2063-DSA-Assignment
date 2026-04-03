/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Control;

import Clinic.Entity.Patient;
import ListADT.ListADT;
import java.util.Comparator;

/**
 *
 * @author chien
 */

// use insert(), remove(), search(), display(), sort(), size()
public class PatientController {
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
}
