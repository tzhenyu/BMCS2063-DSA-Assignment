/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author zhenyu
 */
import ListADT.Node;
import ListADT.ListADT;
import Clinic.Boundary.MedicineBoundary;
import Clinic.Boundary.PatientBoundary;
import Clinic.Boundary.AppointmentBoundary;
import Clinic.Control.AppointmentController;
import Clinic.Control.PatientController;
import Clinic.Control.StaffController;
import Clinic.Boundary.StaffBoundary;

public class Main {

    public static void main(String[] args) {
//        Initialize Boundary
        //MedicineBoundary medicineBoundary = new MedicineBoundary();
        
        //medicineBoundary.start();
 
        //Staff here
        StaffBoundary staffBoundary = new StaffBoundary();
        staffBoundary.start();

        PatientController patientController = new PatientController();
        patientController.loadFile();   // load patient records first

        AppointmentController appointmentController =
                new AppointmentController(patientController);

        PatientBoundary patientBoundary = new PatientBoundary(patientController);
        AppointmentBoundary appointmentBoundary =
                new AppointmentBoundary(appointmentController);

        patientBoundary.start();        // run patient module first
        appointmentBoundary.start();    // then run appointment module
        
//       //Playing with Nodes
//        Node<String> n1 = new Node<>("apple");
//        Node<String> n2 = new Node<>("banana");        
//        Node<String> n3 = new Node<>("cidar");        
//        
//        // Circular linked list
//        n1.setNextNode(n2);
//        n2.setNextNode(n3);
//        n3.setNextNode(n1);
//        
////        //Print out data, will cause infinite loop
////        Node<String> current = n1;
////        
////        while (current != null) { 
////            System.out.println(current.getData());
////            current = current.getNextNode();
////        }
//        
//        // Prints out data, but will stop when loop back to start
//        Node<String> start = n1;
//        Node<String> current = start;
//        
//        do {
//            System.out.println(current.getData());
//            current = current.getNextNode();
//        } while (current != start);
//        
//        
//        //Playing with insert()
//        //Define object
//        ListADT<String> patient = new ListADT<>();
//        
//        patient.insert("your mama");
//        patient.insert("your baba");
//        
//        patient.display(); //someone implement this!
    }
}

