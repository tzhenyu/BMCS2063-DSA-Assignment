/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/

package Clinic.Control;
/**
 *
 * @author ASUS
 */    



import ListADT.ListADT;
import Clinic.Entity.Doctor;
import Clinic.Entity.Appointment;
import ListADT.ListADTInterface;

public class DoctorScheduleControl {
    
    private ListADTInterface<Appointment> appointmentList = new ListADT<>();
    private static final int MAX_PATIENTS_PER_SLOT = 6;

    private static final int MAX_DOCTORS_PER_SLOT = 2;

    private String[] timeSlots = {
        "10-11", "11-12", "12-13",
        "13-14", "14-15", "15-16", "16-17"
    };

    // Each slot stores a list of doctors
    private ListADTInterface<Doctor>[] schedule;

    private ListADTInterface<Doctor> doctorList; // all registered doctors

    // ==========================
    // CONSTRUCTOR (FIXED)
    // ==========================
    public DoctorScheduleControl(ListADTInterface<Doctor> doctorList,
                                 ListADTInterface<Doctor>[] schedule) {
        this.doctorList = doctorList;
        this.schedule = schedule;

        // Ensure schedule is initialized
        for (int i = 0; i < timeSlots.length; i++) {
            if (this.schedule[i] == null) {
                this.schedule[i] = new ListADT<>();
            }
        }
    }
    
    // View Available Slots
    public void viewAvailableSlots(String doctorIdFilter) {
        System.out.println("=== Available Slots ===");
        for (int i = 0; i < timeSlots.length; i++) {
            String slot = timeSlots[i];
            System.out.print(slot + " | Doctors: ");

            ListADTInterface<Doctor> slotList = schedule[i];
            if (slotList.isEmpty()) {
                System.out.print("No doctors available");
            } else {
                for (Doctor d : slotList) {
                    if (doctorIdFilter.isEmpty() || d.getDoctorId().equals(doctorIdFilter)) {
                        System.out.print(d.toString() + " ");
                    }
                }
            }

            // count booked patients for this slot and this doctor(s)
            int booked = 0;
            for (Appointment a : appointmentList) {
                if (a.getTime().trim().equals(slot.trim())) {
                    booked++;
                }
            }

            System.out.println("| Booked: " + booked + "/6");
        }
    }
    
    public void viewMyAppointments(String patientId) {
        System.out.println("=== My Appointments ===");
        boolean found = false;
        for (Appointment a : appointmentList) {
            if (a.getPatientId().equals(patientId)) {
                System.out.println(a);
                found = true;
            }
        }
        if (!found) System.out.println("No appointments booked");
    }

    // ==========================
    // ADD AVAILABILITY
    // ==========================
    public boolean addAvailability(String doctorId, String slot) {
        Doctor doctor = findDoctor(doctorId);
        if (doctor == null) return false;

        int index = findSlotIndex(slot);
        if (index == -1) return false;

        ListADTInterface<Doctor> slotList = schedule[index];

        // check duplicate manually
        for (int i = 0; i < slotList.size(); i++) {
            if (slotList.getEntry(i).getDoctorId().equals(doctorId)) {
                return false;
            }
        }

        // check max limit
        if (slotList.size() >= MAX_DOCTORS_PER_SLOT) {
            return false;
        }

        slotList.insert(doctor);
        return true;
    }

    // ==========================
    // REMOVE AVAILABILITY
    // ==========================
    public boolean removeAvailability(String doctorId, String slot) {
        int index = findSlotIndex(slot);
        if (index == -1) return false;

        ListADTInterface<Doctor> slotList = schedule[index];

        for (int i = 0; i < slotList.size(); i++) {
            if (slotList.getEntry(i).getDoctorId().equals(doctorId)) {
                return slotList.remove(slotList.getEntry(i));
            }
        }

        return false;
    }

    // ==========================
    // DISPLAY SCHEDULE
    // ==========================
    public String getScheduleDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Doctor Schedule ===\n");

        for (int i = 0; i < timeSlots.length; i++) {
            sb.append(timeSlots[i]).append(" : ");

            if (schedule[i].isEmpty()) {
                sb.append("No doctors available");
            } else {
                for (int j = 0; j < schedule[i].size(); j++) {
                    sb.append(schedule[i].getEntry(j).getDoctorId()).append(" ");
                }
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    // ==========================
    // REQUIRED BY PatientBoundary
    // ==========================
    public boolean changeAppointment(String patientId,String oldDate, String oldTime,String newDoctor, String newDate, String newTime) {

        if (cancelAppointment(patientId, oldDate, oldTime)) {
            return bookAppointment(patientId, newDoctor, newDate, newTime, "");
        }
        return false;
    }
    
    
    public boolean cancelAppointment(String patientId, String date, String time) {

        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment a = appointmentList.getEntry(i);

            if (a.getPatientId().equals(patientId)
                    && a.getDate().equals(date)
                    && a.getTime().equals(time)) {

                appointmentList.remove(a);
                return true;
            }
        }
        return false;
    }

    // TEMP booking logic
    public boolean bookAppointment(String patientId, String doctorId,String date, String time, String issue) {
        int index = findSlotIndex(time);
        if (index == -1) return false;

        ListADTInterface<Doctor> slotList = schedule[index];
        Doctor doctor = findDoctor(doctorId);
        if (doctor == null) return false;

        // check doctor available in that slot
        if (slotList.search(doctor) == null) return false;

        // check max 6 patients per doctor per slot
        int count = 0;
        for (Appointment a : appointmentList) {
            if (a.getDoctorId().equals(doctorId)
                    && a.getDate().equals(date)
                    && a.getTime().equals(time)) {
                count++;
            }
        }
        if (count >= 6) return false;

        // create and insert the appointment
        Appointment app = new Appointment(patientId, doctorId, date, time);
        appointmentList.insert(app);
        return true;
    }

    // ==========================
    // HELPER METHODS
    // ==========================
    private int findSlotIndex(String slot) {
        for (int i = 0; i < timeSlots.length; i++) {
            if (timeSlots[i].equals(slot)) {
                return i;
            }
        }
        return -1;
    }

    private Doctor findDoctor(String doctorId) {
        for (int i = 0; i < doctorList.size(); i++) {
            Doctor d = doctorList.getEntry(i);
            if (d.getDoctorId().equals(doctorId)) {
                return d;
            }
        }
        return null;
    }
}