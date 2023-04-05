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


    //TODO: Sort by description.


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
        return quantitySortedMarket;
    }

}
