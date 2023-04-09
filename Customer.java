import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.Buffer;
import java.util.ArrayList;

public class Customer {
    private String username;
    private String password;

    public Customer(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void viewMarket() {
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
                    }
                    break;
                } else {
                    System.out.println(fileContent);
                }
                counter++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("There are currently no items for sale!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewMarketNameSort(String name) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewMarketStoreSort(String storeName) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortDescription(String description) {
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
                    if (Double.parseDouble(itemInfo[4]) > highestPrice) {
                        highestPrice = Double.parseDouble(itemInfo[4]);
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
                    if (Integer.parseInt(itemInfo[3]) > highestQuantity) {
                        highestQuantity = Integer.parseInt(itemInfo[3]);
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

    public void purchaseHistory() {
        //TODO: create a File for each new customer with the formatting as shown in customer.txt
    }

}
