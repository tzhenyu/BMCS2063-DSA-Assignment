/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clinic.Control;
import ListADT.ListADT;
import Clinic.Entity.Medicine;
/**
 *
 * @author zhenyu
 */
public class MedicineController {
    
    private ListADT<Medicine> medicineList;
    
    public MedicineController() {
        medicineList = new ListADT<>();
    }
    
    public void insertMedicine(Medicine medicine) {
        medicineList.insert(medicine);
    }
    
    public void deleteMedicine(Medicine medicine) {
        medicineList.remove(medicine);
    }
    
    public void listAllMedicine() {
        medicineList.display();
    }
    
    public void searchMedicineByName(Medicine medicineName) {
        medicineList.search(medicineName);
    }
    
    public void sortMedicine() {
        medicineList.sort();
    }
    
}
