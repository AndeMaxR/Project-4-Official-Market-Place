import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;


public class Seller {
    private String username;
    private String password;
    private ArrayList<Store> storeList; //list of all stores owned by this seller

    public Seller(String username, String password) {
        this.username = username;
        this.password = password;
        this.storeList = new ArrayList<>();
        File file = new File(username + ".txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
                printToFile();
            } else {
                String line;
                String[] content;
                BufferedReader bfr = new BufferedReader(new FileReader(file));
                line = bfr.readLine();
                line = bfr.readLine();
                if (line != null) {
                    content = line.split(",");
                    for (int i = 0; i < content.length; i++) {
                        storeList.add(new Store(username, content[i]));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't create new seller file for whatever reason.");
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        for (int i = 0; i < storeList.size(); i++) {
            storeList.get(i).setOwner(username);
        }
        File file = new File(this.username + ".txt");
        file.delete();
        this.username = username;
        printToFile();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        printToFile();
    }

    public Store getSpecificStore(int i) {
        return storeList.get(i);
    }
    public ArrayList<Store> getFullStoreList() {
        return storeList;
    }

    public String getStoreList() {
        String holder = "";
        for (int i = 0; i < storeList.size(); i++) {
            holder += (i + 1) + ". " + storeList.get(i).getStoreName() + "\n";
        }
        return holder;
    }

    public void addStore(Store store) {
        this.storeList.add(store);
        printToFile();
    }

    public boolean removeStore(int location) {
        File file = new File(username + "_" + storeList.get(location).getStoreName() + ".txt");
        if (file.exists()) {
            if (!file.delete()) {
                return false;
            }
        }
        if (storeList.remove(location) != null) {
            return true;
        } else {
            return false;
        }

    }

    public void printToFile() {
        File file = new File(username + ".txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bfw = new BufferedWriter(new FileWriter(file));
            bfw.write("SELLER," + username + "," + password + "\n");
            for (int i = 0; i < storeList.size(); i++) {
                if (i == storeList.size() - 1) {
                    bfw.write(storeList.get(i).getStoreName() + "\n");
                } else {
                    bfw.write(storeList.get(i).getStoreName() + ",");
                }
            }
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    //edits the item inputted in the parameter

    /**
     * Keep for later for market - Max
     * switch (editChoice) {
     *                 case 1:
     *                     System.out.println("What would you like to change the product name to?");
     *                     String newName = scan.nextLine();
     *                     item.setProductName(newName);
     *                     break;
     *                 case 2:
     *                     String newDesc;
     *                     do {
     *                         System.out.println("What would you like to change the product description to?");
     *                         newDesc = scan.nextLine();
     *                         if (newDesc.length() > 200) {
     *                             System.out.println("Invalid Description: Product description must be less than 200 characters!");
     *                         }
     *                     } while (newDesc.length() > 200);
     *                     break;
     *                 case 3:
     *                     System.out.println("What would you like to change the available quantity to?");
     *                     int newAvailQuant = scan.nextInt();
     *                     scan.nextLine();
     *                     item.setQuantityAvailable(newAvailQuant);
     *                     break;
     *                 case 4:
     *                     System.out.println("What would you like to change the items price to?");
     *                     double newPrice = scan.nextDouble();
     *                     scan.nextLine();
     *                     item.setPrice(newPrice);
     *                     break;
     *                 default:
     *                     choiceValid = false;
     *                     System.out.println("Invalid input: Please type an integer between 1 and 4.");
     *                     break;
     *             }
     */

}