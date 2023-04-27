import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

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

    // listen for buttons being pressed

    // button methods
    public void loginMethod() {

    }
    public void signupMethod() {

    }
    public void cancelMethod() {

    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 4242);
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            register(socket, pw, br);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
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
                        socket.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
    public static void login(Socket socket, JFrame frame, JTextField usernameTextBox, JTextField passwordTextBox,
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
                    frame.setVisible(false);
                    seller(socket, username, password, pw, br);
                } else {
                    frame.setVisible(false);
                    customer(socket, username, password, pw, br);
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

    public static void signup(Socket socket, JFrame frame, JTextField usernameTextBox, JTextField passwordTextBox,
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
                    frame.setVisible(false);
                    seller(socket, username, password, pw, br);
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
                            " and logged in"
                            + username + "!", "Sign-up", JOptionPane.PLAIN_MESSAGE);
                    frame.setVisible(false);
                    //TODO remove "//" from below after implementing customer
                    customer(socket, username, password, pw, br);
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

    public static void seller(Socket socket, String username, String password, PrintWriter pw,
                              BufferedReader br) {
        JButton sellerManageMyStores;
        JButton sellerViewMyFinances;
        JButton sellerLogout;

        JButton sellerEditStore;
        JButton sellerAddStore;
        JButton sellerRemoveStore;
        JButton sellerManageCancel;

        JButton sellerModifyItems;
        JButton sellerModifyStoreInfo;
        JButton sellerModifyStoreCancel;

        JButton sellerAddItem;
        JButton sellerRemoveItem;
        JButton sellerEditItem;
        JButton sellerItemCancel;

        JButton sellerRemoveStoreYes;
        JButton sellerRemoveStoreNo;
        try {
            JFrame frame = new JFrame("Seller Interface");

            frame.setSize(430, 150);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel parentPanel = new JPanel();
            parentPanel.setLayout(new BorderLayout(10, 10));
            // create top panel
            JPanel panel1 = new JPanel();
            BoxLayout horizontal = new BoxLayout(panel1, BoxLayout.X_AXIS);
            panel1.setLayout(horizontal);
            sellerManageMyStores = new JButton("Manage my stores");
            sellerViewMyFinances = new JButton("View my your Finances");
            sellerLogout = new JButton("Logout");
            panel1.add(sellerManageMyStores);
            panel1.add(sellerViewMyFinances);
            panel1.add(sellerLogout);
            FlowLayout flow = new FlowLayout();
            frame.setLayout(flow);
            parentPanel.add(panel1, BorderLayout.CENTER);
            frame.add(parentPanel);
            frame.setVisible(true);

            //SECOND MENU
            JPanel panel2 = new JPanel(); //panel2 is second menu in customer
            panel2.add(new JLabel("What would you like to do?"));

            sellerEditStore = new JButton("Edit a Store");
            sellerAddStore = new JButton("Add a Store");
            sellerRemoveStore = new JButton("Remove a Store");
            sellerManageCancel = new JButton("Cancel");

            panel2.add(sellerEditStore);
            panel2.add(sellerAddStore);
            panel2.add(sellerRemoveStore);
            panel2.add(sellerManageCancel);



            JPanel modifyItem = new JPanel(); //panel2 is second menu in customer
            modifyItem.add(new JLabel("How would you like to modify your Items?"));

            sellerAddItem = new JButton("Add Item");
            sellerRemoveItem = new JButton("Remove Item");
            sellerEditItem = new JButton("Edit an Item");
            sellerItemCancel = new JButton("Cancel");

            modifyItem.add(sellerAddItem);
            modifyItem.add(sellerRemoveItem);
            modifyItem.add(sellerEditItem);
            modifyItem.add(sellerItemCancel);

            JPanel addPanel = new JPanel(); //panel2 is second menu in customer
            addPanel.add(new JLabel("Store Name:"));
            JTextField storeName = new JTextField("",20);
            storeName.setSize(20, 5);
            addPanel.add(storeName);
            JButton add = new JButton("Add");
            addPanel.add(add);

            JPanel modifyStore = new JPanel(); //panel2 is second menu in customer
            modifyStore.add(new JLabel("What do you want to do to this store?"));

            sellerModifyItems = new JButton("Modify Items");
            sellerModifyStoreInfo = new JButton("Modify the Stores information");
            sellerModifyStoreCancel = new JButton("Cancel");

            modifyStore.add(sellerModifyItems);
            modifyStore.add(sellerModifyStoreInfo);
            modifyStore.add(sellerModifyStoreCancel);


            JPanel removeStorePanel = new JPanel();
            removeStorePanel.add(new JLabel("Would you like to remove another store?"));

            sellerRemoveStoreYes = new JButton("Yes");
            sellerRemoveStoreNo = new JButton("No");

            removeStorePanel.add(sellerRemoveStoreYes);
            removeStorePanel.add(sellerRemoveStoreNo);

            //ACTION LISTENER

            //FIRST MENU LISTENER
            sellerManageMyStores.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.write("1\n");
                    pw.flush();
                    try {
                        br.readLine();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    parentPanel.remove(panel1);
                    parentPanel.add(panel2, BorderLayout.CENTER);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                    frame.pack();
                }
            });

            sellerViewMyFinances.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    writer.write("1");
//                    writer.println();
//                    writer.flush();
                }
            });

            sellerLogout.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    writer.write("2");
