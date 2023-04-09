import java.io.*;
import java.util.ArrayList;
//owner + "_" + this.storeName + ".txt" is file format name

public class Store {
    private String owner;
    private String storeName;
    private int numberOfSales;
    private double revenue;
    private String filename;
    private ArrayList<Item> items;

    public Store(String owner, String storeName) {
        this.owner = owner;
        this.storeName = storeName;
        this.items = new ArrayList<>();
        try {
            filename = owner + "_" + storeName + ".txt";
            File storeFile = new File(owner + "_" + storeName + ".txt");
            File masterListFile = new File("StoreMasterList.txt");
            File receiptListFile = new File( storeName + "_Receipt.txt");

            if (!receiptListFile.exists()) {
                receiptListFile.createNewFile();
            }
            if (!masterListFile.exists()) {
                masterListFile.createNewFile();
                BufferedWriter bfw = new BufferedWriter(new FileWriter(masterListFile, true));
                bfw.write(owner + "_" + storeName + ".txt\n");
                bfw.flush();
                bfw.close();
            }

            if (storeFile.exists()) {
                String content;
                BufferedReader bfr = new BufferedReader(new FileReader(storeFile));
                content = bfr.readLine();
                content = bfr.readLine();
                numberOfSales = Integer.parseInt(content.substring(0, content.indexOf(",")));
                revenue = Double.parseDouble(content.substring(content.indexOf(",") + 1));
                content = bfr.readLine();
                bfr.close();
                if (content != null) {
                    String[] items = content.split(",");
                    for (int i = 0; i < items.length; i++) {
                        String[] holder;
                        File itemFile = new File(items[i] + ".txt");
                        bfr = new BufferedReader(new FileReader(itemFile));
                        content = bfr.readLine();
                        if (content != null) {
                            holder = content.split(",");
                            this.items.add(new Item(holder[0], holder[1], holder[2],
                                    Integer.parseInt(holder[3]), Double.parseDouble(holder[4])));

                        }
                        bfr.close();
                    }
                }
            } else {
                storeFile.createNewFile();
                numberOfSales = 0;
                revenue = 0;
            }

            printToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOwner(String owner) {
        try {
            ArrayList<String> content = new ArrayList<>();
            File masterListFile = new File("StoreMasterList.txt");
            BufferedReader bfr = new BufferedReader(new FileReader(masterListFile));
            String temp;
            while (true) {
                temp = bfr.readLine();
                if (temp == null) {
                    break;
                }
                if (!temp.equals(this.owner + "_" + storeName + ".txt")) {
                    content.add(temp);
                }
            }
            bfr.close();
            BufferedWriter bfw = new BufferedWriter(new FileWriter("StoreMasterList.txt"));
            for (int i = 0; i < content.size(); i++) {
                bfw.write(content.get(i) + "\n");
            }
            bfw.write(owner + "_" + storeName + ".txt\n");
            bfw.flush();
            bfw.close();


            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            this.owner = owner;
            filename = owner + "_" + storeName + ".txt";
            printToFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStoreName(String storeName) {
        //TODO do the same thing here as up above
        try {
            ArrayList<String> content = new ArrayList<>();
            File masterListFile = new File("StoreMasterList.txt");
            BufferedReader bfr = new BufferedReader(new FileReader(masterListFile));
            String temp;
            while (true) {
                temp = bfr.readLine();
                if (temp == null) {
                    break;
                }
                if (!temp.equals(filename)) {
                    content.add(temp);
                }
            }
            bfr.close();
            BufferedWriter bfw = new BufferedWriter(new FileWriter("StoreMasterList.txt"));
            for (int i = 0; i < content.size(); i++) {
                bfw.write(content.get(i) + "\n");
            }
            bfw.write(owner + "_" + storeName + ".txt\n");
            bfw.flush();
            bfw.close();

            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            this.storeName = storeName;
            filename = owner + "_" + storeName + ".txt";
            printToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilename() {
        return filename;
    }

    public String getOwner() {
        return owner;
    }

    public String getStoreName() {
        return storeName;
    }

    public void addItem(Item item) {
        items.add(item);
        item.printToFile();
        printToFile();
    }

    public boolean removeItem(int index) {
        File file1 = new File(storeName + "_" + items.get(index).getProductName() + ".txt");
        if (file1.exists()) {
            if (!file1.delete()) {
                return false;
            }
        }
        items.remove(index);
        printToFile();
        return true;
    }

    public int getItemListSize() {
        return items.size();
    }
    public Item getItem(int index) {
        return items.get(index);
    }

    public void editItem(int location, String productName, String storeName,
                         String productDescription, int quantityAvailable, double price) {
        items.get(location).setProductName(productName);
        items.get(location).setStoreName(storeName);
        items.get(location).setProductDescription(productDescription);
        items.get(location).setQuantityAvailable(quantityAvailable);
        items.get(location).setPrice(price);
        items.get(location).printToFile();
    }

    public String getItemList() {
        String holder = "";
        for (int i = 0; i < items.size(); i++) {
            holder += (i+1) + ". " + items.get(i).getProductName() + "\n";
        }
        return holder;
    }


    public void buyItem(int itemLocation, int quantity, String customerName) {
        numberOfSales++;
        revenue += quantity * items.get(itemLocation).getPrice();
        items.get(itemLocation).setQuantityAvailable(items.get(itemLocation).getQuantityAvailable() - quantity);
        try {
            File file = new File(storeName + "_Receipt.txt");
            if (file.exists()) {
                BufferedWriter bfw = new BufferedWriter(new FileWriter(file, true));
                bfw.write("--------------------\n");
                bfw.write("Sale # " + numberOfSales + "\n");
                bfw.write("Customer Name: " + customerName + "\n");
                bfw.write("Revenue from sale: " + (quantity * items.get(itemLocation).getPrice()) + "\n");
                bfw.write("Items purchased: " + (items.get(itemLocation).getProductName()) + "\n");
                bfw.flush();
                bfw.close();
            } else {
                file.createNewFile();
                BufferedWriter bfw = new BufferedWriter(new FileWriter(file, true));
                bfw.write("--------------------\n");
                bfw.write("Sale # " + numberOfSales + "\n");
                bfw.write("Customer Name: " + customerName + "\n");
                bfw.write("Revenue from sale: " + (quantity * items.get(itemLocation).getPrice()) + "\n");
                bfw.write("Items purchased: " + (items.get(itemLocation).getProductName()) + "\n");
                bfw.flush();
                bfw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFinances() {
        String holder = "";
        holder = String.format("Total Sales: %d\nTotal Revenue: %.2f\n",numberOfSales,revenue);
        try {
            String contents;
            File receiptListFile = new File(storeName + "_Receipt.txt");
            BufferedReader bfr = new BufferedReader(new FileReader(receiptListFile));
            while (true) {
                contents = bfr.readLine();
                if (contents == null) {
                    break;
                }
                holder += contents + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return holder;
    }

    public void printToFile() {
        try {
            File file = new File(owner + "_" + storeName + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bfw = new BufferedWriter(new FileWriter(file, false));
            bfw.write(owner + "," + storeName + "\n");
            bfw.write(numberOfSales + "," + revenue + "\n");
            for (int i = 0; i < items.size(); i++) {
                if (i == items.size() - 1) {
                    bfw.write(storeName + "_" + items.get(i).getProductName() + "\n");
                } else {
                    bfw.write(storeName + "_" + items.get(i).getProductName() + ",");
                }
            }
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
