import javax.swing.*;
import javax.swing.border.LineBorder;
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

        //seller Buttons
        JButton ManageStores;
        JButton ManageFinances;
        JButton viewDashboard;
        JButton sellerLogoutButton;
        JTextArea textArea;

        ManageStores = new JButton("Manage Stores");
        ManageFinances = new JButton("View Finances");
        viewDashboard = new JButton("View Dashboard");
        sellerLogoutButton = new JButton("Logout");

        textArea  = new JTextArea(13, 20);
        textArea.setEditable(false);
        textArea.setBounds(0, 20, 280, 200);
        textArea.setBorder(new LineBorder(Color.black));

        JScrollPane scroll = new JScrollPane (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(280, 20, 20, 200);
        scroll.getViewport().setBackground(Color.white);
        scroll.getViewport().add(textArea);




        frame1.setTitle("Seller Interface");
        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(1, 2,0,0));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(4,1,0,0));

        content.add(scroll);
        panel1.add(ManageStores);
        panel1.add(ManageFinances);
        panel1.add(viewDashboard);
        panel1.add(sellerLogoutButton);

        //content.add(textArea);
        //content.add(scroll);

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
                    manageFinances(socket, username, pw, br, textArea);
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
    public static void manageFinances(Socket socket, String username, PrintWriter pw,
                                      BufferedReader br, JTextArea textArea) {
        pw.write("ViewFinances\n");
        pw.flush();
        try {
            String fulltext = "";
            String line;
            while (true) {
                line = br.readLine();
                if (line.equals("-1")) {
                    break;
                } else {
                    fulltext += line + "\n";
                }
            }
            textArea.setText(fulltext);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
                    addItem(socket, username, pw, br);
                }
                // if login is pressed
                if (e.getSource() == RemoveItem) {
                    frame1.dispose();
                    removeItem(socket, username, pw, br);
                }
                // if signup is pressed
                if (e.getSource() == EditItem) {
                    frame1.dispose();
                    editItem(socket, username, pw, br);
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

    // todo clienthandler part of this
    public static void addItem(Socket socket, String username, PrintWriter pw,
                               BufferedReader br) {

        JFrame frame1 = new JFrame("Add item interface");
        frame1.setSize(600, 400);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField productName = new JTextField("", 20);
        JTextField storeName = new JTextField("", 20);
        JTextField productDesc = new JTextField("", 20);
        JTextField price = new JTextField("", 5);
        JTextField available = new JTextField("", 5);
        JButton confirm = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");

        frame1.setTitle("Add Item Interface");

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Product Name:"));
        panel.add(productName);
        panel.add(new JLabel("Store Name:"));
        panel.add(storeName);
        panel.add(new JLabel("Product Description:"));
        panel.add(productDesc);
        panel.add(new JLabel("Price:"));
        panel.add(price);
        panel.add(new JLabel("Available Quantity:"));
        panel.add(available);
        panel.add(confirm);
        panel.add(cancel);

        Container content = frame1.getContentPane();
        content.add(panel);

        ActionListener addItemListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == confirm) {
                    String product = productName.getText();
                    String store = storeName.getText();
                    String desc = productDesc.getText();
                    String q = available.getText();
                    String p = price.getText();
                    try {
                        Double.parseDouble(p);
                        Integer.parseInt(q);
                        if (product.isEmpty() || store.isEmpty() || desc.isEmpty() || p.isEmpty() || q.isEmpty()) {
                            JOptionPane.showMessageDialog(frame1, "Please fill out all fields.");
                        } else {
                            pw.write("AddItem\n");
                            pw.flush();
                            pw.write(product + "," + store + "," + desc + "," + q + "," + p + "\n");
                            pw.flush();
                        }
                        if (br.readLine().equals("-1")) {
                            JOptionPane.showMessageDialog(frame1, "The item was not added.");
                        } else {
                            JOptionPane.showMessageDialog(frame1, "The item was added.");
                            frame1.dispose();
                            manageInventory(socket, username, pw, br);
                        }
                    } catch (Exception y) {
                        JOptionPane.showMessageDialog(frame1, "Please fill out all fields correctly.", "Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }
                if (e.getSource() == cancel) {
                    frame1.dispose();
                    manageInventory(socket, username, pw, br);
                }
            }
        };

        confirm.addActionListener(addItemListener);
        cancel.addActionListener(addItemListener);

        frame1.setVisible(true);
    }
    //TODO
    public static void editItem(Socket socket, String username, PrintWriter pw,
                                  BufferedReader br) {
        pw.write("EditItem\n");
        pw.flush();

        JFrame editItemInterface = new JFrame("Edit Item Interface");
        editItemInterface.setSize(800, 600);
        editItemInterface.setLocationRelativeTo(null);
        editItemInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container content = editItemInterface.getContentPane();
        content.setLayout(new GridLayout(1, 3,0,0));

        JTextArea list = new JTextArea("");
        list.setEditable(false);

        list.setBounds(0, 0, 250, 600);
        list.setBorder(new LineBorder(Color.BLACK));

        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(230, 0, 20, 600);
        scrollPane.getViewport().add(list);

        JLabel oldLabel = new JLabel("Selected Item");
        JLabel oldStoreLabel = new JLabel("Store name: ");
        JLabel oldItemLabel = new JLabel("Item name: ");
        JTextField oldStoreText = new JTextField("", 30);
        JTextField oldItemText = new JTextField("", 20);

        JPanel panel1 = new JPanel(new GridLayout(8,2, 0, 0));
        panel1.add(oldLabel);
        panel1.add(new JLabel(""));
        panel1.add(new JLabel(""));
        panel1.add(new JLabel(""));
        panel1.add(oldStoreLabel);
        panel1.add(oldStoreText);
        panel1.add(oldItemLabel);
        panel1.add(oldItemText);

        JPanel panel2 = new JPanel(new GridLayout(8,2, 0, 0));
        JLabel newLabel = new JLabel("Edited Item");
        JLabel productNameLabel = new JLabel("Product Name: ");
        JLabel storeNameLabel = new JLabel("Store Name: ");
        JLabel descriptionLabel = new JLabel("Description: ");
        JLabel quantityAvailable = new JLabel("Quantity Available: ");
        JLabel priceLabel = new JLabel("Price: ");

        JTextField nameText = new JTextField("", 20);
        JTextField storeText = new JTextField("", 30);
        JTextField descText = new JTextField("", 200);
        JTextField quantText = new JTextField("",5);
        JTextField priceText = new JTextField("", 10);

        JButton cancel = new JButton("Cancel");
        JButton confirm = new JButton("Confirm");
        storeText.setEditable(false);

        panel2.add(newLabel);
        panel1.add(new JLabel(""));
        panel2.add(new JLabel(""));
        panel1.add(new JLabel(""));
        panel2.add(productNameLabel);
        panel2.add(nameText);
        panel2.add(storeNameLabel);
        panel2.add(storeText);
        panel2.add(descriptionLabel);
        panel2.add(descText);
        panel2.add(quantityAvailable);
        panel2.add(quantText);
        panel2.add(priceLabel);
        panel2.add(priceText);
        panel2.add(new JLabel(""));
        panel2.add(new JLabel(""));
        panel2.add(cancel);
        panel2.add(confirm);

        String temp;
        String[] storeList = new String[1];
        ArrayList<String[]> itemList = new ArrayList<>();;
        try {
            temp = br.readLine();
            if (temp.equals("")) {
                storeList[0] = "No Stores!";
                itemList.add(new String[]{"No Items!"});
                confirm.setEnabled(false);
            } else {
                storeList = temp.split(",");
                itemList = new ArrayList<>();
                for (int i = 0; i < storeList.length; i++) {
                    temp = br.readLine();
                    itemList.add(temp.split(","));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        temp = "";
        for (int i = 0; i < storeList.length; i++) {
            temp += "Store: " + storeList[i].substring(storeList[i].indexOf(".") + 2) + "\nItems:\n";
            for (int j = 0; j < itemList.get(i).length; j++) {
                if (itemList.get(i)[j].equals("")) {
                    temp += "None.\n";
                } else {
                    temp += itemList.get(i)[j] + "\n";
                }
            }
            temp += "\n";
        }
        list.setText(temp);

        content.add(scrollPane);
        content.add(panel1);
        content.add(panel2);


        ActionListener editItemListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == confirm) {
                    String oldStoreName = oldStoreText.getText();
                    String oldStoreItem = oldItemText.getText();

                    pw.write(oldStoreName + "," + oldStoreItem + "\n");
                    pw.flush();

                    try {
                        String itemName = nameText.getText();
                        String storeName = storeText.getText();
                        String desc = descText.getText();
                        String quant = quantText.getText();
                        String price = priceText.getText();

                        Integer.parseInt(quant);
                        Double.parseDouble(price);

                        pw.write(itemName + "," + storeName + "," + desc + "," + quant + "," + price + "\n");
                        pw.flush();
                    } catch (Exception b) {
                        JOptionPane.showMessageDialog(editItemInterface, "Item was not edited," +
                                " please fill out all fields correctly.");
                    }

                    try {
                        if (br.readLine().equals("-1")) {
                            JOptionPane.showMessageDialog(editItemInterface, "Item was not removed," +
                                    " please fill out all fields correctly.");
                            //TODO YOU MAY NEED THIS YOU MAY NOT
                            pw.write("EditItem\n");
                            pw.flush();
                        } else {
                            JOptionPane.showMessageDialog(editItemInterface, "Item was successfully edited.");
                            editItemInterface.dispose();
                            manageInventory(socket, username, pw, br);
                        }
                    } catch (Exception k) {
                        k.printStackTrace();
                    }
                }
                if (e.getSource() == cancel) {
                    pw.write("cancel\n");
                    pw.flush();
                    editItemInterface.dispose();
                    manageInventory(socket, username, pw, br);
                }
                if (e.getSource() == oldStoreText) {
                    storeText.setText(oldStoreText.getText());
                }
            }
        };

        confirm.addActionListener(editItemListener);
        cancel.addActionListener(editItemListener);
        oldStoreText.addActionListener(editItemListener);

        editItemInterface.setVisible(true);
    }
    //TODO
    public static void removeItem(Socket socket, String username, PrintWriter pw,
                                BufferedReader br) {
        pw.write("RemoveItem\n");
        pw.flush();

        JFrame frame1 = new JFrame("Remove item interface");
        frame1.setSize(600, 600);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(1, 2,0,0));

        JLabel storeLabel = new JLabel("Store name:");
        JLabel itemLabel = new JLabel("Item name:");
        JTextArea list = new JTextArea("");
        JTextField storeText = new JTextField("", 30);
        JTextField itemText = new JTextField("", 20);
        JButton confirm = new JButton("Confirm");
        JButton cancel = new JButton("Cancel");
        list.setEditable(false);

        list.setBounds(0, 0, 300, 600);
        list.setBorder(new LineBorder(Color.BLACK));

        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(280, 0, 20, 600);
        scrollPane.getViewport().add(list);

        String temp;
        String[] storeList = new String[1];
        ArrayList<String[]> itemList = new ArrayList<>();;
        try {
            temp = br.readLine();
            if (temp.equals("")) {
                storeList[0] = "No Stores!";
                itemList.add(new String[]{"No Items!"});
                confirm.setEnabled(false);
            } else {
                storeList = temp.split(",");
                itemList = new ArrayList<>();
                for (int i = 0; i < storeList.length; i++) {
                    temp = br.readLine();
                    itemList.add(temp.split(","));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        temp = "";
        for (int i = 0; i < storeList.length; i++) {
            temp += "Store: " + storeList[i].substring(storeList[i].indexOf(".") + 2) + "\nItems:\n";
            for (int j = 0; j < itemList.get(i).length; j++) {
                if (itemList.get(i)[j].equals("")) {
                    temp += "None.\n";
                } else {
                    temp += itemList.get(i)[j] + "\n";
                }
            }
            temp += "\n";
        }
        list.setText(temp);

        content.add(scrollPane);
        JPanel panel = new JPanel(new GridLayout(3,2, 0, 0));
        panel.add(storeLabel);
        panel.add(itemLabel);
        panel.add(storeText);
        panel.add(itemText);
        panel.add(cancel);
        panel.add(confirm);
        content.add(panel);

        ActionListener removeItemListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == confirm) {
                    String storeName = storeText.getText();
                    String itemName = itemText.getText();

                    pw.write(storeName + "," + itemName + "\n");
                    pw.flush();

                    try {
                        if (br.readLine().equals("-1")) {
                            JOptionPane.showMessageDialog(frame1, "Item was not removed, please fill out all fields correctly.");
                            //TODO YOU MAY NEED THIS YOU MAY NOT
                            pw.write("RemoveItem\n");
                            pw.flush();
                        } else {
                            JOptionPane.showMessageDialog(frame1, "Item was successfully removed.");
                            frame1.dispose();
                            manageInventory(socket, username, pw, br);
                        }
                    } catch (Exception k) {
                        k.printStackTrace();
                    }
                }
                if (e.getSource() == cancel) {
                    pw.write("cancel\n");
                    pw.flush();
                    frame1.dispose();
                    manageInventory(socket, username, pw, br);
                }
            }
        };

        confirm.addActionListener(removeItemListener);
        cancel.addActionListener(removeItemListener);

        frame1.setVisible(true);
    }

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
        JButton ViewPurchaseHistory;
        JButton exportPurchaseHistory;
        JButton Logout;

        viewMarket = new JButton("View Market");
        viewDashboard = new JButton("View Dashboard");
        ViewPurchaseHistory = new JButton("View Purchase History");
        exportPurchaseHistory = new JButton("Export Purchase History");
        Logout = new JButton("Logout");

        frame1.setTitle("Customer Interface");
        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(1, 1,0,0));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(5,1,0,0));

        panel1.add(viewMarket);
        panel1.add(viewDashboard);
        panel1.add(ViewPurchaseHistory);
        panel1.add(exportPurchaseHistory);
        panel1.add(Logout);

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
                    frame1.dispose();
                    pw.write("viewDashboard\n");
                    pw.flush();
                    //TODO: viewDashboard(socket,username, pw, br);
                }
                if (e.getSource() == ViewPurchaseHistory) {
                    frame1.dispose();
                    pw.write("ViewPurchaseHistory");
                    pw.flush();
                    pw.write(username);
                    pw.flush();
                    viewPurchaseHistory(socket, pw, br);
                }
                if (e.getSource() == exportPurchaseHistory) {
                    exportPurchaseHistory(socket, pw, br, username);
                }
                if (e.getSource() == Logout) {
                    pw.write("Logout\n");
                    pw.flush();
                    frame1.dispose();
                    register(socket, pw, br);
                }

            }
        };
        viewMarket.addActionListener(customerActionListeners);
        viewDashboard.addActionListener(customerActionListeners);
        ViewPurchaseHistory.addActionListener(customerActionListeners);
        exportPurchaseHistory.addActionListener(customerActionListeners);
        Logout.addActionListener(customerActionListeners);
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
        frame1.setSize(1200, 400);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //customer Buttons
        JLabel purchaseLabel;
        //TODO CHECK TO SEE IF THIS WORKS ONCE ADD ITEM IS DONE
        JComboBox stores;
        JComboBox items;

        JLabel searchPrompt; //search sub panel
        JComboBox marketSearchOptions; //search sub panel
        JTextField marketSearchBox; //search sub panel
        JButton searchMarketButton; //search sub panel

        JLabel sortPrompt; //search sub panel
        JComboBox marketSortOptions; //search sub panel
        JButton sortMarketButton; //sort sub panel

        JLabel purchaseQuantityPrompt; //purchase sub panel
        JTextField purchaseQuantity; //purchase sub panel
        JButton purchaseButton; //purchase sub panel

        JButton refresh;
        JButton cancel;
        JTextField textField;


        String fullString = "";
        for (int i = 0; i < storesList.length; i++) {
            fullString += storesList[i] + "\n";
            for (int j = 0; itemList.size() != 0 && j < itemList.get(i).length; j++) {
                fullString += itemList.get(i)[j] + "\n";
            }
        }

        purchaseLabel = new JLabel("Select an item if you wish to purchase it");

        searchPrompt = new JLabel("Search market by: "); //search sub panel
        marketSearchOptions = new JComboBox<>(new String[]{"Item name", "Store Name", "Item Description"}); //search sub panel
        marketSearchBox = new JTextField(15); // search sub panel
        searchMarketButton = new JButton("Search Market"); //search sub panel

        sortPrompt = new JLabel("Sort market by: "); //search sub panel
        marketSortOptions = new JComboBox<>(new String[]{"Item Price", "Quantity in Stock"}); //search sub panel
        sortMarketButton = new JButton("Sort Market"); //sort sub panel

        purchaseQuantityPrompt = new JLabel("Quantity");
        purchaseQuantity = new JTextField(2);
        purchaseButton = new JButton("Purchase");

        refresh = new JButton("Refresh");
        cancel = new JButton("Cancel");
        textField = new JTextField(fullString);

        //restrict buttons if there's nothing in the market
        if (storesList.length == 0) {
            stores = new JComboBox<>(new String[]{"No Stores Available"});
            purchaseButton.setEnabled(false);
            searchMarketButton.setEnabled(false);
            sortMarketButton.setEnabled(false);

        } else {
            stores = new JComboBox<>(storesList);
            purchaseButton.setEnabled(true);
        }
        if (itemList.size() == 0) {
            items = new JComboBox<>(new String[]{"No Items Available"});
            purchaseButton.setEnabled(false);
            searchMarketButton.setEnabled(false);
            sortMarketButton.setEnabled(false);

        } else if (itemList.get(0).length == 1 && itemList.get(0)[0].equals("")) {
            items = new JComboBox<>(new String[]{"No Items Available"});
            purchaseButton.setEnabled(false);
            searchMarketButton.setEnabled(false);
            sortMarketButton.setEnabled(false);

        } else {
            items = new JComboBox<>(itemList.get(0));
            purchaseButton.setEnabled(true);
            searchMarketButton.setEnabled(true);
            sortMarketButton.setEnabled(true);
        }

        //entire frame
        frame1.setTitle("Market_Interface");
        Container content = frame1.getContentPane();
        content.setLayout(new GridLayout(1, 2,0,0));

        //left side
        content.add(textField);

        //item/store sub panel
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridLayout(1,2,0,0));
        subPanel.add(stores);
        subPanel.add(items);

        //search sub panel
        JPanel searchMarketSubPanel = new JPanel();
        searchMarketSubPanel.setLayout(new GridLayout(1, 4, 0,0));
        searchMarketSubPanel.add(searchPrompt);
        searchMarketSubPanel.add(marketSearchOptions);
        searchMarketSubPanel.add(marketSearchBox);
        searchMarketSubPanel.add(searchMarketButton);

        //sort sub panel
        JPanel sortMarketSubPanel = new JPanel();
        sortMarketSubPanel.setLayout(new GridLayout(1, 3, 0, 0));
        sortMarketSubPanel.add(sortPrompt);
        sortMarketSubPanel.add(marketSortOptions);
        sortMarketSubPanel.add(sortMarketButton);

        //purchase sub panel
        JPanel purchaseSubPanel = new JPanel();
        purchaseSubPanel.setLayout(new GridLayout(1, 3, 0, 0));
        purchaseSubPanel.add(purchaseQuantityPrompt);
        purchaseSubPanel.add(purchaseQuantity);
        purchaseSubPanel.add(purchaseButton);

        //initializing entire panel
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(7,1,0,0));

        panel1.add(purchaseLabel);
        panel1.add(subPanel);
        panel1.add(searchMarketSubPanel);
        panel1.add(sortMarketSubPanel);
        panel1.add(purchaseSubPanel);
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
                        purchaseButton.setEnabled(false);

                    } else if (itemList.get(stores.getSelectedIndex()).length == 1
                            && itemList.get(stores.getSelectedIndex())[0].equals("")) {
                        items.addItem(new String[]{"No Items Available"});
                        purchaseButton.setEnabled(false);

                    } else {
                        for (int i = 0; i < itemList.get(stores.getSelectedIndex()).length; i++) {
                            items.addItem(itemList.get(stores.getSelectedIndex())[i]);
                            purchaseButton.setEnabled(true);
                        }
                    }
                }
                if (e.getSource() == searchMarketButton) {
                    pw.write("searchMarket"); //send that search market button was pushed
                    pw.flush();
                    pw.write(marketSearchOptions.getSelectedIndex()); //send type of search
                    pw.flush();
                    pw.write(marketSearchBox.getText()); //send search prompt
                    pw.flush();
                    searchMarket(socket, pw, br);
                    SwingUtilities.updateComponentTreeUI(frame1);
                }
                if (e.getSource() == sortMarketButton) {
                    pw.write("sortMarket"); //send that sort market button was pushed
                    pw.flush();
                    pw.write(marketSortOptions.getSelectedIndex()); //send type of sort
                    pw.flush();
                    frame1.dispose();
                    sortMarket(socket, pw, br);
                    viewMarket(socket, username, pw, br);
                }
                if (e.getSource() == purchaseButton) {
                    pw.write("purchase"); //send that purchase button was pushed
                    pw.flush();
                    pw.write(purchaseQuantity.getText()); //send desired purchase quantity
                    pw.flush();
                    pw.write(items.getSelectedIndex()); //send product name
                    pw.flush();
                    pw.write(username);
                    pw.flush();
                    JOptionPane.showMessageDialog(null, "You've successfully purchased " +
                                    purchaseQuantity + " " + (String)items.getSelectedItem() + "!", "Purchase Successful",
                            JOptionPane.PLAIN_MESSAGE);
                    SwingUtilities.updateComponentTreeUI(frame1);
                }
                if (e.getSource() == refresh) {
                    SwingUtilities.updateComponentTreeUI(frame1);
                }
                if (e.getSource() == cancel) {
                    frame1.dispose();
                    customer(socket, username, pw, br);
                }
            }
        };
        stores.addActionListener(marketActionListeners);
        searchMarketButton.addActionListener(marketActionListeners);
        sortMarketButton.addActionListener(marketActionListeners);
        purchaseButton.addActionListener(marketActionListeners);
        refresh.addActionListener(marketActionListeners);
        cancel.addActionListener(marketActionListeners);
        frame1.setVisible(true);
    }

    public static void searchMarket(Socket socket, PrintWriter pw, BufferedReader br) {
        try {

            JFrame viewSearchFrame = new JFrame();
            viewSearchFrame.setSize(400, 600);
            viewSearchFrame.setLocationRelativeTo(null);
            viewSearchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            viewSearchFrame.setTitle("Search Results");
            Container content = viewSearchFrame.getContentPane();
            content.setLayout(new BoxLayout(viewSearchFrame, BoxLayout.PAGE_AXIS));

            int numResults = Integer.parseInt(br.readLine());
            for (int i = 0; i < numResults; i++) {
                content.add(new JLabel(br.readLine()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sortMarket(Socket socket, PrintWriter pw, BufferedReader br) {
        try {
            //initialize frame
            JFrame viewSortFrame = new JFrame();
            viewSortFrame.setSize(400, 600);
            viewSortFrame.setLocationRelativeTo(null);
            viewSortFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            viewSortFrame.setTitle("Sort Results");
            Container content = viewSortFrame.getContentPane();
            content.setLayout(new BoxLayout(viewSortFrame, BoxLayout.PAGE_AXIS));

            int numResults = Integer.parseInt(br.readLine());
            for (int i = 0; i < numResults; i++) {
                content.add(new JLabel(br.readLine()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewPurchaseHistory(Socket socket, PrintWriter pw, BufferedReader br) {
        try {
            //frame buttons
            JButton cancelButton = new JButton("Cancel");

            //initialize frame
            JFrame viewPurchaseHistoryFrame = new JFrame();
            viewPurchaseHistoryFrame.setSize(400, 600);
            viewPurchaseHistoryFrame.setLocationRelativeTo(null);
            viewPurchaseHistoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            viewPurchaseHistoryFrame.setTitle("Transaction History");
            Container content = viewPurchaseHistoryFrame.getContentPane();
            content.setLayout(new GridLayout(1,2,0,0));

            JPanel panel1 = new JPanel();
            panel1.setLayout(new BoxLayout(viewPurchaseHistoryFrame, BoxLayout.PAGE_AXIS));

            content.add(panel1);
            content.add(cancelButton);

            int numResults = Integer.parseInt(br.readLine());
            for (int i = 0; i < numResults; i++) {
                panel1.add(new JLabel(br.readLine()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void exportPurchaseHistory(Socket socket, PrintWriter pr, BufferedReader br, String username) {
        JOptionPane.showMessageDialog(null, "Your purchase history file is now available to " +
                        "view in the depository under the name " + username + "_History.txt", "Export Purchase History",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
