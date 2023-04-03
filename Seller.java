import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;


public class Seller {
    private String username;
    private String store;
    private ArrayList<Item> items;
    private ArrayList<String> storeList;

    public Seller(String username, String store, ArrayList<Item> items, ArrayList<String> storeList) {
        this.username = username;
        this.store = store;
        this.items = items;
        this.storeList = storeList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<String> getStoreList() {
        return storeList;
    }

    public void setStoreList(ArrayList<String> storeList) {
        this.storeList = storeList;
    }

    // creates a new item object
    public void createProduct() {
        // initialize scanner object, ask for all of the attributes of the new item
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the new product name:");
        String newPName = scan.nextLine();
        System.out.println("Enter the store name:");
        String newSName = scan.nextLine();
        String newPDesc;
        do {
            System.out.println("Enter the new product description:");
            newPDesc = scan.nextLine();
            if (newPDesc.length() > 200) {
                System.out.println("Invalid Description: Product description must be less than 200 characters!");
            }
        } while (newPDesc.length() > 200);

        System.out.println("Enter the amount available:");
        String available = scan.nextLine();
        int numAvail = Integer.parseInt(available);
        System.out.println("Enter the price:");
        String newPrice = scan.nextLine();
        double dPrice = Double.parseDouble(newPrice);

        // make a new item object with these inputs
        Item item = new Item(newPName, newSName, newPDesc, numAvail, dPrice);

        // add the item to the items arraylist
        items.add(item);
    }

    //edits the item inputted in the parameter
    public void editProduct(Item item) {
        Scanner scan = new Scanner(System.in);
        boolean choiceValid = true;
        do {
            System.out.println("What feature would you like to edit?" +
                    "\n1. Product Name" +
                    "\n2. Product Description" +
                    "\n3. Quantity available " +
                    "\n4. Product Price");
            int editChoice = scan.nextInt();
            scan.nextLine();

            switch (editChoice) {
                case 1:
                    System.out.println("What would you like to change the product name to?");
                    String newName = scan.nextLine();
                    item.setProductName(newName);
                    break;
                case 2:
                    String newDesc;
                    do {
                        System.out.println("What would you like to change the product description to?");
                        newDesc = scan.nextLine();
                        if (newDesc.length() > 200) {
                            System.out.println("Invalid Description: Product description must be less than 200 characters!");
                        }
                    } while (newDesc.length() > 200);
                    break;
                case 3:
                    System.out.println("What would you like to change the available quantity to?");
                    int newAvailQuant = scan.nextInt();
                    scan.nextLine();
                    item.setQuantityAvailable(newAvailQuant);
                    break;
                case 4:
                    System.out.println("What would you like to change the items price to?");
                    double newPrice = scan.nextDouble();
                    scan.nextLine();
                    item.setPrice(newPrice);
                    break;
                default:
                    choiceValid = false;
                    System.out.println("Invalid input: Please type an integer between 1 and 4.");
                    break;
            }
        } while (!choiceValid);
    }

    public void deleteProduct(int index) {
        items.remove(index);
    }

    // TO DO: VIEW SALES METHOD
    public void viewSales() throws FileNotFoundException, IOException {
        Scanner scan = new Scanner(System.in);
        int storeChoice;
        do {
            System.out.println("Which store would you like to view the sales of?");
            for (int i = 0; i < storeList.size(); i++) {
                System.out.println(i + 1 + ". " + storeList.get(i));
            }
            storeChoice = scan.nextInt();
            scan.nextLine();
            if (!(storeChoice > 0 && storeChoice < storeList.size())) {
                System.out.println("Invalid input: please input an integer from the menu below.");
            }
        } while (!(storeChoice > 0 && storeChoice < storeList.size()));

        try {
            BufferedReader bfr = new BufferedReader(new FileReader(storeList.get(storeChoice) + "txt"));

            String line = bfr.readLine();

            if (line == null) {
                System.out.println("This store has not made any sales yet.");
            }

            //this loop will print out the stats of the store based on how the store is formatted.
            while (!(line == null)) {
                System.out.println("");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Store file doesn't exist!");
            e.printStackTrace();
        }
    }
}
