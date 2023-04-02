import java.util.ArrayList;
import java.util.Scanner;
public class Seller {
    private String username;
    private String store;
    private ArrayList<Item> items;

    public Seller(String username, String store, ArrayList<Item> items) {
        this.username = username;
        this.store = store;
        this.items = items;
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

    // create
    public void createProduct() {
        // initialize scanner object, ask for all of the attributes of the new item
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the new product name:");
        String newPName = scan.nextLine();
        System.out.println("Enter the store name:");
        String newSName = scan.nextLine();
        System.out.println("Enter the new product description:");
        String newPDesc = scan.nextLine();
        System.out.println("Enter the amount available:");
        String available = scan.nextLine();
        int avail = Integer.parseInt(available);
        System.out.println("Enter the price:");
        String newPrice = scan.nextLine();
        double dPrice = Double.parseDouble(newPrice);

        // make a new item object with these inputs
        Item item = new Item(newPName, newSName, newPDesc, avail, dPrice);

        // add the item to the items arraylist
        items.add(item);
    }

    // TO DO: EDIT PRODUCT METHOD
    public void editProduct() {

    }

    public void deleteProduct(int index) {
        items.remove(index);
    }

    // TO DO: VIEW SALES METHOD
    public void viewSales() {

    }

}
