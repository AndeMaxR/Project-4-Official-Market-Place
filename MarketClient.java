import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.EventHandler;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Market Client
 *
 * This class handles the GUI and sends input to the server for processing.
 *
 * @author Mark Herman, Max Anderson, Colin McKee, Aarnav Bomma, Section L06
 *
 * @version 4/18/2023
 */
public class MarketClient extends JComponent {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4242);
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            register(socket, pw, br);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //todo Make methods for each individual panel that return boolean value so that way we can move through out panels


    public static void register(Socket socket, PrintWriter pw, BufferedReader br) {
        // initialize the frame
        JFrame frame = new JFrame();
        // initialize the buttons
        JButton login;
        JButton signup;
        JButton cancel;

        JTextField usernameTextBox;
        JTextField passwordTextBox;
        //initialize the button objects
        login = new JButton("Login");
        signup = new JButton("Sign-up");
        cancel = new JButton("Cancel");

        // initialize the textField objects
        usernameTextBox = new JTextField("",20);
        usernameTextBox.setSize(20, 5);
        passwordTextBox = new JTextField("",20);
        passwordTextBox.setSize(20, 5);

        //todo add action Listeners ##############################################################
        //Added Cancel ActionListener
        ActionListener registerActions = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == cancel) {
                    frame.dispose();
                    try {
                        pw.close();
                        br.close();
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        throw new RuntimeException(ex);
                    }
                    return;
                }
                // if login is pressed
                if (e.getSource() == login) {
                    login(socket, frame, usernameTextBox, passwordTextBox, pw, br);
                }
                // if signup is pressed
                if (e.getSource() == signup) {
                    signup(socket, frame, usernameTextBox, passwordTextBox, pw, br);
                }

            }
        };
        login.addActionListener(registerActions);
        signup.addActionListener(registerActions);
        cancel.addActionListener(registerActions);
        //todo add action Listeners ##############################################################

        frame.setTitle("Welcome");
        Container content = frame.getContentPane();

        //content.setLayout(new BorderLayout());
        content.setLayout(new GridLayout(5, 3,20,5));
        //marketClient = this;
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add the panels to the frame so they are visible when run
        content.add(new JLabel(""));
        content.add(new JLabel(""));
        content.add(new JLabel(""));
        content.add(new JLabel("                        Username:"));
        content.add(usernameTextBox);
        content.add(new JLabel(""));
        content.add(new JLabel("                        Password:"));
        content.add(passwordTextBox);
        content.add(new JLabel(""));
        content.add(new JLabel(""));
        content.add(new JLabel(""));
        content.add(new JLabel(""));
        content.add(login);
        content.add(signup);
        content.add(cancel);

        frame.setVisible(true);
    }

    /**
     * This takes in the socket as the input so that way we can pass information back and forth to the
     * connected socket.
     *
     * initially we write username,password to the server.
     * the server will check to see if the user input matches any made accounts, if it matches the server will return 1,
     * if it doesn't match the server will return a number 0.
     *
     * @param socket
     */
    public static void login(Socket socket, Frame frame, JTextField usernameTextBox, JTextField passwordTextBox,
                             PrintWriter pw, BufferedReader br) {
        try {
            pw.write("LOGIN," + usernameTextBox.getText() + "," + passwordTextBox.getText() + "\n");
            pw.flush();

            int decision = Integer.parseInt(br.readLine());
            if (decision == 1) {
                JOptionPane.showMessageDialog(null,"You have successfully been logged in "
                        + usernameTextBox.getText() + "!", "Login", JOptionPane.PLAIN_MESSAGE);

                String username = usernameTextBox.getText();
                String password = passwordTextBox.getText();
                String type = br.readLine();
                if (type.contains("SELLER")) {
                    frame.dispose();
                    seller(socket, username, pw, br);
                } else {
                    frame.dispose();
                    //TODO
                    customer(socket, username, pw, br);
                }
            } else {
                JOptionPane.showMessageDialog(null,"Login failed, your username or"
                        + " password was incorrect", "Login", JOptionPane.PLAIN_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"IOException", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void signup(Socket socket, Frame frame, JTextField usernameTextBox, JTextField passwordTextBox,
                              PrintWriter pw, BufferedReader br) {
        try {
            String[] options = {"Seller", "Customer"};
            String type = (String) JOptionPane.showInputDialog(null, "Please chose type: ",
                    "Type", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            String username = usernameTextBox.getText();
            String password = passwordTextBox.getText();
            if (type.equals("Seller")) {
                pw.write("SIGNUP," + username + "," + password + "," + type + "\n");
                pw.flush();

                int decision = Integer.parseInt(br.readLine());
                if (decision == 1) {
                    JOptionPane.showMessageDialog(null,"You have successfully been signed-up" +
                            " and logged in "
                            + username + "!", "Sign-up", JOptionPane.PLAIN_MESSAGE);
                    frame.dispose();
                    seller(socket, username, pw, br);
                } else {
                    JOptionPane.showMessageDialog(null,"Sign-up failed, there is already an " +
                            "account with the same username", "Sign-up", JOptionPane.PLAIN_MESSAGE);
                }
            } else if (type.equals("Customer")) {
                pw.write("SIGNUP," + usernameTextBox.getText() + "," + passwordTextBox.getText() + "," + type + "\n");
                pw.flush();


                int decision = Integer.parseInt(br.readLine());
                if (decision == 1) {
                    JOptionPane.showMessageDialog(null,"You have successfully been signed-up" +
                            " and logged in "
                            + username + "!", "Sign-up", JOptionPane.PLAIN_MESSAGE);
                    frame.dispose();
                    customer(socket, username, pw, br);
                } else {
                    JOptionPane.showMessageDialog(null,"Sign-up failed, there is already an " +
                            "account with the same username", "Sign-up", JOptionPane.PLAIN_MESSAGE);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"IOException", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void seller(Socket socket, String username, PrintWriter pw,
                              BufferedReader br) {
        //Initialize the frame
        JFrame frame1 = new JFrame();
        frame1.setSize(600, 400);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //customer Buttons
        JButton ManageStores;
        JButton ManageFinances;
        JButton viewDashboard;
        JButton sellerLogoutButton;
        JTextField textField;

        ManageStores = new JButton("Manage Stores");
        ManageFinances = new JButton("View Finances");
        viewDashboard = new JButton("View Dashboard");
        sellerLogoutButton = new JButton("Logout");
        textField  = new JTextField("");
        textField.setEditable(false);



        frame1.setTitle("Seller Interface");
        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(1, 2,0,0));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(4,1,0,0));

        panel1.add(ManageStores);
        panel1.add(ManageFinances);
        panel1.add(viewDashboard);
        panel1.add(sellerLogoutButton);

        content.add(textField);
        content.add(panel1);


        ActionListener sellerActionListeners = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == ManageStores) {
                    frame1.dispose();
                    manageStores(socket, username, pw, br);
                }
                // if login is pressed
                if (e.getSource() == ManageFinances) {
                //TODO
                }
                // if signup is pressed
                if (e.getSource() == viewDashboard) {
                //TODO
                }
                if (e.getSource() == sellerLogoutButton) {
                    pw.write("Logout\n");
                    pw.flush();
                    frame1.dispose();
                    register(socket, pw, br);
                }
            }
        };
        ManageStores.addActionListener(sellerActionListeners);
        ManageFinances.addActionListener(sellerActionListeners);
        viewDashboard.addActionListener(sellerActionListeners);
        sellerLogoutButton.addActionListener(sellerActionListeners);
        frame1.setVisible(true);

    }

    public static void manageStores(Socket socket, String username, PrintWriter pw,
                                    BufferedReader br) {
        //Initialize the frame
        JFrame frame1 = new JFrame();
        frame1.setSize(600, 400);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //customer Buttons
        JButton AddStore;
        JButton RemoveStore;
        JButton ManageInventory;
        JButton Cancel;

        AddStore = new JButton("Add Store");
        RemoveStore = new JButton("Remove Store");
        ManageInventory = new JButton("Manage Inventory");
        Cancel = new JButton("Cancel");



        frame1.setTitle("Seller Interface");
        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(1, 1,0,0));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(4,1,0,0));

        panel1.add(AddStore);
        panel1.add(RemoveStore);
        panel1.add(ManageInventory);
        panel1.add(Cancel);

        content.add(panel1);

        ActionListener sellerActionListeners = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == AddStore) {
                    frame1.dispose();
                    addStore(socket, username, pw, br);
                }
                // if login is pressed
                if (e.getSource() == RemoveStore) {
                    frame1.dispose();
                    removeStore(socket, username, pw, br);
                }
                // if signup is pressed
                if (e.getSource() == ManageInventory) {
                    frame1.dispose();
                    manageInventory(socket, username, pw, br);
                }
                if (e.getSource() == Cancel) {
                    frame1.dispose();
                    seller(socket, username, pw, br);
                }
            }
        };
        AddStore.addActionListener(sellerActionListeners);
        RemoveStore.addActionListener(sellerActionListeners);
        ManageInventory.addActionListener(sellerActionListeners);
        Cancel.addActionListener(sellerActionListeners);
        frame1.setVisible(true);
    }
    public static void addStore(Socket socket, String username, PrintWriter pw,
                                BufferedReader br) {
        JFrame frame1 = new JFrame();
        frame1.setSize(600, 400);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel storeName;
        JTextField textField;
        JButton enter;
        JButton cancel;

        storeName = new JLabel("Store name: ");
        textField = new JTextField("", 30);
        enter = new JButton("Enter");
        cancel = new JButton("Cancel");;

        frame1.setTitle("Add store Interface");
        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(2, 2,0,0));
        content.add(storeName);
        content.add(textField);
        content.add(cancel);
        content.add(enter);
        ActionListener addStoreActionListeners = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == enter) {
                    //might need username + "," +
                    pw.write("AddStore\n");
                    pw.flush();
                    pw.write(textField.getText() + "\n");
                    pw.flush();
                    try {
                        String temp = br.readLine();
                        if (temp.equals("-1")) {
                            JOptionPane.showMessageDialog(null, "The Store wasn't added",
                                    "Failed", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "The Store was added",
                                    "Complete", JOptionPane.PLAIN_MESSAGE);
                        }
                    } catch (Exception s) {
                        s.printStackTrace();
                    }
                    frame1.dispose();
                    manageStores(socket, username, pw, br);
                }
                if (e.getSource() == cancel) {
                    frame1.dispose();
                    manageStores(socket, username, pw, br);
                }
            }
        };
        enter.addActionListener(addStoreActionListeners);
        cancel.addActionListener(addStoreActionListeners);
        frame1.setVisible(true);
    }

    public static void removeStore(Socket socket, String username, PrintWriter pw,
                                BufferedReader br) {
        pw.write("RemoveStore\n");
        pw.flush();
        String[] list = new String[0];
        try {
            list = br.readLine().split(",");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame1 = new JFrame();
        frame1.setSize(600, 400);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel storeName;
        JComboBox stores;
        JButton enter;
        JButton cancel;

        storeName = new JLabel("Store name: ");
        stores = new JComboBox<>(list);
        enter = new JButton("Enter");
        cancel = new JButton("Cancel");;

        frame1.setTitle("Remove store Interface");
        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(2, 2,0,0));
        content.add(storeName);
        content.add(stores);
        content.add(cancel);
        content.add(enter);
        ActionListener removeStoreActionListeners = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == enter) {
                    //might need username + "," +
                    pw.write(stores.getSelectedIndex() + "\n");
                    pw.flush();
                    try {
                        String temp = br.readLine();
                        if (temp.equals("-1")) {
                            JOptionPane.showMessageDialog(null, "The Store wasn't removed",
                                    "Failed", JOptionPane.PLAIN_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "The Store was removed",
                                    "Complete", JOptionPane.PLAIN_MESSAGE);
                        }
                    } catch (Exception s) {
                        s.printStackTrace();
                    }
                    frame1.dispose();
                    manageStores(socket, username, pw, br);
                }
                if (e.getSource() == cancel) {
                    pw.write("-1\n");
                    frame1.dispose();
                    manageStores(socket, username, pw, br);
                }
            }
        };
        enter.addActionListener(removeStoreActionListeners);
        cancel.addActionListener(removeStoreActionListeners);
        frame1.setVisible(true);
    }

    public static void manageInventory(Socket socket, String username, PrintWriter pw,
                                       BufferedReader br) {
        //Initialize the frame
        JFrame frame1 = new JFrame();
        frame1.setSize(600, 400);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //customer Buttons
        JButton AddItem;
        JButton RemoveItem;
        JButton EditItem;
        JButton Cancel;

        AddItem = new JButton("Add Item");
        RemoveItem = new JButton("Remove Item");
        EditItem = new JButton("Edit Item");
        Cancel = new JButton("Cancel");

        frame1.setTitle("Seller Interface");
        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(1, 1,0,0));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(4,1,0,0));

        panel1.add(AddItem);
        panel1.add(RemoveItem);
        panel1.add(EditItem);
        panel1.add(Cancel);

        content.add(panel1);

        ActionListener sellerActionListeners = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == AddItem) {
                    frame1.dispose();
                    addStore(socket, username, pw, br);
                }
                // if login is pressed
                if (e.getSource() == RemoveItem) {
                    frame1.dispose();
                    removeStore(socket, username, pw, br);
                }
                // if signup is pressed
                if (e.getSource() == EditItem) {
                    frame1.dispose();
                    manageInventory(socket, username, pw, br);
                }
                if (e.getSource() == Cancel) {
                    frame1.dispose();
                    manageStores(socket, username, pw, br);
                }
            }
        };
        AddItem.addActionListener(sellerActionListeners);
        RemoveItem.addActionListener(sellerActionListeners);
        EditItem.addActionListener(sellerActionListeners);
        Cancel.addActionListener(sellerActionListeners);
        frame1.setVisible(true);

    }

    //TODO
    public static void addItem(Socket socket, String username, PrintWriter pw,
                               BufferedReader br) {

    }
    //TODO
    public static void removeItem(Socket socket, String username, PrintWriter pw,
                                  BufferedReader br) {

    }
    //TODO
    public static void editItem(Socket socket, String username, PrintWriter pw,
                                BufferedReader br) {

    }

    //TODO: implement the code below. THE CODE BELOW IS VERY IMPORTANT IT JUST NEEDS TO BE ALTERED to conform to updated CODE
    //IMPORTANT DO NOT DELETE
    public static void customer(Socket socket, String username, PrintWriter pw,
                                BufferedReader br) {
        //Initialize the frame
        JFrame frame1 = new JFrame();
        frame1.setSize(600, 400);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //customer Buttons
        JButton viewMarket;
        JButton viewDashboard;
        JButton customerViewPurchaseHistoryButton;
        JButton exportPurchaseHistory;
        JButton customerLogoutButton;

        viewMarket = new JButton("View Market");
        viewDashboard = new JButton("View Dashboard");
        customerViewPurchaseHistoryButton = new JButton("View Purchase History");
        exportPurchaseHistory = new JButton("Export Purchase History");
        customerLogoutButton = new JButton("Logout");


        frame1.setTitle("Customer Interface");
        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(1, 1,0,0));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(5,1,0,0));

        panel1.add(viewMarket);
        panel1.add(viewDashboard);
        panel1.add(customerViewPurchaseHistoryButton);
        panel1.add(exportPurchaseHistory);
        panel1.add(customerLogoutButton);

        content.add(panel1);


        ActionListener customerActionListeners = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == viewMarket) {
                    frame1.dispose();
                    pw.write("viewMarket\n");
                    pw.flush();
                    viewMarket(socket, username, pw, br);
                }
                if (e.getSource() == viewDashboard) {

                }
                if (e.getSource() == customerViewPurchaseHistoryButton) {

                }
                if (e.getSource() == exportPurchaseHistory) {

                }
                if (e.getSource() == customerLogoutButton) {
                    pw.write("Logout\n");
                    pw.flush();
                    frame1.dispose();
                    register(socket, pw, br);
                }

            }
        };
        viewMarket.addActionListener(customerActionListeners);
        viewDashboard.addActionListener(customerActionListeners);
        customerViewPurchaseHistoryButton.addActionListener(customerActionListeners);
        exportPurchaseHistory.addActionListener(customerActionListeners);
        customerLogoutButton.addActionListener(customerActionListeners);
        frame1.setVisible(true);
    }

    public static void viewMarket(Socket socket,  String username, PrintWriter pw, BufferedReader br) {
        String temp;
        String[] storesList = new String[0];
        ArrayList<String[]> itemList = new ArrayList<>();
        try {
            temp = br.readLine();
            storesList = new String[Integer.parseInt(temp)];
            for (int i = 0; i < storesList.length; i++) {
                storesList[i] = br.readLine();
                itemList.add(br.readLine().split(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame1 = new JFrame();
        frame1.setSize(600, 400);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //customer Buttons
        JLabel purchaseLabel;
        //TODO CHECK TO SEE IF THIS WORKS ONCE ADD ITEM IS DONE
        JComboBox stores;
        JComboBox items;
        JButton SearchMarket;
        JButton SortMarket;
        JButton refresh;
        JButton cancel;
        JButton purchase;
        JTextField textField;

        String fullString = "";
        for (int i = 0; i < storesList.length; i++) {
            fullString += storesList[i] + "\n";
            for (int j = 0; itemList.size() != 0 && j < itemList.get(i).length; j++) {
                fullString += itemList.get(i)[j] + "\n";
            }
        }

        purchaseLabel = new JLabel("Select and item if you wish to purchase it");
        SearchMarket = new JButton("Search Market");
        SortMarket = new JButton("Sort Market");
        purchase = new JButton("Purchase");
        refresh = new JButton("Refresh");
        cancel = new JButton("Cancel");
        textField = new JTextField(fullString);

        if (storesList.length == 0) {
            stores = new JComboBox<>(new String[]{"No Stores Available"});
            purchase.setEnabled(false);
        } else {
            stores = new JComboBox<>(storesList);
            purchase.setEnabled(true);
        }
        if (itemList.size() == 0) {
            items = new JComboBox<>(new String[]{"No Items Available"});
            purchase.setEnabled(false);
        } else if (itemList.get(0).length == 1 && itemList.get(0)[0].equals("")) {
            items = new JComboBox<>(new String[]{"No Items Available"});
            purchase.setEnabled(false);
        } else {
            items = new JComboBox<>(itemList.get(0));
            purchase.setEnabled(true);
        }


        frame1.setTitle("Market_Interface");
        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(1, 2,0,0));

        content.add(textField);

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridLayout(1,2,0,0));
        subPanel.add(stores);
        subPanel.add(items);


        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(7,1,0,0));

        panel1.add(purchaseLabel);
        panel1.add(subPanel);
        panel1.add(SearchMarket);
        panel1.add(SortMarket);
        panel1.add(purchase);
        panel1.add(refresh);
        panel1.add(cancel);

        content.add(panel1);


        ActionListener marketActionListeners = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == stores) {
                    //COMEBACK TO
                    items.removeAllItems();
                    if (itemList.size() == 0) {
                        items.addItem(new String[]{"No Items Available"});
                        purchase.setEnabled(false);
                    } else if (itemList.get(stores.getSelectedIndex()).length == 1
                            && itemList.get(stores.getSelectedIndex())[0].equals("")) {
                        items.addItem(new String[]{"No Items Available"});
                        purchase.setEnabled(false);
                    } else {
                        for (int i = 0; i < itemList.get(stores.getSelectedIndex()).length; i++) {
                            items.addItem(itemList.get(stores.getSelectedIndex())[i]);
                            purchase.setEnabled(true);
                        }
                    }

                }
                if (e.getSource() == SearchMarket) {
                    //TODO
                }
                if (e.getSource() == SortMarket) {
                    //TODO
                }
                if (e.getSource() == purchase) {
                    //TODO
                }
                if (e.getSource() == refresh) {
                    //TODO
                }
                if (e.getSource() == cancel) {
                    frame1.dispose();
                    customer(socket, username, pw, br);
                }
            }
        };
        stores.addActionListener(marketActionListeners);
        SearchMarket.addActionListener(marketActionListeners);
        SortMarket.addActionListener(marketActionListeners);
        purchase.addActionListener(marketActionListeners);
        refresh.addActionListener(marketActionListeners);
        cancel.addActionListener(marketActionListeners);
        frame1.setVisible(true);

    }

    public static void makePurchase(Socket socket, PrintWriter pw,
                                    BufferedReader br) {

    }
}