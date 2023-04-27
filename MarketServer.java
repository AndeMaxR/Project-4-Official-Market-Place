import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class MarketServer {

    public static ArrayList<Store> storeMasterArrayList;

    public static void main(String[] args) throws IOException {
        storeMasterArrayList = new ArrayList<>();
        File storeMasterList = new File("StoreMasterList.txt");
        if (storeMasterList.exists()) {
            String content;
            BufferedReader bfr = new BufferedReader(new FileReader(storeMasterList));
            while (true) {
                content = bfr.readLine();
                if (content == null) {
                    break;
                } else {
                    String owner = content.substring(0, content.indexOf("_"));
                    String storeName = content.substring(content.indexOf("_") + 1, content.indexOf("."));
                    storeMasterArrayList.add(new Store(owner, storeName));
                }
            }
        }

        ServerSocket serverSocket = new ServerSocket(4242);
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Thread thread = new ClientHandler(pw, br, socket, storeMasterArrayList);
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
                socket.close();
            }
        }
    }
}