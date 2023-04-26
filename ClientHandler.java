import java.io.*;
import java.net.Socket;

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
                            if (temp.contains("SELLER")) {
                                seller = new Seller(username, password);
                            } else {
                                customer = new Customer(username, password);
                            }
                            printWriter.write("1\n");
                            printWriter.flush();
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

    /**
     *  public static boolean customer(Socket socket) {
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
