import javax.swing.*;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.Buffer;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

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
                        JOptionPane.showMessageDialog(null,
                                "There are currently no items for sale! Returning to main menu.",
                                "Empty market", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null,
                    "There are currently no items for sale! Returning to main menu.",
                    "Empty market", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean viewMarketNameSort(String name) {
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
                        System.out.println("There are currently no items for sale!");
                        return false;
                    }
                    break;
                }
                if (fileContent.contains(name)) {
                    System.out.println(fileContent);
                }
                counter++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("There are currently no items for sale!");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean viewMarketStoreSort(String storeName) {
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
                        System.out.println("There are currently no items for sale!");
                        return false;
                    }
                    break;
                }
                if (fileContent.contains(storeName)) {
                    System.out.println(fileContent);
                }
                counter++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("There are currently no items for sale!");
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean sortDescription(String description) {
        ArrayList<String> sortedByDescription = new ArrayList<String>();
        try {
            int items = 0;
            BufferedReader br = new BufferedReader(new FileReader("ItemMasterList.txt"));
            String line;
            // iterate through the file and add each line to the temp list
            while ((line = br.readLine()) != null) {
                sortedByDescription.add(line);
                items++;
            }
            // check if there are any items in the file
            if (items == 0) {
                System.out.println("There are currently no items for sale!");
                return false;
            }
            // check each line if the description, and print if it contains the search
            for (String s : sortedByDescription) {
                if (s.contains(description)) {
                    System.out.println(s);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find ItemMasterList.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    //returns a string list of item info ordered from greatest to lowest price, also prints to terminal in that order
    public void sortMarketPrice() {
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
                System.out.println("There are currently no items for sale!");
            }
            //iterates through tempMarket, searches for the highest price item, adds that item to sorted market list,
            // deletes that item from temp market
            String[] itemInfo;
            while (tempMarket.size() > 0) {
                double highestPrice = 0;
                int highestPriceIndex = 0;
                for (int i = 0; i < tempMarket.size(); i ++) {
                    itemInfo = tempMarket.get(i).split(",");
                    if (Double.parseDouble(itemInfo[4].substring(9)) > highestPrice) {
                        highestPrice = Double.parseDouble(itemInfo[4].substring(9));
                        highestPriceIndex = i;
                    }
                }
                System.out.println(tempMarket.get(highestPriceIndex));
                priceSortedMarket.add(tempMarket.get(highestPriceIndex));
                tempMarket.remove(highestPriceIndex);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find ItemMasterList.txt");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //returns a string list of item info ordered from greatest to lowest quantity, also prints to terminal in that order
    public void sortMarketQuantity() {
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
                System.out.println("There are currently no items for sale!");
            }
            //iterates through tempMarket, searches for the highest quantity item, adds that item to sorted market list,
            // deletes that item from temp market
            String[] itemInfo;
            while (tempMarket.size() > 0) {
                int highestQuantity = 0;
                int highestQuantityIndex = 0;
                for (int i = 0; i < tempMarket.size(); i ++) {
                    itemInfo = tempMarket.get(i).split(",");
                    if (Integer.parseInt(itemInfo[3].substring(21)) > highestQuantity) {
                        highestQuantity = Integer.parseInt(itemInfo[3].substring(21));
                        highestQuantityIndex = i;
                    }
                }
                System.out.println(tempMarket.get(highestQuantityIndex));
                quantitySortedMarket.add(tempMarket.get(highestQuantityIndex));
                tempMarket.remove(highestQuantityIndex);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find ItemMasterList.txt");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void purchaseHistory(String customerName) {
        File file = new File(customerName + "_History.txt");
        try {
            if (!file.exists()) {
                System.out.println("You have not made any purchases yet.");
            } else {
                BufferedReader bfr = new BufferedReader(new FileReader(file));
                String line;
                while ((line = bfr.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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