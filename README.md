# README
To compile this project, make sure to run the MarketServer class first, followed by the MarketClient. This ensures that the Client class will compile because it will be able to connect the server to the client with sockets.

Mark submitted the report, presentation, and the project.

Descriptions of classes:

MarketClient:
  This class handles all of the GUI and sends messages across the socket to the server and ClientHandler for processing.
  
MarketServer: 
  This class establishes the connection over a socket with the MarketClient class.

ClientHandler:
  This class deals with all of the processing of information and utilizes functions based off of user input (decided by what the user does on the frame in GUI), which is sent over the socket, then sends the new information back to the MarketClient.
  
Customer:
  Customer is a class that enables customer (buyer) objects to be utilized in the MarketClient and ClientHandler classes so they can buy products from sellers.

Seller:
  Seller is a class that allows merchants to sell items to customers, and they can also do other various functions like manage their stores, add new items, etc.

Item:
  Item is a class that is used for product objects. Buyers and sellers can utilize these, where buyers can purchase items and sellers can sell them across their stores.
 
Store:
  Store is a class where sellers can add items to their store, remove items from their store, etc. for buyers to buy items from the seller's store. This class allows for sellers to have multiple stores and hold multiple products in the same place.
