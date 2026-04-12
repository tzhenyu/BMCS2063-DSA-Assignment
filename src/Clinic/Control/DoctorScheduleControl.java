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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DoctorScheduleControl {
    
    private ListADT<Appointment> appointmentList = new ListADT<>();
    private static final int MAX_PATIENTS_PER_SLOT = 6; // allowing maximum 6 patients per slot
    private static final int MAX_DOCTORS_PER_SLOT = 2; // allowing maximum 2 doctors perslot
    
    private final String[] timeSlots = {"10-11", "11-12", "12-13", "13-14", "14-15", "15-16", "16-17"}; //fixed timeslots
    //matrix of list for both slots and day
    private ListADT<Doctor>[][] weeklySchedule;  // [day][slot] 
    private ListADT<Doctor> doctorList;
    private LocalDate systemDate; 
    
    public DoctorScheduleControl(ListADT<Doctor> doctorList) {
        this.doctorList = doctorList;
        this.systemDate = LocalDate.now();  // Real today
        
        initializeWeeklySchedule();
    }
    
    private void initializeWeeklySchedule() {
        weeklySchedule = new ListADT[7][7];
        for (int day = 0; day < 7; day++) {
            for (int slot = 0; slot < 7; slot++) {
                // each day = 7 slots
                // total 49 slots for all 7 days
                weeklySchedule[day][slot] = new ListADT<>();
            }
        }
    }
    
    public LocalDate getWeekStart() { 
        return systemDate; 
    }
    public LocalDate getWeekEnd() { 
        return getWeekStart().plusDays(6); 
    }
    
    public void setSystemDate(int year, int month, int day) {
        this.systemDate = LocalDate.of(year, month, day);
    }
    
    // operation for doctor to add their availability 
    public boolean addAvailability(String doctorId, String dateStr, String slot) {
        Doctor doctor = findDoctor(doctorId);
        if (doctor == null){
            return false;
        } 
        
        
        int dayIndex = getDayIndex(dateStr); // arranges the dy according to the index
        int slotIndex = getSlotIndex(slot); // arranges the slot
        if (dayIndex == -1 || slotIndex == -1) 
        {
            return false;
        }
        
        ListADT<Doctor> slotList = weeklySchedule[dayIndex][slotIndex];
        if (slotList.search(doctor) != null)
            return false;
        if (slotList.size() >= MAX_DOCTORS_PER_SLOT) 
            return false;
        
        slotList.insert(doctor);
        return true;
    }
    
    public boolean removeAvailability(String doctorId, String dateStr, String slot) {
        int dayIndex = getDayIndex(dateStr);
        int slotIndex = getSlotIndex(slot);
        if (dayIndex == -1 || slotIndex == -1) return false;
        
        ListADT<Doctor> slotList = weeklySchedule[dayIndex][slotIndex];
        Doctor doctor = findDoctor(doctorId);
        if (doctor == null) return false;
        
        return slotList.remove(doctor);
    }
    
    // operations for patient
    public void viewAvailableSlots() {
        LocalDate weekStart = getWeekStart();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("\n=== Available Slots (" + 
            weekStart.format(df) + " to " + getWeekEnd().format(df) + ") ===");
        
        for (int day = 0; day < 7; day++) {
            LocalDate date = weekStart.plusDays(day);
            System.out.println("\n" + date.format(df) + ":");
            
            for (int slot = 0; slot < 7; slot++) {
                String timeSlot = timeSlots[slot];
                ListADT<Doctor> slotList = weeklySchedule[day][slot];
                
                if (!slotList.isEmpty()) {
                    int booked = getBookedCount(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), timeSlot);
                    System.out.print(timeSlot + " (" + booked + "/" + MAX_PATIENTS_PER_SLOT + ") | ");
                    for (Doctor d : slotList) {
                        System.out.print(d.getName() + " [" + d.getDoctorId() + "] ");
                    }
                    System.out.println();
                }
            }
        }
    }
    
    public boolean bookAppointment(String patientId, String dateStr, String timeSlot) {
        int dayIndex = getDayIndex(dateStr);
        int slotIndex = getSlotIndex(timeSlot);
        if (dayIndex == -1 || slotIndex == -1) return false;
        
        ListADT<Doctor> slotList = weeklySchedule[dayIndex][slotIndex];
        if (slotList.isEmpty()) return false;
        
        Doctor doctor = slotList.getEntry(0);  // First available doctor
        String doctorId = doctor.getDoctorId();
        
        int booked = getBookedCountForDoctor(doctorId, dateStr, timeSlot);
        if (booked >= MAX_PATIENTS_PER_SLOT) return false;
        
        Appointment app = new Appointment(patientId, doctorId, dateStr, timeSlot);
        appointmentList.insert(app);
        return true;
    }
    
    public void viewMyAppointments(String patientId) {
        System.out.println("=== My Appointments (" + patientId + ") ===");
        boolean found = false;
        for (Appointment a : appointmentList) {
            if (a.getPatientId().equals(patientId)) {
                System.out.println(a);
                found = true;
            }
        }
        if (!found) System.out.println("No appointments");
    }
    
    public boolean cancelAppointment(String patientId, String date, String time) {
        for (int i = 0; i < appointmentList.size(); i++) {
            Appointment a = appointmentList.getEntry(i);
            if (a.getPatientId().equals(patientId) && a.getDate().equals(date) && a.getTime().equals(time)) {
                appointmentList.remove(a);
                return true;
            }
        }
        return false;
    }
    
    public boolean changeAppointment(String patientId, String oldDate, String oldTime, String newDate, String newTime) {
        if (cancelAppointment(patientId, oldDate, oldTime)) {
            return bookAppointment(patientId, newDate, newTime);
        }
        return false;
    }
    
    public String getScheduleDisplay() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate weekStart = getWeekStart();
        sb.append("=== Schedule: ").append(weekStart.format(df))
          .append(" - ").append(getWeekEnd().format(df)).append(" ===\n");
        
        for (int day = 0; day < 7; day++) {
            LocalDate date = weekStart.plusDays(day);
            sb.append(date.format(df)).append(":\n");
            for (int slot = 0; slot < 7; slot++) {
                ListADT<Doctor> slotList = weeklySchedule[day][slot];
                if (!slotList.isEmpty()) {
                    sb.append("  ").append(timeSlots[slot]).append(": ");
                    for (Doctor d : slotList) sb.append(d.getDoctorId()).append(" ");
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }
    
    // Helpers
    private int getDayIndex(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        LocalDate weekStart = getWeekStart();
        long daysDiff = ChronoUnit.DAYS.between(weekStart, date);
        return (daysDiff >= 0 && daysDiff < 7) ? (int)daysDiff : -1;
    }
    
    private int getSlotIndex(String slot) {
        for (int i = 0; i < timeSlots.length; i++) {
            if (timeSlots[i].equals(slot)) return i;
        }
        return -1;
    }
    
    private int getBookedCount(String date, String timeSlot) {
        int count = 0;
        for (Appointment a : appointmentList) {
            if (a.getDate().equals(date) && a.getTime().equals(timeSlot)) count++;
        }
        return count;
    }
    
    private int getBookedCountForDoctor(String doctorId, String date, String timeSlot) {
        int count = 0;
        for (Appointment a : appointmentList) {
            if (a.getDoctorId().equals(doctorId) && a.getDate().equals(date) && a.getTime().equals(timeSlot)) count++;
        }
        return count;
    }
    
    private Doctor findDoctor(String doctorId) {
        for (int i = 0; i < doctorList.size(); i++) {
            if (doctorList.getEntry(i).getDoctorId().equals(doctorId)){ 
                return doctorList.getEntry(i);
        
            }
        }
        return null;
    }

    public ListADT<Appointment> getAppointmentList() {
        return appointmentList;
    }
    
    public ListADT<Doctor> getDoctorList() { 
        return doctorList; 
    }
}