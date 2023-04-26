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
            register(socket);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //todo Make methods for each individual panel that return boolean value so that way we can move through out panels


    public static void register(Socket socket) {
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
        //MarketClient marketClient; No Idea what this is
        // create a new frame for the Welcome screen
        //todo add action Listeners ##############################################################
        //Added Cancel ActionListener
        ActionListener registerActions = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == cancel) {
                    frame.dispose();
                }
                // if login is pressed
                if (e.getSource() == login) {
                    login(socket, frame, usernameTextBox, passwordTextBox);
                }
                // if signup is pressed
                if (e.getSource() == signup) {
                    signup(socket, frame, usernameTextBox, passwordTextBox);
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
        frame.setVisible(true);

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
    public static void login(Socket socket, Frame frame, JTextField usernameTextBox, JTextField passwordTextBox) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.write("LOGIN," + usernameTextBox.getText() + "," + passwordTextBox.getText() + "\n");
            out.flush();
            out.close();

            int decision = Integer.parseInt(in.readLine());
            if (decision == 1) {
                JOptionPane.showMessageDialog(null,"You have successfully been logged in "
                        + usernameTextBox.getText() + "!", "Login", JOptionPane.PLAIN_MESSAGE);

                String username = usernameTextBox.getText();
                String password = passwordTextBox.getText();
                String type = in.readLine();
                in.close();
                if (type.contains("SELLER")) {
                    frame.setVisible(false);
                    seller(socket, username, password);
                } else {
                    frame.setVisible(false);
                    //customer(socket, username, password);
                }
            } else {
                in.close();
                JOptionPane.showMessageDialog(null,"Login failed, your username or"
                        + " password was incorrect", "Login", JOptionPane.PLAIN_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"IOException", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void signup(Socket socket, Frame frame, JTextField usernameTextBox, JTextField passwordTextBox) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String[] options = {"Seller", "Customer"};
            String type = (String) JOptionPane.showInputDialog(null, "Please chose type: ",
                    "Type", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            String username = usernameTextBox.getText();
            String password = passwordTextBox.getText();
            if (type.equals("Seller")) {
                out.write("SIGNUP," + username + "," + password + "," + type + "\n");
                out.flush();
                out.close();

                int decision = Integer.parseInt(in.readLine());
                if (decision == 1) {
                    JOptionPane.showMessageDialog(null,"You have successfully been signed-up" +
                            " and logged in"
                            + username + "!", "Sign-up", JOptionPane.PLAIN_MESSAGE);
                    in.close();
                    frame.setVisible(false);
                    seller(socket, username, password);
                } else {
                    in.close();
                    JOptionPane.showMessageDialog(null,"Sign-up failed, there is already an " +
                            "account with the same username", "Sign-up", JOptionPane.PLAIN_MESSAGE);
                }
            } else if (type.equals("Customer")) {
                out.write("SIGNUP," + usernameTextBox.getText() + "," + passwordTextBox.getText() + "," + type + "\n");
                out.flush();
                out.close();
                out.write("SIGNUP," + username + "," + password + "," + type + "\n");
                out.flush();
                out.close();

                int decision = Integer.parseInt(in.readLine());
                if (decision == 1) {
                    JOptionPane.showMessageDialog(null,"You have successfully been signed-up" +
                            " and logged in"
                            + username + "!", "Sign-up", JOptionPane.PLAIN_MESSAGE);
                    in.close();
                    frame.setVisible(false);
                    //TODO remove "//" from below after implementing customer
                    customer(socket, username, password);
                } else {
                    in.close();
                    JOptionPane.showMessageDialog(null,"Sign-up failed, there is already an " +
                            "account with the same username", "Sign-up", JOptionPane.PLAIN_MESSAGE);
                }
            } else {

            }
            System.out.println(type);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"IOException", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void seller(Socket socket, String username, String password) {

    }

    //TODO: implement the code below. THE CODE BELOW IS VERY IMPORTANT IT JUST NEEDS TO BE ALTERED to conform to updated CODE
    //IMPORTANT DO NOT DELETE
    public static void customer(Socket socket, String username, String password) {
        //customer Buttons
        JButton customerBrowseMarketPlaceButton;
        JButton customerViewPurchaseHistoryButton;
        JButton customerLogoutButton;
        JButton customerViewWholeMarketPlaceButton;
        JButton customerSearchMarketButton;
        JButton customerSortMarketPlaceButton;
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            JFrame frame = new JFrame();

            frame.setTitle("Customer Interface");
            Container content = frame.getContentPane();

            //FIRST MENU
            JPanel panel1 = new JPanel(); //panel1 is the first menu in customer
            content.setLayout(new BoxLayout(panel1, BoxLayout.PAGE_AXIS));

            customerBrowseMarketPlaceButton = new JButton("Browse Market Place");
            customerViewPurchaseHistoryButton = new JButton("View Purchase History");
            customerLogoutButton = new JButton("Logout");

            panel1.add(customerBrowseMarketPlaceButton); //sends the string "0" to server if clicked on
            panel1.add(customerViewPurchaseHistoryButton); //sends the string "1" to server if clicked on
            panel1.add(customerLogoutButton); //sends the string "2" to server if clicked on

            content.add(panel1);

            //SECOND MENU
            JPanel panel2 = new JPanel(); //panel2 is second menu in customer
            panel2.add(new JLabel("How would you like to view the market?/n"));

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
                    writer.write("0");
                    content.remove(panel1);
                    content.add(panel2);
                }
            });

            customerViewPurchaseHistoryButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("1");
                    writer.println();
                    writer.flush();
                }
            });

            customerLogoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("2");
                    writer.println();
                    writer.flush();
                }
            });

            //SECOND MENU LISTENER
            customerViewWholeMarketPlaceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("0");
                    writer.println();
                    writer.flush();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
