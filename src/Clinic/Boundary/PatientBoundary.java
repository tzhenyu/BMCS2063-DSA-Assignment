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
            System.out.println("6. Check Insurance Eligibility");
            System.out.println("7. Export Medical Report");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(scanner.nextLine());
            
            switch (choice) {
                case 1 -> addPatient();
                case 2 -> searchPatient();
                case 3 -> updatePatient();
                case 4 -> deletePatient();
                case 5 -> displayPatients();
                case 6 -> checkInsurance();
                case 7 -> exportMedicalReport();
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
    
    private void updatePatient() {
    System.out.print("Enter Patient ID to update: ");
    String patientId = scanner.nextLine().trim().toUpperCase();

    Patient dummy = new Patient();
    dummy.setPatientId(patientId);

    Patient found = controller.searchPatient(dummy);

    if (found == null) {
        System.out.println("Patient not found.");
        return;
    }

    int choice;

    do {
        System.out.println("\nCurrent Patient Details:");
        printHeader();
        System.out.print(found);

        System.out.println("\n=== Update Menu ===");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Gender");
        System.out.println("4. Contact Number");
        System.out.println("5. Nationality and ID Number");
        System.out.println("6. Patient Type");
        System.out.println("7. Allergy Info");
        System.out.println("8. Medical History");
        System.out.println("0. Finish Update");
        System.out.print("Enter choice: ");

        choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1 -> {
                String name = inputValidName("New Name: ");
                if (controller.nameExists(name)) {
                    System.out.println("Warning: A patient with the same name already exists.");
                }
                found.setName(name);
            }

            case 2 -> found.setAge(inputValidAge("New Age: "));

            case 3 -> found.setGender(inputValidGender("New Gender (Male/Female): "));

            case 4 -> found.setContactNumber(
                    inputValidContactUpdate("New Contact Number: ", patientId)
            );

            case 5 -> updateNationalityAndId(found, patientId);

            case 6 -> found.setPatientType(
                    inputValidPatientType("New Patient Type (Emergency / Normal): ")
            );

            case 7 -> found.setAllergyInfo(
                    inputNonEmpty("New Allergy Info: ")
            );

            case 8 -> found.setMedicalHistory(
                    inputNonEmpty("New Medical History: ")
            );

            case 0 -> {
                controller.saveFile();
                System.out.println("Patient information updated successfully.");
            }

            default -> System.out.println("Invalid choice.");
        }

    } while (choice != 0);
    }
    
    // Helper function for update purpose
    private void updateNationalityAndId(Patient patient, String patientId) {
    String nationality = inputValidNationality("New Nationality (Malaysian / Non-Malaysian): ");
    patient.setNationality(nationality);

    if (nationality.equalsIgnoreCase("Malaysian")) {
        patient.setIdType("IC");
        System.out.println("ID Type: IC");
        patient.setIdNumber(inputValidICUpdate("New IC Number: ", patientId));
    } else {
        patient.setIdType("Passport");
        System.out.println("ID Type: Passport");
        patient.setIdNumber(inputValidPassportUpdate("New Passport Number: ", patientId));
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
    
    
    private void displayPatients() {
    if (controller.getTotalPatients() == 0) {
        System.out.println("No patient records found.");
        return;
    }

    System.out.println("Display option:");
    System.out.println("1. Current order");
    System.out.println("2. Sort by Patient ID");
    System.out.println("3. Sort by Patient Name");
    System.out.print("Enter choice: ");

    int choice = Integer.parseInt(scanner.nextLine());

    switch (choice) {
        case 1 -> {
            // do nothing, keep current order
        }
        case 2 -> {
            controller.sortPatients(Comparator.comparing(Patient::getPatientId));
            System.out.println("Patients sorted by Patient ID.");
        }
        case 3 -> {
            controller.sortPatients(Comparator.comparing(Patient::getName, String.CASE_INSENSITIVE_ORDER));
            System.out.println("Patients sorted by Patient Name.");
        }
        default -> {
            System.out.println("Invalid choice.");
            return;
        }
    }

    printHeader();
    System.out.println(controller.displayPatients());
    System.out.println("Total Patients: " + controller.getTotalPatients());
}

    private void sortPatients() {
    System.out.println("Sort by:");
    System.out.println("1. Patient ID");
    System.out.println("2. Patient Name");
    System.out.print("Enter choice: ");
    
    int choice = Integer.parseInt(scanner.nextLine());

    switch (choice) {
        case 1 -> {
            controller.sortPatients(Comparator.comparing(Patient::getPatientId));
            System.out.println("Patients sorted by Patient ID.");
        }
        case 2 -> {
            controller.sortPatients(Comparator.comparing(Patient::getName, String.CASE_INSENSITIVE_ORDER));
            System.out.println("Patients sorted by Name.");
        }
        default -> {
            System.out.println("Invalid choice.");
            return;
        }
    }
    System.out.println("Current list order updated.");
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

    String newPatientId = controller.generateNextPatientId();
    System.out.println("Generated Patient ID: " + newPatientId);
    patient.setPatientId(newPatientId);

    String name = inputValidName("Name: ");
    if (controller.nameExists(name)) {
        System.out.println("Warning: A patient with the same name already exists. Please check to avoid duplicate record.");
    }
    patient.setName(name);

    patient.setAge(inputValidAge("Age: "));
    patient.setGender(inputValidGender("Gender (Male/Female): "));
    patient.setContactNumber(inputValidContact("Contact Number: "));

    String nationality = inputValidNationality("Nationality (Malaysian / Non-Malaysian): ");
    patient.setNationality(nationality);

    if (nationality.equalsIgnoreCase("Malaysian")) {
        patient.setIdType("IC");
        System.out.println("ID Type: IC");
        patient.setIdNumber(inputValidIC("IC Number: "));
    } else {
        patient.setIdType("Passport");
        System.out.println("ID Type: Passport");
        patient.setIdNumber(inputValidPassport("Passport Number: "));
    }

    patient.setPatientType(inputValidPatientType("Patient Type (Emergency / Normal): "));
    patient.setAllergyInfo(inputNonEmpty("Allergy Info: "));
    patient.setMedicalHistory(inputNonEmpty("Medical History: "));

    return patient;
}

//UPDATE PATIENT DETAILS
private Patient readUpdatedPatientDetails(String patientId) {
    Patient patient = new Patient();
    patient.setPatientId(patientId); // keep same ID

    String name = inputValidName("Name: ");
    if (controller.nameExists(name)) {
        System.out.println("Warning: A patient with the same name already exists.");
    }
    patient.setName(name);

    patient.setAge(inputValidAge("Age: "));
    patient.setGender(inputValidGender("Gender (Male/Female): "));

    patient.setContactNumber(inputValidContactUpdate("Contact Number: ", patientId));

    String nationality = inputValidNationality("Nationality (Malaysian / Non-Malaysian): ");
    patient.setNationality(nationality);

    if (nationality.equalsIgnoreCase("Malaysian")) {
        patient.setIdType("IC");
        System.out.println("ID Type: IC");
        patient.setIdNumber(inputValidICUpdate("IC Number: ", patientId));
    } else {
        patient.setIdType("Passport");
        System.out.println("ID Type: Passport");
        patient.setIdNumber(inputValidPassportUpdate("Passport Number: ", patientId));
    }

    patient.setPatientType(inputValidPatientType("Patient Type (Emergency / Normal): "));
    patient.setAllergyInfo(inputNonEmpty("Allergy Info: "));
    patient.setMedicalHistory(inputNonEmpty("Medical History: "));

    return patient;
}

    private void printHeader() {
        System.out.printf("| %-8s | %-20s | %-3s | %-8s | %-13s | %-12s | %-10s | %-15s | %-12s | %-15s | %-20s |%n",
                "ID", "NAME", "AGE", "GENDER", "CONTACT", "NATIONALITY",
                "ID TYPE", "ID NUMBER", "TYPE", "ALLERGY", "HISTORY");
    }
    
    //VALIDATION FOR INPUT OF PATIENT DETAILS
    private String inputNonEmpty(String message) {
    String input;
    do {
        System.out.print(message);
        input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            System.out.println("Input cannot be empty.");
        }
    } while (input.isEmpty());
    return input;
    }
    
    private String inputValidName(String message) {
    String name;

    do {
        System.out.print(message);
        name = scanner.nextLine().trim();

        if (!name.matches("^[A-Za-z]+(\\s[A-Za-z]+)+$")) {
            System.out.println("Invalid name. Use letters and at least 2 words.");
        } 
        else if (name.length() < 3) {
            System.out.println("Name too short.");
        }
        else {
            return name;
        }

    } while (true);
    }
    
    private int inputValidAge(String message) {
    int age = -1;
    boolean valid = false;

    do {
        try {
            System.out.print(message);
            age = Integer.parseInt(scanner.nextLine());
            if (age >= 1 && age <= 110) {
                valid = true;
            } else {
                System.out.println("Age must be between 1 and 120.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid age. Please enter a number.");
        }
    } while (!valid);

    return age;
    }
    
    private String inputValidGender(String message) {
    String gender;
    do {
        System.out.print(message);
        gender = scanner.nextLine().trim();
        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
            System.out.println("Invalid gender. Enter Male or Female only.");
        }
    } while (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female"));
    return gender;
    }
    
    private String inputValidContact(String message) {
    String contact;

    do {
        System.out.print(message);
        contact = scanner.nextLine().trim();

        if (!contact.matches("01\\d{8,9}")) {
            System.out.println("Invalid contact number. Use Malaysian format, e.g. 0123456789.");
        } else if (controller.contactNumberExists(contact)) {
            System.out.println("Contact number already exists. Please enter another number.");
        } else {
            return contact;
        }

    } while (true);
}
    
    private String inputValidNationality(String message) {
    String nationality;
    do {
        System.out.print(message);
        nationality = scanner.nextLine().trim();
        if (!nationality.equalsIgnoreCase("Malaysian") &&
            !nationality.equalsIgnoreCase("Non-Malaysian")) {
            System.out.println("Invalid nationality. Enter Malaysian or Non-Malaysian only.");
        }
    } while (!nationality.equalsIgnoreCase("Malaysian") &&
             !nationality.equalsIgnoreCase("Non-Malaysian"));
    return nationality;
}
    
    private String inputValidIC(String message) {
    String ic;

    do {
        System.out.print(message);
        ic = scanner.nextLine().trim();

        if (!ic.matches("\\d{6}-\\d{2}-\\d{4}")) {
            System.out.println("Invalid IC format. Example: 010101-10-1234");
        } else if (controller.idNumberExists(ic)) {
            System.out.println("IC number already exists. Duplicate patient record is not allowed.");
        } else {
            return ic;
        }

    } while (true);
    }
    
    private String inputValidPassport(String message) {
    String passport;

    do {
        System.out.print(message);
        passport = scanner.nextLine().trim();

        if (passport.isEmpty()) {
            System.out.println("Passport number cannot be empty.");
        } else if (controller.idNumberExists(passport)) {
            System.out.println("Passport number already exists. Duplicate patient record is not allowed.");
        } else {
            return passport;
        }

    } while (true);
    }
    
    
    private String inputValidPatientType(String message) {
    String type;
    do {
        System.out.print(message);
        type = scanner.nextLine().trim();
        if (!type.equalsIgnoreCase("Emergency") && !type.equalsIgnoreCase("Normal")) {
            System.out.println("Invalid patient type. Enter Emergency or Normal only.");
        }
    } while (!type.equalsIgnoreCase("Emergency") && !type.equalsIgnoreCase("Normal"));
    return type;
    }
    
    //VALIDATION FOR UPDATE PATIENT'S DETAILS
    private String inputValidContactUpdate(String message, String patientId) {
    String contact;

    do {
        System.out.print(message);
        contact = scanner.nextLine().trim();

        if (!contact.matches("01\\d{8,9}")) {
            System.out.println("Invalid contact number. Example: 0123456789");
        } else if (controller.contactNumberExistsExcept(contact, patientId)) {
            System.out.println("Contact number already exists. Please enter another number.");
        } else {
            return contact;
        }

    } while (true);
    }
    
    private String inputValidICUpdate(String message, String patientId) {
    String ic;

    do {
        System.out.print(message);
        ic = scanner.nextLine().trim();

        if (!ic.matches("\\d{6}-\\d{2}-\\d{4}")) {
            System.out.println("Invalid IC format. Example: 010101-10-1234");
        } else if (controller.idNumberExistsExcept(ic, patientId)) {
            System.out.println("IC already exists.");
        } else {
            return ic;
        }

    } while (true);
    }
    
    private String inputValidPassportUpdate(String message, String patientId) {
    String passport;

    do {
        System.out.print(message);
        passport = scanner.nextLine().trim();

        if (passport.isEmpty()) {
            System.out.println("Passport cannot be empty.");
        } else if (controller.idNumberExistsExcept(passport, patientId)) {
            System.out.println("Passport already exists.");
        } else {
            return passport;
        }

    } while (true);
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
