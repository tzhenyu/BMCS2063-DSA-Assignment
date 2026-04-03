/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package StorageHandler;

/**
 *
 * @author zhenyu
 */
public interface CSVHandler<T> {
    // THIS IS USED ON YOUR ENTITY CLASS
    // public class [ENTITY] implements CSVHandler<[ENTITY]> {
    //                       ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // YOU CAN REFER Medicine.java FOR MORE
    
    // For Saving: Converts the object's data into a single CSV line
    /*
    Example:
        @Override
        public String toCSV() {
          // its like toString() but make it in CSV format (semicolon not comma)

            return (name + ";" + description + ";" + String.valueOf(quantity) + ";" + registrationNum + ";" + expiryDate + ";" + lastUpdated);
        }
    */
    String toCSV();

    // For Loading: Takes an array of strings and returns a new object of type T
    /*
    Example:
        @Override
        public Medicine fromCSV(String[] field) {
            // this is to mapped the field from file to the program.

            // Example from my csv file:
            // NAME;DESCRIPTION;QUANTITY;REG. NUM;EXPIRY DATE;LAST UPDATED DATE
            // Amoxicillin;Antibiotic;300;MAL12312317XZ;2031-04-04;2026-03-25

            // Just map it like below:
            String name = field[0].trim();
            String description = field[1].trim();
            int quantity = Integer.parseInt(field[2].trim());
            String regNum = field[3].trim();
            LocalDate expiryDate = LocalDate.parse(field[4].trim());
            LocalDate lastUpdated = LocalDate.parse(field[5].trim());

            // Then convert it into your respective object
            return new Medicine(name, description, quantity, regNum, expiryDate, lastUpdated);
        }
    */
    T fromCSV(String[] fields);
}