//                    writer.println();
//                    writer.flush();
                }
            });
            final String[] storeEditSelected = {""};
            sellerEditStore.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.write("1\n");
                    pw.flush();
                    try {
                        String earlierStores = br.readLine();
                        String[] options = earlierStores.split("#");
                        storeEditSelected[0] = (String) JOptionPane.showInputDialog(null, "Which store would you like to edit?",
                                "Type", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        parentPanel.remove(panel2);
                        parentPanel.add(modifyStore, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            final String[] storeRemoveSelected = {""};
            sellerRemoveStore.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.write("3\n");
                    pw.flush();
                    try {
                        String earlierStores = br.readLine();
                        String[] options = earlierStores.split("#");
                        storeRemoveSelected[0] = (String) JOptionPane.showInputDialog(null, "Which store would you like to remove?",
                                "Type", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        parentPanel.remove(panel2);
                        parentPanel.add(removeStorePanel, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                        int i=0;
                        for(String s: options) {
                            if (s.equals(storeRemoveSelected[0])) {
                                break;
                            }
                            i++;
                        }
                        pw.write(i+"\n");
                        pw.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            sellerAddStore.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.write("2\n");
                    pw.flush();
                    try {
                        br.readLine();
                        parentPanel.remove(panel2);
                        parentPanel.add(addPanel, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            sellerModifyItems.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.write("1\n");
                    pw.flush();
                    try {
                        br.readLine();
                        parentPanel.remove(modifyStore);
                        parentPanel.add(modifyItem, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.write(storeName.getText()+"\n");
                    pw.flush();
                    try {
                        br.readLine();
                        parentPanel.remove(addPanel);
                        parentPanel.add(panel2, BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: implement the code below. THE CODE BELOW IS VERY IMPORTANT IT JUST NEEDS TO BE ALTERED to conform to updated CODE
    //IMPORTANT DO NOT DELETE
    public static void customer(Socket socket, String username, String password, PrintWriter pw,
                                BufferedReader br) {
        //customer Buttons
        JButton customerBrowseMarketPlaceButton;
        JButton customerViewPurchaseHistoryButton;
        JButton customerLogoutButton;
        JButton customerViewWholeMarketPlaceButton;
        JButton customerSearchMarketButton;
        JButton customerSortMarketPlaceButton;
        try {
            JFrame frame = new JFrame("Customer Interface");

            frame.setSize(430, 150);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel parentPanel = new JPanel();
            parentPanel.setLayout(new BorderLayout(10, 10));
            // create top panel
            JPanel panel1 = new JPanel();
            BoxLayout horizontal = new BoxLayout(panel1, BoxLayout.X_AXIS);
            panel1.setLayout(horizontal);
            customerBrowseMarketPlaceButton = new JButton("Browse Market Place");
            customerViewPurchaseHistoryButton = new JButton("View Purchase History");
            customerLogoutButton = new JButton("Logout");
            panel1.add(customerBrowseMarketPlaceButton);
            panel1.add(customerViewPurchaseHistoryButton);
            panel1.add(customerLogoutButton);
            FlowLayout flow = new FlowLayout();
            frame.setLayout(flow);
            parentPanel.add(panel1, BorderLayout.CENTER);
            frame.add(parentPanel);
            frame.setVisible(true);

            //SECOND MENU
            JPanel panel2 = new JPanel(); //panel2 is second menu in customer
            panel2.add(new JLabel("How would you like to view the market?"));

            customerViewWholeMarketPlaceButton = new JButton("View Entire Market Place");
            customerSearchMarketButton = new JButton("Search");
            customerSortMarketPlaceButton = new JButton("Sort Market");

            panel2.add(customerViewWholeMarketPlaceButton); //sends the string "0" to server if clicked on
            panel2.add(customerSearchMarketButton); //sends the string "1" to server if clicked on
            panel2.add(customerSortMarketPlaceButton); //sends the string "2" to server if clicked on

            //ACTION LISTENER

            //FIRST MENU LISTENER
            customerBrowseMarketPlaceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.write("1\n");
                    pw.flush();
                    try {
                        br.readLine();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    parentPanel.remove(panel1);
                    parentPanel.add(panel2, BorderLayout.CENTER);
                    parentPanel.revalidate();
                    parentPanel.repaint();
                    frame.pack();
                }
            });

            customerViewPurchaseHistoryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    writer.write("2");
//                    writer.println();
//                    writer.flush();
                }
            });

            customerLogoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    writer.write("3");
//                    writer.println();
//                    writer.flush();
                }
            });

            //SECOND MENU LISTENER
            customerViewWholeMarketPlaceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pw.write("1\n");
                    pw.flush();
                    try {
                        String data = br.readLine();
                        parentPanel.remove(panel2);
                        parentPanel.add(new JLabel(data), BorderLayout.CENTER);
                        parentPanel.revalidate();
                        parentPanel.repaint();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
