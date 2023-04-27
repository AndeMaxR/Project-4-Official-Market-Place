import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    final PrintWriter printWriter;
    final BufferedReader bufferedReader;
    final Socket socket;
    public static final Object obj = new Object();

    private String username;
    private String password;

    private Seller seller;
    private Customer customer;

    public ClientHandler(PrintWriter printWriter, BufferedReader bufferedReader, Socket socket) {
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        this.socket = socket;
    }

    @Override
    public void run() {
        String temp;
        try {
            synchronized (obj) {
                temp = bufferedReader.readLine();
            }
            if (temp.contains("LOGIN")) {
                login(temp);
            } else if (temp.contains("SIGNUP")) {
                signup(temp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
                synchronized (obj) {
                    String temp;
                    BufferedReader bfr = new BufferedReader(new FileReader(file));
                    temp = bfr.readLine();
                    bfr.close();
                    if (temp == null) {
                        synchronized (obj) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                        }
                    } else if (temp.contains(list[2])) {
                        username = list[1];
                        password = list[2];
                        synchronized (obj) {
                            String content = "1\n";
                            if (temp.contains("SELLER")) {
                                seller = new Seller(username, password);
                                content += "SELLER\n";
                                printWriter.write(content);
                                printWriter.flush();
                                seller(socket);
                            } else {
                                customer = new Customer(username, password);
                                content += "CUSTOMER\n";
                                printWriter.write(content);
                                printWriter.flush();
                                customer(socket);
                            }
                        }
                    } else {
                        synchronized (obj) {
                            printWriter.write("-1\n");
                            printWriter.flush();
                        }
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
            synchronized (obj) {
                username = list[1];
                password = list[2];
                if (list[3].equals("Seller")) {
                    seller = new Seller(username, password);
                    synchronized (obj) {
                        printWriter.write("1\n");
                        printWriter.flush();
                    }
                } else {
                    customer = new Customer(username, password);
                    synchronized (obj) {
                        printWriter.write("1\n");
                        printWriter.flush();
                    }
                }
            }
        }
    }

    public boolean customer(Socket socket) {
        try {
            String temp = bufferedReader.readLine();
            int menu1Option = Integer.parseInt(temp);
            switch (menu1Option) {
                case 1: //Browse marketplace
                    printWriter.write("done\n");
                    printWriter.flush();
                    int menu2Option = Integer.parseInt(bufferedReader.readLine());
                    switch (menu2Option) {
                        case 1: //View whole marketPlace
                            if (!customer.viewMarket(printWriter)) {
                                return true;
                            }
                            break;
                        case 2: //search marketplace
                            //TODO
                            break;
                        case 3: //SortMarketPlace
                            //TODO
                            break;
                    }
                    break;
                case 2: //View Purchase History
                    //TODO
                    break;
                case 3: //Logout
                    //TODO
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean seller(Socket socket) {
        try {
            int option = Integer.parseInt(bufferedReader.readLine());
            switch (option) {
                // Manage Store
                case 1:
                    printWriter.write("success\n");
                    printWriter.flush();
                    int storeOption = Integer.parseInt(bufferedReader.readLine());
                    switch (storeOption) {
                        case 1:
                            List<Store> list = seller.getFullStoreList();
                            String storeNames = null;
                            for (Store st : list) {
                                if (storeNames == null) {
                                    storeNames = st.getStoreName();
                                } else {
                                    storeNames += "#" + st.getStoreName();
                                }
                            }
                            printWriter.write(storeNames+"\n");
                            printWriter.flush();
                            String editStore = bufferedReader.readLine();
                            break;
                        case 2:
                            printWriter.write("received\n");
                            printWriter.flush();
                            String newStore = bufferedReader.readLine();
                            seller.addStore(new Store(seller.getUsername(), newStore));
                            break;
                        case 3:
                            List<Store> removeList = seller.getFullStoreList();
                            String sn = null;
                            for (Store st : removeList) {
                                if (sn == null) {
                                    sn = st.getStoreName();
                                } else {
                                    sn += "#" + st.getStoreName();
                                }
                            }
                            printWriter.write(sn+"\n");
                            printWriter.flush();
                            int removeStoreIndex = Integer.parseInt(bufferedReader.readLine());
                            seller.removeStore(removeStoreIndex);
                            break;
                        case 4:
                            break;
                    }

//                    String myStr = seller.getStoreList();
//                    if (myStr.equals("")) {
//                        // Seller has no stores
//                        printWriter.write("Error\n");
//                        printWriter.flush();
//                        break;
//                    } else {
//                        // Seller has stores, so send message to client so it can display the stores.
//                        //TODO: CHECK THIS, might need to add stuff to send to the client so it can send info back.
//                        printWriter.write("Success\n");
//                        printWriter.flush();
//                        // Now get the store they would like to edit
//                        String newStore = bufferedReader.readLine();
//                        int location = Integer.parseInt(newStore);
//                        if (location == seller.getFullStoreList().size() + 1) {
//                            printWriter.write("Cancelling...\n");
//                            printWriter.flush();
//                            break;
//                        } else if (myStr.contains(newStore)) {
//                            while (true) {
//                                // Get their option for what they want to do to this store
//                                int mod = Integer.parseInt(bufferedReader.readLine());
//                                Store store = seller.getSpecificStore(location - 1);
//                                switch (mod) {
//                                    case 1:
//                                        // add item
//                                        // Need to have client sending messages for this stuff to work.
//                                        String prodName = bufferedReader.readLine();
//                                        String storeName = seller.getSpecificStore(location - 1).getStoreName();
//                                        String desc = bufferedReader.readLine();
//                                        int available = 0;
//                                        double price = 0;
//                                        while (true) {
//                                            try {
//                                                available = Integer.parseInt(bufferedReader.readLine());
//                                                price = Integer.parseInt(bufferedReader.readLine());
//                                                break;
//                                            } catch (Exception e) {
//                                                // TODO: make it so that client produces an error message from this.
//                                                printWriter.write("Error\n");
//                                                printWriter.flush();
//                                            }
//                                        }
//                                        store.addItem(new Item(prodName, storeName, desc, available, price));
//                                        break;
//                                    case 2:
//                                        // Remove item
//                                        // TODO: in client, print out the list of the items for the user to choose.
//                                        String decision = bufferedReader.readLine();
//                                        try {
//
//                                        } catch (Exception e) {
//                                            // client produce error message from this
//                                            printWriter.write("Error\n");
//                                            printWriter.flush();
//                                        }
//                                        break;
//                                    case 3:
//                                        // TODO: Edit item
//                                        break;
//                                    case 4:
//                                        // Cancel
//                                        printWriter.write("Cancel\n");
//                                        printWriter.flush();
//                                        break;
//                                }
//                            }
//                        }
//
//                    }
                case 2:

                    break;
                case 3:
                    // logout TODO
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
