/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Boundary;

import Clinic.Control.PatientController;
import Clinic.Entity.Patient;
import java.util.Comparator;
import java.util.Scanner;
/**
 *
 * @author chien
 */
public class PatientBoundary {
    private PatientController controller;
    private Scanner scanner;
    
    public PatientBoundary(){
        controller = new PatientController();
        scanner = new Scanner(System.in);
    }
    
    public void loadFile(){
        controller.loadFile();
    }
    
    public void saveFile(){
        controller.saveFile();
    }
    
    public void start(){
        loadFile();
        menu();
    }
    
    public void menu(){
        int choice;
        
        do{
            System.out.println("\n=== Patient Management ===");
            System.out.println("1. Add Patient");
            System.out.println("2. Search Patient");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Display All Patients");
            System.out.println("6. Sort Patients by ID");
            System.out.println("7. Check Insurance Eligibility");
            System.out.println("8. Export Medical Report");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1 -> addPatient();
                case 2 -> searchPatient();
                case 3 -> updatePatient();
                case 4 -> deletePatient();
                case 5 -> displayPatients();
                case 6 -> sortPatients();
                case 7 -> checkInsurance();
                case 8 -> exportMedicalReport();
                case 0 -> exit();
                default -> System.out.println("Invalid choice.");
            }
        } while (choice !=0);
    } // close for the menu
    
    private void addPatient(){
        Patient patient = readPatientDetails();
        controller.addPatient(patient);
        controller.saveFile();
        System.out.println("Patient added successfully.");
    }
    
    private void searchPatient(){
        System.out.print("Enter Patient ID to search: ");
        String patientId = scanner.nextLine();
        
        Patient dummy = new Patient();
        dummy.setPatientId(patientId);
        
        Patient found = controller.searchPatient(dummy);
        
        if(found != null){
            printHeader();
            System.out.print(found);
        }else{
            System.out.println("Patient not found.");
        }
    }
    
    private void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        String patientId = scanner.nextLine();

        Patient dummy = new Patient();
        dummy.setPatientId(patientId);

        boolean deleted = controller.deletePatient(dummy);

        if (deleted) {
            controller.saveFile();
            System.out.println("Patient deleted successfully.");
        } else {
            System.out.println("Patient not found.");
        }
    }
    
    private void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        String patientId = scanner.nextLine();

        Patient updatedPatient = readPatientDetails();
        updatedPatient.setPatientId(patientId);

        boolean updated = controller.updatePatient(patientId, updatedPatient);

        if (updated) {
            System.out.println("Patient information updated successfully.");
        } else {
            System.out.println("Patient not found.");
        }
    }
    private void displayPatients() {
        printHeader();
        System.out.println(controller.displayPatients());
        System.out.println("Total Patients: " + controller.getTotalPatients());
    }

    private void sortPatients() {
        controller.sortPatients(Comparator.comparing(Patient::getPatientId));
        System.out.println("Patients sorted by ID.");
    }

    private void checkInsurance() {
        System.out.print("Enter Patient ID to check insurance: ");
        String patientId = scanner.nextLine();

        Patient dummy = new Patient();
        dummy.setPatientId(patientId);

        Patient found = controller.searchPatient(dummy);

        if (found != null) {
            if (controller.isInsuranceEligible(found)) {
                System.out.println(found.getName() + " is eligible for local insurance checking.");
            } else {
                System.out.println(found.getName() + " is a non-Malaysian patient. Passport/international insurance applies.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    private void exportMedicalReport() {
    System.out.print("Enter Patient ID: ");
    String patientId = scanner.nextLine();

    Patient dummy = new Patient();
    dummy.setPatientId(patientId);

    Patient found = controller.searchPatient(dummy);

    if (found != null) {
        controller.exportMedicalReport(found);
    } else {
        System.out.println("Patient not found.");
    }
}
    
    private Patient readPatientDetails() {
        Patient patient = new Patient();

        System.out.print("Patient ID: ");
        patient.setPatientId(scanner.nextLine());

        System.out.print("Name: ");
        patient.setName(scanner.nextLine());

        System.out.print("Age: ");
        patient.setAge(Integer.parseInt(scanner.nextLine()));

        System.out.print("Gender: ");
        patient.setGender(scanner.nextLine());

        System.out.print("Contact Number: ");
        patient.setContactNumber(scanner.nextLine());

        System.out.print("Nationality (Malaysian / Non-Malaysian): ");
        patient.setNationality(scanner.nextLine());

        System.out.print("ID Type (IC / Passport): ");
        patient.setIdType(scanner.nextLine());

        System.out.print("ID Number: ");
        patient.setIdNumber(scanner.nextLine());

        System.out.print("Patient Type (Emergency / Normal): ");
        patient.setPatientType(scanner.nextLine());

        System.out.print("Allergy Info: ");
        patient.setAllergyInfo(scanner.nextLine());

        System.out.print("Medical History: ");
        patient.setMedicalHistory(scanner.nextLine());

        return patient;
    }

    private void printHeader() {
        System.out.printf("| %-8s | %-20s | %-3s | %-8s | %-13s | %-12s | %-10s | %-15s | %-12s | %-15s | %-20s |%n",
                "ID", "NAME", "AGE", "GENDER", "CONTACT", "NATIONALITY",
                "ID TYPE", "ID NUMBER", "TYPE", "ALLERGY", "HISTORY");
    }
    
    private void pause(){
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    public static void clearConsole(){
        for(int i = 0; i< 50; i++){
            System.out.println();
        }
    }
    
    private void exit(){
        System.out.println("Exiting...");
        saveFile();
    }
}
