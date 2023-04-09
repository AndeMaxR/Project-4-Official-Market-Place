import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * For CSV files we will format all item strings as productName,storeName,productDescription,quantityAvailable,price
 * No spaces in between to make things simple
 *
 * We don't really need to add anything to this, if there needs to be an altercation done do it in the seller class
 *
 */
public class Item {
    private String productName;
    private String storeName;
    private String productDescription;
    private int quantityAvailable;
    private double price;


    //Initializes a new Item
    public Item(String productName, String storeName, String productDescription, int quantityAvailable, double price) {
        this.productName = productName;
        this.storeName = storeName;
        this.productDescription = productDescription;
        this.quantityAvailable = quantityAvailable;
        this.price = price;
    }

    //Gets the product name
    public String getProductName() {
        return productName;
    }

    //Gets the store name that the product is associated with
    public String getStoreName() {
        return storeName;
    }

    //Gets the product description
    public String getProductDescription() {
        return productDescription;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    //Use this for general text
    public String toString() {
        String str = String.format("Product Name: %s, Store Name: %s, Description: %s" + ", Quantity Available: %d" +
                ", Price: $%.2f", productName, storeName, productDescription, quantityAvailable, price);
        return str;
    }

    //Use this for CSV's
    public String toStringFile() {
        String str = String.format("%s,%s,%s,%d,%f\n", productName, storeName, productDescription, quantityAvailable,
                price);
        return str;
    }

    public void printToFile() {
        try {
            File file = new File(storeName + "_" + productName + ".txt");
            if (!file.exists()) { file.createNewFile(); }
            BufferedWriter bfr = new BufferedWriter(new FileWriter(file, false));
            bfr.write(toStringFile());
            bfr.flush();
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}