import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
/**
 * Market Client
 *
 * This class handles the input from the market
 * @version 5/2/2023
 * @author Colin, Max A, Mark, Bomma
 */
public class ClientHandler extends Thread {
    final PrintWriter printWriter;
    final BufferedReader bufferedReader;
    final Socket socket;

    public static final Object OBJ = new Object();

    private String username;
    private String password;

    private ArrayList<Store> storeMasterArrayList;

    private Seller seller;
    private Customer customer;


    public ClientHandler(PrintWriter printWriter, BufferedReader bufferedReader, Socket socket,
                         ArrayList<Store> storeMasterArrayList) {
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        this.socket = socket;
        this.storeMasterArrayList = storeMasterArrayList;
    }

    @Override
    public void run() {
        while (true) {
            String temp;
            try {
                temp = bufferedReader.readLine();
                if (temp.contains("LOGIN")) {
                    login(temp);
                } else if (temp.contains("SIGNUP")) {
                    signup(temp);
                }
            } catch (Exception e) {
                //Ignore red here, I want it to stop the current thread that threw the exception
                e.printStackTrace();
                //IGNORE THIS RED MARKER, IT"S MARKED FOR DELETION, BUT SATISFIES WHAT WE NEED IT FOR
                currentThread().stop();
                throw new RuntimeException(e);
            }
        }
    }

    public void login(String string) {
        String[] list = string.split(",");
        File file = new File(list[1] + ".txt");
        if (!file.exists()) {
            synchronized (OBJ) {
                printWriter.write("-1\n");
                printWriter.flush();
            }
        } else {
            try {
                String temp;
                synchronized (OBJ) {
                    BufferedReader bfr = new BufferedReader(new FileReader(file));
                    temp = bfr.readLine();
                    bfr.close();

                }
                if (temp == null) {
                    synchronized (OBJ) {
                        printWriter.write("-1\n");
                        printWriter.flush();
                    }
                } else if (temp.contains(list[2])) {
                    username = list[1];
                    password = list[2];
                    synchronized (OBJ) {
                        printWriter.write("1\n");
                        printWriter.flush();
                        if (temp.contains("SELLER")) {
                            seller = new Seller(username, password);
                            printWriter.write("SELLER\n");
                            printWriter.flush();
                            seller();
                        } else {
                            customer = new Customer(username, password);
                            printWriter.write("CUSTOMER\n");
                            printWriter.flush();
                            customer();
                        }

                    }
                } else {
                    synchronized (OBJ) {
                        printWriter.write("-1\n");
                        printWriter.flush();
                    }
                }
            } catch (IOException e) {
                synchronized (OBJ) {
                    printWriter.write("-1\n");
                    printWriter.flush();
                }
                e.printStackTrace();
            }
        }
    }

    public void signup(String string) {
        String[] list = string.split(",");
        File file = new File(list[1] + ".txt");
        if (file.exists()) {
            synchronized (OBJ) {
                printWriter.write("-1\n");
                printWriter.flush();
            }
        } else {
            username = list[1];
            password = list[2];
            if (list[3].equals("Seller")) {
                this.seller = new Seller(username, password);
                synchronized (OBJ) {
                    printWriter.write("1\n");
                    printWriter.flush();
                    seller();
                }
            } else {
                this.customer = new Customer(username, password);
                synchronized (OBJ) {
                    printWriter.write("1\n");
                    printWriter.flush();
                    customer();
                }
            }
        }
    }

