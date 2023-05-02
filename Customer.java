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
 * Market Client
 *
 * This class handles the Customer
 *
 * @version 5/2/2023
 * @author Colin, Max A, Mark, Bomma
 */
public class Customer {
    private String username;
    private String password;

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

    public ArrayList<String> viewMarketNameSearch(String name) {
        ArrayList<String> searchResults = new ArrayList<String>();
        try {
            File masterList = new File("ItemMasterList.txt");
            if (!masterList.exists()) {
                masterList.createNewFile();
            }
            BufferedReader bfr = new BufferedReader(new FileReader(masterList));
            String fileContent;
            int counter = 0;

            while (true) {
                fileContent = bfr.readLine();
                if (fileContent == null) {
                    if (counter == 0) {
                        JOptionPane.showMessageDialog(null,
                                "There are currently no items for sale!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
                if (fileContent.contains(name)) {
                    searchResults.add(fileContent);
                }
                counter++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find ItemMasterList.txt");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    public ArrayList<String> viewMarketStoreSearch(String storeName) {
        ArrayList<String> searchResults = new ArrayList<String>();
        try {
            File masterList = new File("ItemMasterList.txt");
            if (!masterList.exists()) {
                masterList.createNewFile();
            }
            BufferedReader bfr = new BufferedReader(new FileReader(masterList));
            String fileContent;
            int counter = 0;

            while (true) {
                fileContent = bfr.readLine();
                if (fileContent == null) {
                    if (counter == 0) {
                        JOptionPane.showMessageDialog(null,
                                "There are currently no stores selling!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
                if (fileContent.contains(storeName)) {
                    searchResults.add(fileContent);
                }
                counter++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find ItemMasterList.txt");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    public ArrayList<String> viewMarketDescriptionSearch(String description) {
        ArrayList<String> searchResults = new ArrayList<String>();
        try {
            File masterList = new File("ItemMasterList.txt");
            if (!masterList.exists()) {
                masterList.createNewFile();
            }
            BufferedReader bfr = new BufferedReader(new FileReader(masterList));
            String fileContent;
            int counter = 0;

            while (true) {
                fileContent = bfr.readLine();
                if (fileContent == null) {
                    if (counter == 0) {
                        JOptionPane.showMessageDialog(null,
                                "There are currently no items for sale!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
                if (fileContent.contains(description)) {
                    searchResults.add(fileContent);
                }
                counter++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find ItemMasterList.txt");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }


    //returns a string list of item info ordered from greatest to lowest price, also prints to terminal in that order
    public ArrayList<String> sortMarketPrice() {
        ArrayList<String> priceSortedMarket = new ArrayList<String>();
        ArrayList<String> tempMarket = new ArrayList<String>();
        try {
            int numItems = 0;
            BufferedReader bfr = new BufferedReader(new FileReader((new File("ItemMasterList.txt"))));
            String line;
            //copies marketplace into temporary marketplace variable
            while ((line = bfr.readLine()) != null) {
                tempMarket.add(line);
                numItems++;
            }
            if (numItems == 0) {
                JOptionPane.showMessageDialog(null,
                        "There are currently no items for sale!", "Error", JOptionPane.ERROR_MESSAGE);
            }


            //iterates through tempMarket, searches for the highest price item, adds that item to sorted market list,
            // deletes that item from temp market
            String[] itemInfo;
            while (tempMarket.size() > 0) {
                double highestPrice = 0;
                int highestPriceIndex = 0;
                for (int i = 0; i < tempMarket.size(); i++) {
                    itemInfo = tempMarket.get(i).split(",");
                    if (Double.parseDouble(itemInfo[4].substring(9)) > highestPrice) {
                        highestPrice = Double.parseDouble(itemInfo[4].substring(9));
                        highestPriceIndex = i;
                    }
                }
                priceSortedMarket.add(tempMarket.get(highestPriceIndex));
                tempMarket.remove(highestPriceIndex);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find ItemMasterList.txt");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return priceSortedMarket;
    }

    //returns a string list of item info ordered from greatest to lowest quantity, also prints to terminal in that order
    public ArrayList<String> sortMarketQuantity() {
        ArrayList<String> quantitySortedMarket = new ArrayList<String>();
        ArrayList<String> tempMarket = new ArrayList<String>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader((new File("ItemMasterList.txt"))));
            String line;
            //copies marketplace into temporary marketplace variable
            int counter = 0;
            while ((line = bfr.readLine()) != null) {
                tempMarket.add(line);
                counter++;
            }
            if (counter == 0) {
                JOptionPane.showMessageDialog(null,
                        "There are currently no items for sale!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            //iterates through tempMarket, searches for the highest quantity item, adds that item to sorted market list,
            // deletes that item from temp market
            String[] itemInfo;
            while (tempMarket.size() > 0) {
                int highestQuantity = 0;
                int highestQuantityIndex = 0;
                for (int i = 0; i < tempMarket.size(); i++) {
                    itemInfo = tempMarket.get(i).split(",");
                    if (Integer.parseInt(itemInfo[3].substring(21)) > highestQuantity) {
                        highestQuantity = Integer.parseInt(itemInfo[3].substring(21));
                        highestQuantityIndex = i;
                    }
                }

                quantitySortedMarket.add(tempMarket.get(highestQuantityIndex));
                tempMarket.remove(highestQuantityIndex);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find ItemMasterList.txt");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quantitySortedMarket;
    }

    // one of the selections, customer can view the dashboard. the parameter
    // will be user input
    public void viewDashboard(String option) {
        while (true) {
            if (option.equals("Yes") || option.equals("yes")) {
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
