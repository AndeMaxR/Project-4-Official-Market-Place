import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
public class MarketServer {
    
    private static Seller seller;
    private static Customer customer;
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4242);
        Socket socket = serverSocket.accept();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));


    }

    public static boolean customer(Socket socket) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            int menu1Option = Integer.parseInt(in.readLine());

            switch (menu1Option) {
                case 0: //Browse marketplace
                    int menu2Option = Integer.parseInt(in.readLine());
                    switch (menu2Option) {
                        case 0: //View whole marketPlace
                            if (!customer.viewMarket()) {
                                return true;
                            }
                            break;
                        case 1: //search marketplace
                            //TODO
                            break;
                        case 2: //SortMarketPlace
                            //TODO
                            break;
                    }
                    break;
                case 1: //View Purchase History
                    //TODO
                    break;
                case 2: //Logout
                    //TODO
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
