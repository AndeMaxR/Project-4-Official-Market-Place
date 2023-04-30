import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    final PrintWriter printWriter;
    final BufferedReader bufferedReader;
    final Socket socket;

    public static final Object obj = new Object();

    private String username;
    private String password;

    private ArrayList<Store> storeMasterArrayList;

    private Seller seller;
    private Customer customer;
    private Store store;

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
            synchronized (obj) {
                printWriter.write("-1\n");
                printWriter.flush();
            }
        } else {
            try {
                String temp;
                synchronized (obj) {
                    BufferedReader bfr = new BufferedReader(new FileReader(file));
                    temp = bfr.readLine();
                    bfr.close();

                }
                if (temp == null) {
                    synchronized (obj) {
                        printWriter.write("-1\n");
                        printWriter.flush();
                    }
                } else if (temp.contains(list[2])) {
                    username = list[1];
                    password = list[2];
                    synchronized (obj) {
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
                    synchronized (obj) {
                        printWriter.write("-1\n");
                        printWriter.flush();
                    }
                }
            } catch (IOException e) {
                synchronized (obj) {
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
            synchronized (obj) {
                printWriter.write("-1\n");
                printWriter.flush();
            }
        } else {
            username = list[1];
            password = list[2];
            if (list[3].equals("Seller")) {
                this.seller = new Seller(username, password);
                synchronized (obj) {
                    printWriter.write("1\n");
                    printWriter.flush();
                    seller();
                }
            } else {
                this.customer = new Customer(username, password);
                synchronized (obj) {
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
                //TODO rest of the client methods should be done below in the else if statements
                if (temp.equals("viewMarket")) {
                    //TODO sorting should also be done inside of here
                    printWriter.write(storeMasterArrayList.size() + "\n");
                    printWriter.flush();
                    for (int i = 0; i < storeMasterArrayList.size(); i++) {
                        printWriter.write(storeMasterArrayList.get(i).getStoreName() + "\n");
                        printWriter.flush();
                        printWriter.write(storeMasterArrayList.get(i).getItemList() + "\n");
                        printWriter.flush();
                    }
                    String viewMarketChoice = bufferedReader.readLine();
                    if (viewMarketChoice.equals("searchMarket")){
                        int searchType = Integer.parseInt(bufferedReader.readLine());
                        if (searchType == 0) { //search by item name
                            String searchPrompt = bufferedReader.readLine();
                            synchronized (obj) {
                                ArrayList<String> searchResults = customer.viewMarketNameSearch(searchPrompt);
                                printWriter.write(searchResults.size());
                                for (int i = 0; i < searchResults.size(); i++) {
                                    printWriter.write(searchResults.get(i));
                                }
                            }
                        } else if (searchType == 1) { //search by store name
                            String searchPrompt = bufferedReader.readLine();
                            synchronized (obj) {
                                ArrayList<String> searchResults = customer.viewMarketStoreSearch(searchPrompt);
                                printWriter.write(searchResults.size());
                                for (int i = 0; i < searchResults.size(); i++) {
                                    printWriter.write(searchResults.get(i));
                                }
                            }
                        } else if (searchType == 2) {
                            String searchPrompt = bufferedReader.readLine();
                            synchronized (obj) {
                                ArrayList<String> searchResults = customer.viewMarketDescriptionSearch(searchPrompt);
                                printWriter.write(searchResults.size());
                                for (int i = 0; i < searchResults.size(); i++) {
                                    printWriter.write(searchResults.get(i));
                                }
                            }
                        }
                    }
                    if (viewMarketChoice.equals("sortMarket")) {
                        int sortType = Integer.parseInt(bufferedReader.readLine());
                        if (sortType == 0) { // sort by price
                            synchronized (obj) {
                                ArrayList<String> sortResults = customer.sortMarketPrice();
                                printWriter.write(sortResults.size());
                                printWriter.write(sortResults.size());
                                for (int i = 0; i < sortResults.size(); i++) {
                                    printWriter.write(sortResults.get(i));
                                }
                            }
                        } else if (sortType == 1) { //sort by quantity
                            synchronized (obj) {
                                ArrayList<String> sortResults = customer.sortMarketQuantity();
                                printWriter.write(sortResults.size());
                                printWriter.write(sortResults.size());
                                for (int i = 0; i < sortResults.size(); i++) {
                                    printWriter.write(sortResults.get(i));
                                }
                            }
                        }
                    }
                    if (viewMarketChoice.equals("purchase")) {
                        int purchaseQuantity = Integer.parseInt(bufferedReader.readLine());
                        int itemIndex = Integer.parseInt(bufferedReader.readLine());
                        String username = bufferedReader.readLine();
                        synchronized (obj) {
                            store.buyItem(itemIndex, purchaseQuantity, username);
                        }
                    }
                } else if (temp.equals("ViewPurchaseHistory")) {
                    ArrayList<String> purchaseHistory = new ArrayList<>();
                    String username = bufferedReader.readLine();
                    purchaseHistory = customer.purchaseHistory(username);
                    printWriter.write(purchaseHistory.size());
                    for (int i = 0; i < purchaseHistory.size(); i++) {
                        printWriter.write(purchaseHistory.get(i));
                    }

                } else if (temp.equals("")) {

                } else if (temp.equals("")) {

                } else if (temp.equals("Logout")) {
                    break;
                }
            } catch (Exception e) {
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
                        seller.addStore(new Store(seller.getUsername(), storeName));
                        synchronized (obj) {
                            storeMasterArrayList.add(seller.getSpecificStore(seller.getFullStoreList().size() - 1));
                            printWriter.write("1\n");
                            printWriter.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        synchronized (obj) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                        }
                    }
                } else if (temp.equals("RemoveStore")) {
                    try {
                        synchronized (obj) {
                            printWriter.write(seller.getStoreList() + "\n");
                            printWriter.flush();
                        }

                        int input = Integer.parseInt(bufferedReader.readLine());
                        if (input == -1) {
                            continue;
                        } else {
                            synchronized (obj) {
                                for (int i = 0; i < storeMasterArrayList.size(); i++) {
                                    if (storeMasterArrayList.get(i).getStoreName().equals(seller.getSpecificStore(input).getStoreName())) {
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
                                synchronized (obj) {
                                    seller.removeStore(input);
                                    printWriter.write("1\n");
                                    printWriter.flush();
                                }
                            }
                        }
                    } catch (Exception e) {
                        synchronized (obj) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }

                } else if (temp.equals("ViewFinances")) {
                    try {
                        synchronized (obj) {
                            for (int i = 0; i < seller.getFullStoreList().size(); i++) {
                                Store store = seller.getSpecificStore(i);
                                printWriter.write(store.getFinances());
                            }
                        }
                    } catch (Exception e) {
                        synchronized (obj) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                            e.printStackTrace();
                        }
                    }
                } else if (temp.equals("AddItem")) {

                } else if (temp.equals("RemoveItem")) {

                }  else if (temp.equals("EditItem")) {

                } else if (temp.equals("Logout")) {
                    break;
                }
                //TODO ADD MORE ELSE IF STATEMENTS TO COMPLETE THE REST OF SELLER
            } catch (Exception e) {
                e.printStackTrace();
                //IGNORE THIS RED MARKER, IT"S MARKED FOR DELETION, BUT SATISFIES WHAT WE NEED IT FOR
                currentThread().stop();
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *  public void customer() {
     *         try {
     *             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
     *             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
     *
     *             int menu1Option = Integer.parseInt(in.readLine());
     *
     *             switch (menu1Option) {
     *                 case 0: //Browse marketplace
     *                     int menu2Option = Integer.parseInt(in.readLine());
     *                     switch (menu2Option) {
     *                         case 0: //View whole marketPlace
     *                             if (!customer.viewMarket()) {
     *                                 return true;
     *                             }
     *                             break;
     *                         case 1: //search marketplace
     *                             //TODO
     *                             break;
     *                         case 2: //SortMarketPlace
     *                             //TODO
     *                             break;
     *                     }
     *                     break;
     *                 case 1: //View Purchase History
     *                     //TODO
     *                     break;
     *                 case 2: //Logout
     *                     //TODO
     *             }
     *
     *         } catch (IOException e) {
     *             e.printStackTrace();
     *         }
     *         return false;
     *     }
     *
     *     public static boolean Seller(Socket socket) {
     *         try {
     *             PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
     *             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
     *             int option = Integer.parseInt(br.readLine());
     *
     *             switch (option) {
     *                 case 1:
     *                     // Manage Store
     *                     String myStr = seller.getStoreList();
     *                     if (myStr.equals("")) {
     *                         // Seller has no stores
     *                         pw.write("Error");
     *                         break;
     *                     } else {
     *                         // Seller has stores, so send message to client so it can display the stores.
     *                         //TODO: CHECK THIS, might need to add stuff to send to the client so it can send info back.
     *                         pw.write("Success");
     *                         // Now get the store they would like to edit
     *                         String newStore = br.readLine();
     *                         int location = Integer.parseInt(newStore);
     *                         if (location == seller.getFullStoreList().size() + 1) {
     *                             pw.write("Cancelling...");
     *                             break;
     *                         } else if (myStr.contains(newStore)) {
     *                             while (true) {
     *                                 // Get their option for what they want to do to this store
     *                                 int mod = Integer.parseInt(br.readLine());
     *                                 Store store = seller.getSpecificStore(location - 1);
     *                                 switch (mod) {
     *                                     case 1:
     *                                         // add item
     *                                         // Need to have client sending messages for this stuff to work.
     *                                         String prodName = br.readLine();
     *                                         String storeName = seller.getSpecificStore(location - 1).getStoreName();
     *                                         String desc = br.readLine();
     *                                         int available = 0;
     *                                         double price = 0;
     *                                         while (true) {
     *                                             try {
     *                                                 available = Integer.parseInt(br.readLine());
     *                                                 price = Integer.parseInt(br.readLine());
     *                                                 break;
     *                                             } catch (Exception e) {
     *                                                 // TODO: make it so that client produces an error message from this.
     *                                                 pw.write("Error");
     *                                             }
     *                                         }
     *                                         store.addItem(new Item(prodName, storeName, desc, available, price));
     *                                         break;
     *                                     case 2:
     *                                         // Remove item
     *                                         // TODO: in client, print out the list of the items for the user to choose.
     *                                         String decision = br.readLine();
     *                                         try {
     *
     *                                         } catch (Exception e) {
     *                                             // client produce error message from this
     *                                             pw.write("Error");
     *                                         }
     *                                         break;
     *                                     case 3:
     *                                         // TODO: Edit item
     *                                         break;
     *                                     case 4:
     *                                         // Cancel
     *                                         pw.write("Cancel");
     *                                         break;
     *                                 }
     *                             }
     *                         }
     *
     *                     }
     *                 case 2:
     *                     // TODO
     *                     break;
     *                 case 3:
     *                     // logout TODO
     *                     break;
     *             }
     *         } catch (Exception e) {
     *             e.printStackTrace();
     *         }
     *         return false;
     *     }
     *
     */
}