    public void customer() {
        while (true) {
            try {
                String temp = bufferedReader.readLine();
                if (temp.equals("viewMarket")) {
                    printWriter.write(storeMasterArrayList.size() + "\n");
                    printWriter.flush();
                    for (int i = 0; i < storeMasterArrayList.size(); i++) {
                        printWriter.write(storeMasterArrayList.get(i).getStoreName() + "\n");
                        printWriter.flush();
                        printWriter.write(storeMasterArrayList.get(i).getItemList() + "\n");
                        printWriter.flush();
                    }
                    String viewMarketChoice = bufferedReader.readLine();
                    System.out.println(viewMarketChoice);
                    if (viewMarketChoice.equals("searchMarket")) {
                        int searchType = Integer.parseInt(bufferedReader.readLine());
                        if (searchType == 0) { //search by item name
                            String searchPrompt = bufferedReader.readLine();
                            synchronized (OBJ) {
                                ArrayList<String> searchResults = customer.viewMarketNameSearch(searchPrompt);
                                printWriter.write(searchResults.size() + "\n");
                                printWriter.flush();
                                for (int i = 0; i < searchResults.size(); i++) {
                                    printWriter.write(searchResults.get(i) + "\n");
                                    printWriter.flush();
                                }
                            }
                        } else if (searchType == 1) { //search by store name
                            String searchPrompt = bufferedReader.readLine();
                            synchronized (OBJ) {
                                ArrayList<String> searchResults = customer.viewMarketStoreSearch(searchPrompt);
                                printWriter.write(searchResults.size() + "\n");
                                printWriter.flush();
                                for (int i = 0; i < searchResults.size(); i++) {
                                    printWriter.write(searchResults.get(i) + "\n");
                                    printWriter.flush();
                                }
                            }
                        } else if (searchType == 2) {
                            String searchPrompt = bufferedReader.readLine();
                            synchronized (OBJ) {
                                ArrayList<String> searchResults = customer.viewMarketDescriptionSearch(searchPrompt);
                                printWriter.write(searchResults.size() + "\n");
                                printWriter.flush();
                                for (int i = 0; i < searchResults.size(); i++) {
                                    printWriter.write(searchResults.get(i) + "\n");
                                    printWriter.flush();
                                }
                            }
                        }
                    }
                    if (viewMarketChoice.equals("sortMarket")) {
                        int sortType = Integer.parseInt(bufferedReader.readLine());
                        if (sortType == 0) { // sort by price
                            synchronized (OBJ) {
                                ArrayList<String> sortResults = customer.sortMarketPrice();
                                printWriter.write(sortResults.size() + "\n");
                                printWriter.flush();
                                for (int i = 0; i < sortResults.size(); i++) {
                                    printWriter.write(sortResults.get(i) + "\n");
                                    printWriter.flush();
                                }
                            }
                        } else if (sortType == 1) { //sort by quantity
                            synchronized (OBJ) {
                                ArrayList<String> sortResults = customer.sortMarketQuantity();
                                printWriter.write(sortResults.size() + "\n");
                                printWriter.flush();
                                for (int i = 0; i < sortResults.size(); i++) {
                                    printWriter.write(sortResults.get(i) + "\n");
                                    printWriter.flush();
                                }
                            }
                        }
                    }
                    if (viewMarketChoice.equals("purchase")) {
                        int storeLocation;
                        int itemIndex;
                        int purchaseQuantity;
                        String usernameTemp;
                        storeLocation = Integer.parseInt(bufferedReader.readLine());
                        itemIndex = Integer.parseInt(bufferedReader.readLine());
                        purchaseQuantity = Integer.parseInt(bufferedReader.readLine());
                        usernameTemp = bufferedReader.readLine();
                        System.out.println(storeLocation);
                        System.out.println(itemIndex);
                        System.out.println(purchaseQuantity);
                        System.out.println(usernameTemp);
                        synchronized (OBJ) {
                            storeMasterArrayList.get(storeLocation).buyItem(itemIndex, purchaseQuantity, usernameTemp);
                        }
                    }
                } else if (temp.equals("ViewPurchaseHistory")) {
                    ArrayList<String> purchaseHistory = new ArrayList<>();
                    String usernameTemp = bufferedReader.readLine();
                    purchaseHistory = customer.purchaseHistory(usernameTemp);
                    synchronized (OBJ) {
                        printWriter.write(purchaseHistory.size() + "\n");
                        printWriter.flush();
                        for (int i = 0; i < purchaseHistory.size(); i++) {
                            printWriter.write(purchaseHistory.get(i) + "\n");
                            printWriter.flush();
                        }
                    }
                } else if (temp.equals("Seller name Asc")) {
                    try {
                        synchronized (OBJ) {
                            String data = customer.dashboard("seller name", "asc").replace("\n", "#");
                            printWriter.write(data + "\n");
                            printWriter.flush();
                        }
                    } catch (Exception e) {
                        synchronized (OBJ) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }
                } else if (temp.equals("Seller name Desc")) {
                    try {
                        synchronized (OBJ) {
                            String data = customer.dashboard("seller name", "desc").replace("\n", "#");
                            printWriter.write(data + "\n");
                            printWriter.flush();
                        }
                    } catch (Exception e) {
                        synchronized (OBJ) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }

                } else if (temp.equals("No Sort")) {
                    try {
                        synchronized (OBJ) {
                            String data = customer.dashboard("", "").replace("\n", "#");
                            printWriter.write(data + "\n");
                            printWriter.flush();
                        }
                    } catch (Exception e) {
                        synchronized (OBJ) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }
                } else if (temp.equals("Logout")) {
                    break;
                }
            } catch (Exception e) {
                currentThread().stop();
                e.printStackTrace();
            }
        }
    }

