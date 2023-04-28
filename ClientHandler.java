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
                            printWriter.write(storeNames + "\n");
                            printWriter.flush();
                            int storeLocation = Integer.parseInt(bufferedReader.readLine());
                            printWriter.write("received\n");
                            printWriter.flush();
                            int editStoreAction = Integer.parseInt(bufferedReader.readLine());
                            switch (editStoreAction) {
                                case 1:
                                    printWriter.write("received\n");
                                    printWriter.flush();
                                    int modifyItemAction = Integer.parseInt(bufferedReader.readLine());
                                    switch (modifyItemAction) {
                                        case 1:
                                            //add item
                                            printWriter.write("received\n");
                                            printWriter.flush();
                                            String[] data = bufferedReader.readLine().split("#");
                                            Store store = seller.getSpecificStore(storeLocation);
                                            store.addItem(new Item(data[0], store.getStoreName(), data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3])));
                                            printWriter.write("success\n");
                                            printWriter.flush();
                                            modifyItem(socket, storeLocation);
                                            break;
                                        case 2:
                                            //remove item
                                            store = seller.getSpecificStore(storeLocation);
                                            List<Item> rmItems = store.listItems();
                                            String rmItemNames = null;
                                            for (Item st : rmItems) {
                                                if (rmItemNames == null) {
                                                    rmItemNames = st.getProductName();
                                                } else {
                                                    rmItemNames += "#" + st.getProductName();
                                                }
                                            }
                                            printWriter.write(rmItemNames + "\n");
                                            printWriter.flush();
                                            int rmItemLocation = Integer.parseInt(bufferedReader.readLine());
                                            store.removeItem(rmItemLocation);
                                            printWriter.write("success\n");
                                            printWriter.flush();
                                            modifyItem(socket, storeLocation);
                                            break;
                                        case 3:
                                            //edit item
                                            store = seller.getSpecificStore(storeLocation);
                                            List<Item> items = store.listItems();
                                            String itemNames = null;
                                            for (Item st : items) {
                                                if (itemNames == null) {
                                                    itemNames = st.getProductName();
                                                } else {
                                                    itemNames += "#" + st.getProductName();
                                                }
                                            }
                                            printWriter.write(itemNames + "\n");
                                            printWriter.flush();
                                            int itemLocation = Integer.parseInt(bufferedReader.readLine());
                                            printWriter.write("received\n");
                                            printWriter.flush();
                                            data = bufferedReader.readLine().split("#");
                                            store.editItem(itemLocation, data[0], store.getItem(itemLocation).getProductName(), data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]));
                                            printWriter.write("success\n");
                                            printWriter.flush();
                                            modifyItem(socket, storeLocation);
                                            break;
                                        case 4:
                                            //cancel
                                            printWriter.write("received\n");
                                            printWriter.flush();
                                            manageStore(socket);
                                            break;
                                    }
                                    break;
                                case 2:
                                    printWriter.write("received\n");
                                    printWriter.flush();
                                    int modifyStoreInfoAction = Integer.parseInt(bufferedReader.readLine());
                                    switch (modifyStoreInfoAction) {
                                        case 1:
                                            //change store name
                                            printWriter.write("received\n");
                                            printWriter.flush();
                                            String changeStoreName = bufferedReader.readLine();
                                            seller.getSpecificStore(storeLocation).setStoreName(changeStoreName);
                                            printWriter.write("success\n");
                                            printWriter.flush();
                                            modifyStoreInfo(socket, storeLocation);
                                            break;
                                        case 2:
                                            //cancel
                                            printWriter.write("received\n");
                                            printWriter.flush();
                                            modifyItem(socket, storeLocation);
                                            break;
                                    }
                                    break;
                                case 3:
                                    printWriter.write("received\n");
                                    printWriter.flush();
                                    manageStore(socket);
                                    break;
                            }
                            break;
                        case 2:
                            printWriter.write("received\n");
                            printWriter.flush();
                            String newStore = bufferedReader.readLine();
                            seller.addStore(new Store(seller.getUsername(), newStore));
                            printWriter.write("success\n");
                            printWriter.flush();
                            manageStore(socket);
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
                            printWriter.write(sn + "\n");
                            printWriter.flush();
                            int removeStoreIndex = Integer.parseInt(bufferedReader.readLine());
                            seller.removeStore(removeStoreIndex);
                            printWriter.write("success\n");
                            printWriter.flush();
                            int doItAgain = Integer.parseInt(bufferedReader.readLine());
                            if (doItAgain == 1) {
                                printWriter.write("done\n");
                                printWriter.flush();
                                manageStore(socket);
                            } else {
                                printWriter.write("done\n");
                                printWriter.flush();
                                seller(socket);
                            }
                            break;
                        case 4:
                            printWriter.write("received\n");
                            printWriter.flush();
                            seller(socket);
                            break;
                    }
                case 2:
                    List<Store> storeList = seller.getFullStoreList();
                    String sn = null;
                    for (Store st : storeList) {
                        if (sn == null) {
                            sn = st.getStoreName();
                        } else {
                            sn += "#" + st.getStoreName();
                        }
                    }
                    printWriter.write(sn + "\n");
                    printWriter.flush();
                    int financeStoreIndex = Integer.parseInt(bufferedReader.readLine());
                    String finances = seller.getSpecificStore(financeStoreIndex).getFinances().replace("\n", "||");
                    printWriter.write(finances + "\n");
                    printWriter.flush();
                    int doItAgain = Integer.parseInt(bufferedReader.readLine());
                    if (doItAgain == 1) {
                        viewFinances(socket);
                    } else {
                        printWriter.write("done\n");
                        printWriter.flush();
                        seller(socket);
                    }
                    break;
                case 3:
                    // logout Done
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean modifyStoreInfo(Socket socket, int storeLocation) throws IOException {
        int modifyStoreInfoAction = Integer.parseInt(bufferedReader.readLine());
        switch (modifyStoreInfoAction) {
            case 1:
                //change store name
                printWriter.write("received\n");
                printWriter.flush();
                String changeStoreName = bufferedReader.readLine();
                seller.getSpecificStore(storeLocation).setStoreName(changeStoreName);
                printWriter.write("success\n");
                printWriter.flush();
                modifyStoreInfo(socket, storeLocation);
                break;
            case 2:
                //cancel
                printWriter.write("received\n");
                printWriter.flush();
                modifyItem(socket, storeLocation);
                break;
        }
        return true;
    }
    public boolean modifyItem(Socket socket, int storeLocation) throws IOException {
        int modifyItemAction = Integer.parseInt(bufferedReader.readLine());
        switch (modifyItemAction) {
            case 1:
                //add item
                printWriter.write("received\n");
                printWriter.flush();
                String[] data = bufferedReader.readLine().split("#");
                Store store = seller.getSpecificStore(storeLocation);
                store.addItem(new Item(data[0], store.getStoreName(), data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3])));
                printWriter.write("success\n");
                printWriter.flush();
                modifyItem(socket, storeLocation);
                break;
            case 2:
                //remove item
                break;
            case 3:
                //edit item
                store = seller.getSpecificStore(storeLocation);
                List<Item> items = store.listItems();
                String itemNames = null;
                for (Item st : items) {
                    if (itemNames == null) {
                        itemNames = st.getProductName();
                    } else {
                        itemNames += "#" + st.getProductName();
                    }
                }
                printWriter.write(itemNames + "\n");
                printWriter.flush();
                int itemLocation = Integer.parseInt(bufferedReader.readLine());
                printWriter.write("received\n");
                printWriter.flush();
                data = bufferedReader.readLine().split("#");
                store.editItem(itemLocation, data[0], store.getItem(itemLocation).getProductName(), data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]));
                printWriter.write("success\n");
                printWriter.flush();
                modifyItem(socket, storeLocation);
                break;
            case 4:
                //cancel
                printWriter.write("received\n");
                printWriter.flush();
                manageStore(socket);
                break;
        }
        return true;
    }

    public boolean viewFinances(Socket socket) throws IOException {
        List<Store> removeList = seller.getFullStoreList();
        String sn = null;
        for (Store st : removeList) {
            if (sn == null) {
                sn = st.getStoreName();
            } else {
                sn += "#" + st.getStoreName();
            }
        }
        printWriter.write(sn + "\n");
        printWriter.flush();
        int financeStoreIndex = Integer.parseInt(bufferedReader.readLine());
        String finances = seller.getSpecificStore(financeStoreIndex).getFinances().replace("\n", "||");
        printWriter.write(finances + "\n");
        printWriter.flush();
        int doItAgain = Integer.parseInt(bufferedReader.readLine());
        if (doItAgain == 1) {
            viewFinances(socket);
        } else {
            printWriter.write("done\n");
            printWriter.flush();
            seller(socket);
        }
        return true;
    }

    public boolean manageStore(Socket socket) throws IOException {
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
                printWriter.write(storeNames + "\n");
                printWriter.flush();
                int storeLocation = Integer.parseInt(bufferedReader.readLine());
                printWriter.write("received\n");
                printWriter.flush();
                int editStoreAction = Integer.parseInt(bufferedReader.readLine());
                switch (editStoreAction) {
                    case 1:
                        printWriter.write("received\n");
                        printWriter.flush();
                        int modifyItemAction = Integer.parseInt(bufferedReader.readLine());
                        switch (modifyItemAction) {
                            case 1:
                                //add item
                                printWriter.write("received\n");
                                printWriter.flush();
                                String[] data = bufferedReader.readLine().split("#");
                                Store store = seller.getSpecificStore(storeLocation);
                                store.addItem(new Item(data[0], store.getStoreName(), data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3])));
                                printWriter.write("success\n");
                                printWriter.flush();
                                modifyItem(socket, storeLocation);
                                break;
                            case 2:
                                //remove item
                                store = seller.getSpecificStore(storeLocation);
                                List<Item> rmItems = store.listItems();
                                String rmItemNames = null;
                                for (Item st : rmItems) {
                                    if (rmItemNames == null) {
                                        rmItemNames = st.getProductName();
                                    } else {
                                        rmItemNames += "#" + st.getProductName();
                                    }
                                }
                                printWriter.write(rmItemNames + "\n");
                                printWriter.flush();
                                int rmItemLocation = Integer.parseInt(bufferedReader.readLine());
                                store.removeItem(rmItemLocation);
                                printWriter.write("success\n");
                                printWriter.flush();
                                modifyItem(socket, storeLocation);
                                break;
                            case 3:
                                //edit item
                                store = seller.getSpecificStore(storeLocation);
                                List<Item> items = store.listItems();
                                String itemNames = null;
                                for (Item st : items) {
                                    if (itemNames == null) {
                                        itemNames = st.getProductName();
                                    } else {
                                        itemNames += "#" + st.getProductName();
                                    }
                                }
                                printWriter.write(itemNames + "\n");
                                printWriter.flush();
                                int itemLocation = Integer.parseInt(bufferedReader.readLine());
                                printWriter.write("received\n");
                                printWriter.flush();
                                data = bufferedReader.readLine().split("#");
                                store.editItem(itemLocation, data[0], store.getItem(itemLocation).getProductName(), data[1], Integer.parseInt(data[2]), Double.parseDouble(data[3]));
                                printWriter.write("success\n");
                                printWriter.flush();
                                modifyItem(socket, storeLocation);
                                break;
                            case 4:
                                //cancel
                                printWriter.write("received\n");
                                printWriter.flush();
                                manageStore(socket);
                                break;
                        }
                        break;
                    case 2:
                        printWriter.write("received\n");
                        printWriter.flush();
                        int modifyStoreInfoAction = Integer.parseInt(bufferedReader.readLine());
                        switch (modifyStoreInfoAction) {
                            case 1:
                                //change store name
                                printWriter.write("received\n");
                                printWriter.flush();
                                String changeStoreName = bufferedReader.readLine();
                                seller.getSpecificStore(storeLocation).setStoreName(changeStoreName);
                                printWriter.write("success\n");
                                printWriter.flush();
                                modifyStoreInfo(socket, storeLocation);
                                break;
                            case 2:
                                //cancel
                                printWriter.write("received\n");
                                printWriter.flush();
                                modifyItem(socket, storeLocation);
                                break;
                        }
                        break;
                    case 3:
                        printWriter.write("received\n");
                        printWriter.flush();
                        manageStore(socket);
                        break;
                }
                break;
            case 2:
                printWriter.write("received\n");
                printWriter.flush();
                String newStore = bufferedReader.readLine();
                seller.addStore(new Store(seller.getUsername(), newStore));
                printWriter.write("success\n");
                printWriter.flush();
                manageStore(socket);
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
                printWriter.write(sn + "\n");
                printWriter.flush();
                int removeStoreIndex = Integer.parseInt(bufferedReader.readLine());
                seller.removeStore(removeStoreIndex);
                printWriter.write("success\n");
                printWriter.flush();
                int doItAgain = Integer.parseInt(bufferedReader.readLine());
                if (doItAgain == 1) {
                    printWriter.write("done\n");
                    printWriter.flush();
                    manageStore(socket);
                } else {
                    printWriter.write("done\n");
                    printWriter.flush();
                    seller(socket);
                }
                break;
            case 4:
                printWriter.write("received\n");
                printWriter.flush();
                seller(socket);
                break;
        }
        return true;
    }
}
