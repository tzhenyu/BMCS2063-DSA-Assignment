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
    private final PatientController controller;   //final = the variable can only assigned once and cannot be changed
    private final Scanner scanner;
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    
    public PatientBoundary(PatientController controller) {
    this.controller = controller;
    this.scanner = new Scanner(System.in);
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
        int choice = -1;   //ct as a safe starting value
        
        do{
            System.out.println("============================");
            System.out.println("    Patient Management");
            System.out.println("============================");
            System.out.println("1. Add Patient");
            System.out.println("2. Search Patient");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Display All Patients");
            System.out.println("6. Check Insurance Eligibility");
            System.out.println("7. Export Medical Report");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
        try {
            choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
                }
            
            switch (choice) {
                case 1 -> addPatient();
                case 2 -> searchPatient();
                case 3 -> updatePatient();
                case 4 -> deletePatient();
                case 5 -> displayPatients();
                case 6 -> checkInsurance();
                case 7 -> exportMedicalReport();
                case 0 -> exit();
                default -> error("Invalid choice.");
            }
        } while (choice !=0);  //if user enter 0 = exit, else continue loop
    } // close for the menu
    
    private void addPatient(){
        Patient patient = readPatientDetails();
        
        if (patient == null){
            error("Add patient cancelled.");
            pause();
            return;
        }
        controller.addPatient(patient);
        controller.saveFile();
        System.out.println("Patient added successfully.");
        pause();
    }
    
    private void searchPatient(){
        System.out.print("Enter Patient ID to search: ");
        String patientId = scanner.nextLine().trim().toUpperCase();
        
        Patient found = controller.searchPatientById(patientId);
        
        if(found != null){
            printHeader();
            System.out.print(found);
        }else{
           error("Patient not found.");
        }
        
        pause();
    }
    
    private void updatePatient() {
    System.out.print("Enter Patient ID to update: ");
    String patientId = scanner.nextLine().trim().toUpperCase();

    Patient found = controller.searchPatientById(patientId);

    if (found == null) {
        error("Patient not found.");
        pause();
        return;
    }

    int choice = -1;

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
       
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
           error("Invalid input.");
           continue;
        }

        switch (choice) {
            case 1 -> {
                String name = inputValidName("New Name: ");
                if (controller.nameExists(name)) {
                    error("Warning: A patient with the same name already exists.");
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

            default -> error("Invalid choice.");
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
    String patientId = scanner.nextLine().trim().toUpperCase();

    boolean deleted = controller.deletePatientById(patientId);

    if (deleted) {
        controller.saveFile();
        System.out.println("Patient deleted successfully.");
    } else {
        error("Patient not found.");
    }

    pause();
    }
    
    
    private void displayPatients() {
    if (controller.getTotalPatients() == 0) {
        error("No patient records found.");
        pause();
        return;
    }

    System.out.println("Display option:");
    System.out.println("1. Current order");
    System.out.println("2. Sort by Patient ID");
    System.out.println("3. Sort by Patient Name");
    System.out.print("Enter choice: ");
    
    int choice;
          try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
           error("Invalid input.");
           pause();
           return;
        }

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
            error("Invalid choice.");
            pause();
            return;
        }
    }

    printHeader();
    System.out.print(controller.displayPatients());
    System.out.println("Total Patients: " + controller.getTotalPatients());
    pause();
}

    private void checkInsurance() {
    System.out.print("Enter Patient ID to check insurance: ");
    String patientId = scanner.nextLine().trim().toUpperCase();

    Patient found = controller.searchPatientById(patientId);

    if (found != null) {
        System.out.println("\n=== Patient Insurance Information ===");
        System.out.println("Patient ID   : " + found.getPatientId());
        System.out.println("Name         : " + found.getName());
        System.out.println("Nationality  : " + found.getNationality());
        System.out.println("Patient Type : " + found.getPatientType());

        if (controller.isEligibleForLocalInsurance(found)) {
            if (found.getPatientType().equalsIgnoreCase("Emergency")) {
                System.out.println("Status       : Emergency case. Treatment first, insurance will be verified later.");
            } else {
                System.out.println("Status       : Eligible for local insurance checking.");
            }
        } else {
            System.out.println("Status       : Non-Malaysian patient. Passport/international insurance applies.");
        }

        } else {
        error("Patient not found.");
         }

    pause();
    }

    private void exportMedicalReport() {
    System.out.print("Enter Patient ID: ");
    String patientId = scanner.nextLine().trim().toUpperCase();

    Patient found = controller.searchPatientById(patientId);

    if (found != null) {
        controller.exportMedicalReport(found);
    } else {
        error("Patient not found.");
    }
    pause();
}
    
    private Patient readPatientDetails() {
    Patient patient = new Patient();

    String newPatientId = controller.generateNextPatientId();
    System.out.println("Generated Patient ID: " + newPatientId);
    patient.setPatientId(newPatientId);

    String name = inputValidName("Name (or type 0 to cancel): ");
    if(name == null){
        return null;
    }
    
    patient.setName(name);
    
    if (controller.nameExists(name)) {
        error("Warning: A patient with the same name already exists. Please check to avoid duplicate record.");
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
    patient.setAllergyInfo(inputNoneMsg("Allergy Info: "));
    patient.setMedicalHistory(inputNoneMsg("Medical History: "));

    return patient;
}

    private void printHeader() {
        System.out.printf("| %-8s | %-20s | %-3s | %-8s | %-13s | %-12s | %-10s | %-15s | %-12s | %-15s | %-20s |%n",
                "ID", "NAME", "AGE", "GENDER", "CONTACT", "NATIONALITY",
                "ID TYPE", "ID NUMBER", "TYPE", "ALLERGY", "HISTORY");
    }
    
    //VALIDATION FOR INPUT OF PATIENT DETAILS, show empty error msg,enter 0 to exit
    private String inputNonEmpty(String message) {
    String input;
    do {
        System.out.print(message + " (or type 0 to cancel): ");
        input = scanner.nextLine().trim();

        if (input.equals("0")) {
            return null; // cancel signal
        }

        if (input.isEmpty()) {
            System.out.println("Input cannot be empty.");
        }

    } while (input.isEmpty());

    return input;
    }
    
    private String inputNoneMsg(String message) {
    String input;
    do {
        System.out.print(message);
        input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            error("Input cannot be empty. Please enter 'None' if there is no information.");
        }

    } while (input.isEmpty());

    return input;
    }
    
    private String inputValidName(String message) {
    String name;

    do {
        System.out.print(message);
        name = scanner.nextLine().trim();

        if (name.equals("0")){
            return null;
        }
        
        if (!name.matches("^[A-Za-z]+(\\s[A-Za-z]+)+$")) {
            error("Invalid name. Use letters and at least 2 words.");
        } 
        else if (name.length() < 3) {
            error("Name too short.");
        }
        else {
            return name;
        }

    } while (true);
    }
    
    private int inputValidAge(String message) {
    int age;

    while (true) {
        try {
            System.out.print(message);
            age = Integer.parseInt(scanner.nextLine());

            if (age >= 1 && age <= 110) {
                return age;   // ✅ return immediately when valid
            } else {
                error("Age must be between 1 and 110.");
            }

        } catch (NumberFormatException e) {
            error("Invalid age. Please enter a number.");
        }
    }
}
    
    private String inputValidGender(String message) {
    String gender;
    do {
        System.out.print(message);
        gender = scanner.nextLine().trim();
        if (!gender.equalsIgnoreCase("Male") && !gender.equalsIgnoreCase("Female")) {
            error("Invalid gender. Enter Male or Female only.");
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
            error("Invalid contact number. Use Malaysian format, e.g. 0123456789.");
        } else if (controller.contactNumberExists(contact)) {
            error("Contact number already exists. Please enter another number.");
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
            error("Invalid nationality. Enter Malaysian or Non-Malaysian only.");
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
            error("Invalid IC format. Example: 010101-10-1234");
        } else if (controller.idNumberExists(ic)) {
            error("IC number already exists. Duplicate patient record is not allowed.");
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
            error("Passport number cannot be empty.");
        } else if (controller.idNumberExists(passport)) {
            error("Passport number already exists. Duplicate patient record is not allowed.");
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
            error("Invalid patient type. Enter Emergency or Normal only.");
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
            error("Invalid contact number. Example: 0123456789");
        } else if (controller.contactNumberExistsExcept(contact, patientId)) {
            error("Contact number already exists. Please enter another number.");
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
            error("Invalid IC format. Example: 010101-10-1234");
        } else if (controller.idNumberExistsExcept(ic, patientId)) {
            error("IC already exists.");
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
            error("Passport cannot be empty.");
        } else if (controller.idNumberExistsExcept(passport, patientId)) {
            error("Passport already exists.");
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
    
    private void error(String message) {
    System.out.println(RED + message + RESET);
}
}
