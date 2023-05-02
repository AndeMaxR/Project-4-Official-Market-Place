import javax.swing.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.Buffer;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Customer
 *
 * This class is meant for buyer objects to purchase items.
 *
 * @author Mark Herman, Max Anderson, Colin McKee, Aarnav Bomma, Section L06
 *
 * @version 5/1/2023
 */

public class Customer {
    private String username;
    private String password;
    private Store store;
    private ArrayList<Store> storeArrayList;

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
        storeArrayList = new ArrayList<>();
        try {
            File customerFile = new File(username + ".txt");
            if (!customerFile.exists()) {
                customerFile.createNewFile();
                printToFile();
            }
            File masterList = new File("StoreMasterList.txt");
            if (masterList.exists()) {
                BufferedReader bfr = new BufferedReader(new FileReader(masterList));
                String content;
                while (true) {
                    content = bfr.readLine();
                    if (content == null) {
                        break;
                    } else {
                        String[] splitContent;
                        content = content.substring(0, content.indexOf("."));
                        splitContent = content.split("_");
                        storeArrayList.add((new Store(splitContent[0], splitContent[1])));
                    }
                }
                bfr.close();
            }
            File itemMasterList = new File("ItemMasterList.txt");
            if (!itemMasterList.exists()) {
                itemMasterList.createNewFile();
            }
            BufferedWriter bfw = new BufferedWriter(new FileWriter("ItemMasterList.txt", false));
            for (int i = 0; i < storeArrayList.size(); i++) {
                for (int j = 0; j < storeArrayList.get(i).getItemListSize(); j++) {
                    bfw.write(storeArrayList.get(i).getItem(j).toString() + "\n");
                }
            }
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Store> getStoreArrayList() {
        return storeArrayList;
    }

    public boolean viewMarket() {
        try {
            File masterList = new File("ItemMasterList.txt");
            if (!masterList.exists()) {
                masterList.createNewFile();
            }
            BufferedReader bfr = new BufferedReader(new FileReader(masterList));
            String fileContent;
            int counter = 0;

            JFrame viewMarketFrame = new JFrame();
            viewMarketFrame.setTitle("Entire Market");
            Container content = viewMarketFrame.getContentPane();
            content.setLayout(new BoxLayout(viewMarketFrame, BoxLayout.PAGE_AXIS));

            while (true) {
                fileContent = bfr.readLine();
                if (fileContent == null) {
                    if (counter == 0) {
                        System.out.println("Couldn't find ItemMasterList.txt");
                        return false;
                    }
                    break;
                } else {
                    viewMarketFrame.add(new JLabel(fileContent));
                }
                counter++;
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find ItemMasterList.txt");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> viewMarketNameSearch(String name, ArrayList<Item> tempMarket) {
        ArrayList<String> searchResults = new ArrayList<String>();

        for (int i = 0; i < tempMarket.size(); i++) {
            if (tempMarket.get(i).getProductName().contains(name)) {
                searchResults.add(tempMarket.get(i).getProductName() + ", Store: " + tempMarket.get(i).getStoreName());
            }
        }

        return searchResults;
    }

    public ArrayList<String> viewMarketStoreSearch(String storeName, ArrayList<Item> tempMarket) {
        ArrayList<String> searchResults = new ArrayList<String>();

        for (int i = 0; i < tempMarket.size(); i++) {
            if (tempMarket.get(i).getStoreName().contains(storeName)) {
                searchResults.add(tempMarket.get(i).getProductName() + ", Store: " + tempMarket.get(i).getStoreName());
            }
        }

        return searchResults;
    }

    public ArrayList<String> viewMarketDescriptionSearch(String description, ArrayList<Item> tempMarket) {
        ArrayList<String> searchResults = new ArrayList<String>();

        for (int i = 0; i < tempMarket.size(); i++) {
            if (tempMarket.get(i).getProductDescription().contains(description)) {
                searchResults.add(tempMarket.get(i).getProductName() + ", Store: " + tempMarket.get(i).getStoreName());
            }
        }

        return searchResults;
    }


    //returns a string list of item info ordered from greatest to lowest price, also prints to terminal in that order
    public ArrayList<String> sortMarketPrice(ArrayList<Item> tempMarket) {
        ArrayList<String> priceSortedMarket = new ArrayList<String>();
        while (tempMarket.size() > 0) {

            int highestPrice = 0;
            int highestPriceIndex = 0;
            for (int i = 0; i < tempMarket.size(); i ++) {
                if (tempMarket.get(i).getPrice() > highestPrice) {
                    highestPrice = tempMarket.get(i).getQuantityAvailable();
                    highestPriceIndex = i;
                }
            }
            priceSortedMarket.add(tempMarket.get(highestPriceIndex).getProductName() + ", Store: " +
                tempMarket.get(highestPriceIndex).getStoreName() + ", Price: " +
                tempMarket.get(highestPriceIndex).getPrice());
            tempMarket.remove(highestPriceIndex);
        }
        return priceSortedMarket;
    }

    //returns a string list of item info ordered from greatest to lowest quantity, also prints to terminal in that order
    public ArrayList<String> sortMarketQuantity(ArrayList<Item> tempMarket) {

        ArrayList<String> quantitySortedMarket = new ArrayList<String>();
        while (tempMarket.size() > 0) {

            int highestQuantity = 0;
            int highestQuantityIndex = 0;
            for (int i = 0; i < tempMarket.size(); i ++) {
                if (tempMarket.get(i).getQuantityAvailable() > highestQuantity) {
                    highestQuantity = tempMarket.get(i).getQuantityAvailable();
                    highestQuantityIndex = i;
                }
            }

            quantitySortedMarket.add(tempMarket.get(highestQuantityIndex).getProductName() + ", Store: " +
                tempMarket.get(highestQuantityIndex).getStoreName() + ", Quantity: " +
                tempMarket.get(highestQuantityIndex).getQuantityAvailable());
            tempMarket.remove(highestQuantityIndex);
        }
        return quantitySortedMarket;
    }

    // one of the selections, customer can view the dashboard. the parameter
    // will be user input
    public void viewDashboard(String option) {
        while (true) {
            if (option.equals("Yes") || option.equals("yes")) {
                // TODO: print the dashboard if the customer says yes

                break;
            } else if (option.equals("No") || option.equals("no")) {
                System.out.println("You chose not to print the dashboard.");
                break;
            } else {
                System.out.println("Please enter Yes or No! Would you like to view the dashboard?");
            }
        }
    }

    // customer can choose to sort the dashboard. parameter is the same
    public void sortDashboard(String option) {
        while (true) {
            if (option.equals("Yes") || option.equals("yes")) {
                // TODO: sort and print the dashboard if the customer says yes

                break;
            } else if (option.equals("No") || option.equals("no")) {
                System.out.println("You chose not to sort the dashboard.");
                break;
            } else {
                System.out.println("Please enter Yes or No! Would you like to sort the dashboard?");
            }
        }
    }

    public ArrayList<String> purchaseHistory(String customerName) {
        ArrayList<String> purchaseHistory = new ArrayList<>();
        File file = new File(customerName + "_History.txt");
        try {
            if (!file.exists()) {
                System.out.println("You have not made any purchases yet.");
            } else {
                BufferedReader bfr = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bfr.readLine()) != null) {
                    purchaseHistory.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return purchaseHistory;
    }
    public String dashboard(String sortField, String order) {
        LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();
        String content = "";
        File file = new File(username + "_History.txt");
        try {
            if (!file.exists()) {
                System.out.println("You have not made any purchases yet.");
            } else {
                BufferedReader bfr = new BufferedReader(new FileReader(file));
                String line;
                String sellerName = "";
                while ((line = bfr.readLine()) != null) {
                    if (line.contains("Seller:")) {
                        sellerName = line.split(" ")[1];
                    }
                    content += line + "\n";
                    if (line.contains("Total price:")) {
                        dataMap.put(sellerName, content);
                        content = "";
                    }
                }
                if (sortField.equals("")) {
                    content = "";
                    for (String key : dataMap.keySet()) {
                        content += dataMap.get(key) + "\n";
                    }
                } else {
                    content = "";
                    Map<String, String> tMap;
                    if (order.equals("desc")) {
                        tMap = new TreeMap<>(Collections.reverseOrder());
                        tMap.putAll(dataMap);
                    } else {
                        tMap = new TreeMap<>(dataMap);
                    }
                    for (String key : tMap.keySet()) {
                        content += dataMap.get(key) + "\n";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public void printToFile() {
        try {
            File file = new File(username + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bfw = new BufferedWriter(new FileWriter(file, false));
            bfw.write("CUSTOMER," + username + "," + password + "\n");
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
