/*
 * Click nbfs:nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs:nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package StorageHandler;

/**
 *
 * @author zhenyu
 */
public interface FileHandler {
    // THIS IS USED ON YOUR CONTROLLER CLASS
    // public class [CONTROLLER] implements FileHandler {
    //                           ^^^^^^^^^^^^^^^^^^^^^^
    // ALSO DECLARE YOUR STATIC FILENAME TO PATH, EXAMPLE:
    // private static final String FILENAME = "src/medicine.csv";
    // YOU CAN REFER MedicineController.java FOR MORE
    
    /*
    Example Use: 
        @Override
        public void loadFile() {
            System.out.println("Loading from file: " + FILENAME);
            File csvFile = new File(FILENAME);

            Medicine m = new Medicine();                    < CHANGE TO YOUR ENTITY!

            try (Scanner scanner = new Scanner(csvFile)) {
                 skips header
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] fields = line.split(";", -1);

                    Medicine med = m.fromCSV(fields);       < CHANGE TO YOUR ENTITY!
                    if (med != null) {                      < CHANGE TO YOUR ENTITY!
                        medicineList.insert(med);           < CHANGE TO YOUR ENTITY!
                    }
                }
                System.out.println("File Loaded.");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    */
    void loadFile();
    
    /*
        @Override
        public void saveFile() {
            System.out.println("Saving file to: " + FILENAME);
            File csvFile = new File(FILENAME);
            try (PrintWriter writer = new PrintWriter(csvFile)) {

                writer.println("NAME;DESCRIPTION;QUANTITY;REG. NUM;EXPIRY DATE;LAST UPDATED DATE"); < CHANGE TO YOUR HEADER!

                for (Medicine m: medicineList) {                                                    < CHANGE TO YOUR ENTITY!
                    writer.println(m.toCSV());
                }
                writer.close();
                System.out.println("Filed saved.");
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    */
    void saveFile();
}