    public void seller() {
        while (true) {
            try {
                String temp = bufferedReader.readLine();
                if (temp.equals("AddStore")) {
                    try {
                        String storeName = bufferedReader.readLine();
                        synchronized (OBJ) {
                            seller.addStore(new Store(seller.getUsername(), storeName));
                            storeMasterArrayList.add(seller.getSpecificStore(seller.getFullStoreList().size() - 1));
                            printWriter.write("1\n");
                            printWriter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        synchronized (OBJ) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                        }
                    }
                } else if (temp.equals("RemoveStore")) {
                    try {
                        synchronized (OBJ) {
                            printWriter.write(seller.getStoreList() + "\n");
                            printWriter.flush();
                        }

                        int input = Integer.parseInt(bufferedReader.readLine());
                        if (input == -1) {
                            continue;
                        } else {
                            synchronized (OBJ) {
                                for (int i = 0; i < storeMasterArrayList.size(); i++) {
                                    if (storeMasterArrayList.get(i).getStoreName().equals(seller
                                            .getSpecificStore(input).getStoreName())) {
                                        storeMasterArrayList.remove(i);
                                        break;
                                    }
                                }
                                try {
                                    ArrayList<String> list = new ArrayList<>();
                                    File file = new File("StoreMasterList.txt");
                                    BufferedReader bfr = new BufferedReader(new FileReader(file));
                                    String temp2 = "";
                                    while (true) {
                                        temp2 = bfr.readLine();
                                        if (temp2 == null) {
                                            bfr.close();
                                            break;
                                        } else if (!temp2.contains(seller.getSpecificStore(input).getStoreName())) {
                                            list.add(temp2);
                                        }
                                    }
                                    BufferedWriter bfw = new BufferedWriter(new FileWriter(file, false));
                                    for (int i = 0; i < list.size(); i++) {
                                        bfw.write(list.get(i) + "\n");
                                    }
                                    bfw.flush();
                                    bfw.close();
                                } catch (Exception k) {
                                    k.printStackTrace();
                                }
                                synchronized (OBJ) {
                                    seller.removeStore(input);
                                    printWriter.write("1\n");
                                    printWriter.flush();
                                }
                            }
                        }
                    } catch (Exception e) {
                        synchronized (OBJ) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }

                } else if (temp.equals("ViewFinances")) {
                    try {
                        synchronized (OBJ) {
                            for (int i = 0; i < seller.getFullStoreList().size(); i++) {
                                Store store = seller.getSpecificStore(i);
                                printWriter.write(store.getStoreName() + "\n" + store.getFinances());
                                printWriter.flush();
                            }
                            printWriter.write("-1\n");
                            printWriter.flush();
                        }
                    } catch (Exception e) {
                        synchronized (OBJ) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }
                } else if (temp.equals("AddItem")) {
                    try {
                        String holder = "";
                        String[] itemParts;
                        holder = bufferedReader.readLine();
                        itemParts = holder.split(",");
                        synchronized (OBJ) {
                            for (int i = 0; i < seller.getFullStoreList().size(); i++) {
                                if (seller.getSpecificStore(i).getStoreName().equals(itemParts[1])) {
                                    seller.getSpecificStore(i).addItem(new Item(itemParts[0], itemParts[1],
                                            itemParts[2], Integer.parseInt(itemParts[3]),
                                            Double.parseDouble(itemParts[4])));
                                    for (int j = 0; j < storeMasterArrayList.size(); j++) {
                                        System.out.println(storeMasterArrayList.get(j).getStoreName());
                                        if (storeMasterArrayList.get(j).getStoreName().equals(itemParts[1])) {
                                            //HIGH CHANCE OF BUG
                                            storeMasterArrayList.set(j, seller.getSpecificStore(i));
                                            printWriter.write("1\n");
                                            printWriter.flush();
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        synchronized (OBJ) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }
                } else if (temp.equals("RemoveItem")) {
                    synchronized (OBJ) {
                        printWriter.write(seller.getStoreList() + "\n");
                        printWriter.flush();

                        for (int i = 0; i < seller.getFullStoreList().size(); i++) {
                            printWriter.write(seller.getSpecificStore(i).getItemList() + "\n");
                            printWriter.flush();
                        }
                    }
                    String temp2;
                    temp2 = bufferedReader.readLine();
                    synchronized (OBJ) {
                        if (!temp2.equals("cancel")) {
                            String[] components = temp2.split(",");
                            for (int i = 0; i < seller.getFullStoreList().size(); i++) {
                                if (seller.getSpecificStore(i).getStoreName().equals(components[0])) {
                                    for (int j = 0; j < seller.getSpecificStore(i).getItemListSize(); j++) {
                                        if (seller.getSpecificStore(i).getItem(j).getProductName()
                                                .equals(components[1])) {
                                            if (seller.getSpecificStore(i).removeItem(j)) {
                                                for (int k = 0; k < storeMasterArrayList.size(); k++) {
                                                    if (storeMasterArrayList.get(k).getStoreName()
                                                            .equals(components[0])) {
                                                        storeMasterArrayList.set(k, seller.getSpecificStore(i));
                                                        break;
                                                    }
                                                }
                                                printWriter.write("1\n");
                                                printWriter.flush();
                                            } else {
                                                printWriter.write("-1\n");
                                                printWriter.flush();
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }

                } else if (temp.equals("EditItem")) {
                    synchronized (OBJ) {
                        printWriter.write(seller.getStoreList() + "\n");
                        printWriter.flush();

                        for (int i = 0; i < seller.getFullStoreList().size(); i++) {
                            printWriter.write(seller.getSpecificStore(i).getItemList() + "\n");
                            printWriter.flush();
                        }
                    }
                    String temp2;
                    String temp3;
                    temp2 = bufferedReader.readLine();
                    if (!temp2.equals("cancel")) {
                        temp3 = bufferedReader.readLine();
                        synchronized (OBJ) {
                            String[] components = temp2.split(",");
                            String[] components2 = temp3.split(",");
                            for (int i = 0; i < seller.getFullStoreList().size(); i++) {
                                if (seller.getSpecificStore(i).getStoreName().equals(components[0])) {
                                    for (int j = 0; j < seller.getSpecificStore(i).getItemListSize(); j++) {
                                        if (seller.getSpecificStore(i).getItem(j).getProductName()
                                                .equals(components[1])) {
                                            seller.getSpecificStore(i).editItem(j, components2[0], components2[1],
                                                    components2[2], Integer.parseInt(components2[3]),
                                                    Double.parseDouble(components2[4]));

                                            for (int k = 0; k < storeMasterArrayList.size(); k++) {
                                                if (storeMasterArrayList.get(k).getStoreName().equals(components[0])) {
                                                    storeMasterArrayList.set(k, seller.getSpecificStore(i));
                                                    break;
                                                }
                                            }
                                            printWriter.write("1\n");
                                            printWriter.flush();
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } else if (temp.equals("Store name Asc")) {
                    try {
                        synchronized (OBJ) {
                            String data = seller.dashboard("store name", "asc").replace("\n",
                                    "#");
                            printWriter.write(data + "\n");
                            printWriter.flush();
                        }
                    } catch (Exception e) {
                        synchronized (OBJ) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }
                } else if (temp.equals("Store name Desc")) {
                    try {
                        synchronized (OBJ) {
                            String data = seller.dashboard("store name", "desc").replace("\n",
                                    "#");
                            printWriter.write(data + "\n");
                            printWriter.flush();
                        }
                    } catch (Exception e) {
                        synchronized (OBJ) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }
                } else if (temp.equals("No Sort")) {
                    try {
                        synchronized (OBJ) {
                            String data = seller.dashboard("", "").replace("\n", "#");
                            printWriter.write(data + "\n");
                            printWriter.flush();
                        }
                    } catch (Exception e) {
                        synchronized (OBJ) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }

                } else if (temp.equals("Logout")) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                //IGNORE THIS RED MARKER, IT"S MARKED FOR DELETION, BUT SATISFIES WHAT WE NEED IT FOR
                currentThread().stop();
                throw new RuntimeException(e);
            }
        }
    }
}