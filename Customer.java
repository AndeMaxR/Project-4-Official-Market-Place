import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Customer {
    private String username;
    private String password;
    private String name;

    public Customer(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public void viewMarket() {
        try {
            File masterList = new File("ItemMasterList.txt");
            if (!masterList.exists()) {
                masterList.createNewFile();
            }
            BufferedReader bfr = new BufferedReader(new FileReader(masterList));
            String fileContent;
            while (true) {
                fileContent = bfr.readLine();
                if (fileContent == null) {
                    break;
                } else {
                    System.out.println(fileContent);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("There are currently no items for sale!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
