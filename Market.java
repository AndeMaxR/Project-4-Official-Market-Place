import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
public class Market {
    private static Seller seller;
    private static Customer customer;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (login(scanner)) {
            if (seller != null) {
                while (sellerManipulation(scanner)) {

                }
            }
            if (customer != null) {
                while (customerManipulation(scanner)) {

                }
            }
        }

    }

    public static boolean login(Scanner scanner) {
        String userNameHolder;
        String passwordHolder;
        String decision;
        System.out.println("Hello an welcome to Emezon, the right place to be for all your shopping needs!\n" +
                "Would you like to \n1. log-in\n2. sign-up?\n3. Cancel");
        decision = scanner.nextLine();
        switch (decision) {
            case "1":
                while (true) {
                    System.out.println("Please enter your username:");
                    userNameHolder = scanner.nextLine();
                    System.out.println("Please enter your password:");
                    passwordHolder = scanner.nextLine();
                    File file1 = new File(userNameHolder + ".txt");
                    if (file1.exists()) {
                        try {
                            BufferedReader bfr = new BufferedReader(new FileReader(file1));
                            String content = bfr.readLine();
                            if (content.contains(passwordHolder)) {
                                if (content.contains("SELLER")) {
                                    seller = new Seller(userNameHolder, passwordHolder);

                                    System.out.println("You are now logged in!");
                                    return true;
                                }
                                if (content.contains("CUSTOMER")) {
                                    customer = new Customer(userNameHolder, passwordHolder);
                                    System.out.println("You are now logged in " + userNameHolder + "!");
                                    return true;
                                }
                            } else {
                                System.out.println("Your Username or Password is incorrect.\n1. Try again?\n2. Return to main menu.");
                                decision = scanner.nextLine();
                                if (decision.equals("2")) {
                                    return true;
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("Your Username or Password is incorrect");
                            return true;
                        }
                    } else {
                        System.out.println("Your Username or Password is incorrect.\n1. Try again?\n2. Return to main menu.");
                        decision = scanner.nextLine();
                        if (decision.equals("2")) {
                            return true;
                        }

                    }
                }
            case "2":
                System.out.println("Please enter your username:");
                userNameHolder = scanner.nextLine();
                System.out.println("Please enter your password:");
                passwordHolder = scanner.nextLine();
                File file2 = new File(userNameHolder + ".txt");
                if (!file2.exists()) {
                    while (true) {
                        System.out.println("What would you like to sign up as?\n1. shopper\n2. seller\n3. Cancel");
                        decision = scanner.nextLine();
                        switch (decision) {
                            case "1":
                                customer = new Customer(userNameHolder, passwordHolder);
                                System.out.println("Thank you for signing up, your account has been created" +
                                        ", and are now logged in!");
                                return true;
                            case "2":
                                seller = new Seller(userNameHolder, passwordHolder);
                                System.out.println("Thank you for signing up, your account has been created" +
                                        ", and are now logged in!");
                                return true;
                            case "3":
                                System.out.println("Cancelling sign-up, you will be return to the main menu.");
                                return true;
                            default:
                                System.out.println("Please enter a valid input.");
                        }
                    }
                } else {
                    System.out.println("You there is already an account registered with that username.");
                    return true;
                }
            case "3":
                System.out.println("Goodbye, come back soon!");
                return false;
            default:
                System.out.println("Please enter a valid input.");

        }
        return true;
    }


    public static boolean sellerManipulation(Scanner scanner) {
        String decision;
        System.out.println("Hello " + seller.getUsername() + " what would you like to do?\n1. Manage my stores.\n2. View my your Finances.\n3. Manage Account.\n4. Logout.");
        decision = scanner.nextLine();
        switch (decision) {
            case "1":
                while (true) {
                    System.out.println("What would you like to do?\n1. Edit a Store.\n2. Add a Store.\n3. Remove a Store.\n4. Cancel.");
                    decision = scanner.nextLine();
                    switch (decision) {
                        case "1":
                            while (true) {

                                String holder;
                                holder = seller.getStoreList();
                                if (holder.equals("")) {
                                    System.out.println("You do not own any stores to edit!");
                                    break;
                                } else {
                                    System.out.println("Which store would you like to edit?");
                                    System.out.println(holder + (seller.getFullStoreList().size() + 1) + ". Cancel");
                                    decision = scanner.nextLine();
                                    try {
                                        int location = Integer.parseInt(decision);
                                        if (location == seller.getFullStoreList().size() + 1) {
                                            System.out.println("Cancelling...");
                                            break;
                                        } else if (holder.contains(decision)) {
                                            while (true) {
                                                System.out.println("What do you want to do to this store?\n1. Modify Items.\n2. Modify the Stores information.\n3. Cancel.");
                                                decision = scanner.nextLine();
                                                if (decision.equals("1")) {
                                                    while (true) {
                                                        //TODO STARTS HERE
                                                        System.out.println("How would you like to modify your Items?\n1. Add Item.\n2. Remove Item.\n3. Edit an Item.\n4. Cancel.");
                                                        Store store = seller.getSpecificStore(location - 1);
                                                        decision = scanner.nextLine();
                                                        if (decision.equals("1")) {
                                                            System.out.println("Please enter the product name:");
                                                            String name = scanner.nextLine();
                                                            String storeName = seller.getSpecificStore(location - 1).getStoreName();
                                                            System.out.println("Please enter the product description name:");
                                                            String description = scanner.nextLine();
                                                            int available = 0;
                                                            double price = 0;
                                                            while (true) {
                                                                try {
                                                                    System.out.println("Please enter the quantity of available products:");
                                                                    available = Integer.parseInt(scanner.nextLine());
                                                                    System.out.println("Please enter the price:");
                                                                    price = Double.parseDouble(scanner.nextLine());
                                                                    break;
                                                                } catch (Exception e) {
                                                                    System.out.println("Please enter a valid input");
                                                                }
                                                            }
                                                            store.addItem(new Item(name, storeName, description, available, price));
                                                        } else if (decision.equals("2")) {
                                                            String list = store.getItemList()
                                                                    + (store.getItemListSize() + 1) + ". Cancel.";
                                                            System.out.println(list);
                                                            decision = scanner.nextLine();
                                                            try {
                                                                int index = Integer.parseInt(decision) - 1;
                                                                if (index == store.getItemListSize() + 1) {
                                                                    System.out.println("Cancelling...");
                                                                } else {
                                                                    store.removeItem(index);
                                                                }
                                                            } catch (Exception e) {
                                                                System.out.println("Please enter a valid input");
                                                                e.printStackTrace();
                                                            }
                                                        } else if (decision.equals("3")) {
                                                            String list = store.getItemList()
                                                                    + (store.getItemListSize() + 1) + ". Cancel.";
                                                            System.out.println(list);
                                                            decision = scanner.nextLine();
                                                            try {
                                                                int index = Integer.parseInt(decision) - 1;
                                                                if (index == store.getItemListSize() + 1) {
                                                                    System.out.println("Cancelling...");
                                                                } else {
                                                                    System.out.println("Please enter the product name:");
                                                                    String name = scanner.nextLine();
                                                                    System.out.println("Please enter the store name:");
                                                                    String storeName = scanner.nextLine();
                                                                    System.out.println("Please enter the product description name:");
                                                                    String description = scanner.nextLine();
                                                                    int available = 0;
                                                                    double price = 0;
                                                                    while (true) {
                                                                        try {
                                                                            System.out.println("Please enter the quantity of available products:");
                                                                            available = Integer.parseInt(scanner.nextLine());
                                                                            System.out.println("Please enter the price:");
                                                                            price = Double.parseDouble(scanner.nextLine());
                                                                            break;
                                                                        } catch (Exception e) {
                                                                            System.out.println("Please enter a valid input");
                                                                        }
                                                                    }
                                                                    store.editItem(index, name, storeName, description, available, price);
                                                                }
                                                            } catch (Exception e) {
                                                                System.out.println("Please enter a valid input");
                                                                e.printStackTrace();
                                                            }
                                                        } else if (decision.equals("4")) {
                                                            System.out.println("Cancelling...");
                                                            break;
                                                        } else {
                                                            System.out.println("Please enter a valid input");
                                                        }
                                                        //TODO ENDS HERE
                                                    }
                                                } else if (decision.equals("2")) {
                                                    while (true) {
                                                        System.out.println("How would you like to modify the store?\n 1. Change Store Name.\n2. Cancel.");
                                                        decision = scanner.nextLine();
                                                        if (decision.equals("1")) {
                                                            System.out.println("Please enter the new store name.");
                                                            String newName = scanner.nextLine();
                                                            seller.getSpecificStore(location - 1).setStoreName(newName);
                                                        } else if (decision.equals("2")) {
                                                            System.out.println("Cancelling...");
                                                            break;
                                                        } else {
                                                            System.out.println("Please enter a valid input");
                                                        }
                                                    }
                                                } else if (decision.equals("3")) {
                                                    System.out.println("Cancelling...");
                                                    break;
                                                } else {
                                                    System.out.println("Please enter a valid input");
                                                }
                                            }
                                        } else {
                                            System.out.println("Please enter a valid input");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Please enter a valid input");
                                        e.printStackTrace();
                                    }
                                }
                            }
                            break;
                        case "2":
                            System.out.println("Please enter the name of the store:");
                            String nameOfStore = scanner.nextLine();
                            seller.addStore(new Store(seller.getUsername(), nameOfStore));
                            System.out.println("You have added the store " + nameOfStore + " to your account." +
                                    "\nYou will now be able to edit your store in \"Edit a Store\".");
                            break;
                        case "3":
                            String holder;
                            holder = seller.getStoreList();
                            if (holder.equals("")) {
                                System.out.println("You do not own any stores!");
                                break;
                            } else {
                                System.out.println("Which store would you like to remove?");
                                System.out.println(holder + (seller.getFullStoreList().size() + 1) + ". Cancel");
                                decision = scanner.nextLine();
                                try {
                                    int location = Integer.parseInt(decision);
                                    if (location == seller.getFullStoreList().size() + 1) {
                                        System.out.println("Cancelling...");
                                    } else if (holder.contains(decision)) {
                                        seller.removeStore(location - 1);
                                        System.out.println("Would you like to remove another store?" +
                                                "\n1. Yes\n2. No");
                                        decision = scanner.nextLine();
                                        while (true) {
                                            if (decision.equals("1")) {
                                                break;
                                            } else if (decision.equals("2")) {
                                                return true;
                                            } else {
                                                System.out.println("Please enter a valid input");
                                            }
                                        }

                                    } else {
                                        System.out.println("Please enter a valid input");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Please enter a valid input");
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case "4":
                            System.out.println("Returning to main menu...");
                            return true;
                        default:
                            System.out.println("Please enter a valid input");
                    }
                }
            case "2":
                while (true) {
                    String holder;
                    holder = seller.getStoreList();
                    if (holder.equals("")) {
                        System.out.println("You do not own any stores!");
                        break;
                    } else {
                        System.out.println("For which store would you like to view your finances?");
                        System.out.print(holder);
                        decision = scanner.nextLine();
                        try {
                            int location = Integer.parseInt(decision);
                            if (holder.contains(decision)) {
                                System.out.print(seller.getSpecificStore(location - 1).getFinances());
                                System.out.println("Would you like to view your finances for another store?" +
                                        "\n1. Yes\n2. No");
                                decision = scanner.nextLine();
                                while (true) {
                                    if (decision.equals("1")) {
                                        break;
                                    } else if (decision.equals("2")) {
                                        return true;
                                    } else {
                                        System.out.println("Please enter a valid input");
                                    }
                                }

                            } else {
                                System.out.println("Please enter a valid input");
                            }
                        } catch (Exception e) {
                            System.out.println("Please enter a valid input");
                            e.printStackTrace();
                        }
                    }

                }
                return true;
            case "3":
                //TODO
            case "4":
                System.out.println("Thank you " + seller.getUsername() + ", you have been successfully logged out.");
                seller = null;
                return false;
            default:
                System.out.println("Please enter a valid input.");
                return true;
        }
    }

    public static boolean customerManipulation(Scanner scanner) {
        String decision;
        System.out.println("What would you like to do? " +
                "\n1. Browse marketplace." +
                "\n2. View purchase history." +
                "\n3. Logout.");
        decision = scanner.nextLine();
        while (true) {
            boolean areThereItemsToView = true;
            switch (decision) {
                case "1":
                    String viewMarket;
                    System.out.println("How would you like to view the market place?" +
                            "\n1. View entire market place." +
                            "\n2. Search." +
                            "\n3. Sort." +
                            "\n4. Cancel.");
                    viewMarket = scanner.nextLine();
                    switch (viewMarket) {
                        case "1":
                            if (!customer.viewMarket()) {
                                return true;
                            }
                            break;

                        case "2":
                            boolean searchAgain = true;
                            while (true) {
                                String searchBy;
                                System.out.println("What would you like to search for?" +
                                        "\n1. Item Name." +
                                        "\n2. Store Name." +
                                        "\n3. Item Description." +
                                        "\n4. Cancel");
                                searchBy = scanner.nextLine();
                                if (searchBy.equals("1")) {
                                    String itemName;
                                    System.out.println("Please enter your search for Item Name");
                                    itemName = scanner.nextLine();
                                    areThereItemsToView = customer.viewMarketNameSort(itemName);
                                } else if (searchBy.equals("2")) {
                                    String storeName;
                                    System.out.println("Please enter your search for Store Name");
                                    storeName = scanner.nextLine();
                                    areThereItemsToView = customer.viewMarketStoreSort(storeName);
                                } else if (searchBy.equals("3")) {
                                    String itemDesc;
                                    System.out.println("Please enter your search for Item Description");
                                    itemDesc = scanner.nextLine();
                                    areThereItemsToView = customer.sortDescription(itemDesc);
                                } else if (searchBy.equals("4")) {
                                    System.out.println("Cancelling...");
                                    break;
                                } else {
                                    System.out.println("Invalid input!");
                                    continue;
                                }
                                String answer;
                                System.out.println("Would you like to search again?" +
                                        "\n1. Yes." +
                                        "\n2. No.");
                                answer = scanner.nextLine();
                                if (answer.equals("2")) {
                                    break;
                                }
                            }
                            break;

                        case "3":
                            boolean sortAgain = true;
                            while (true) {
                                String sortMethod;
                                System.out.println("How would you like to sort the market?" +
                                        "\n1. Price." +
                                        "\n2. Available Quantity." +
                                        "\n3. Cancel.");
                                sortMethod = scanner.nextLine();
                                if (sortMethod.equals("1")) {
                                    customer.sortMarketPrice();
                                } else if (sortMethod.equals("2")) {
                                    customer.sortMarketQuantity();
                                } else if (sortMethod.equals("3")) {
                                    System.out.println("Cancelling");
                                    break;
                                } else {
                                    System.out.println("Invalid input.");
                                    continue;
                                }
                                String answer;
                                System.out.println("Would you like to sort again?" +
                                        "\n1. Yes." +
                                        "\n2. No.");
                                answer = scanner.nextLine();

                                if (answer.equals("2")) {
                                    break;
                                }
                            }
                            break;
                        case "4":
                            System.out.println("Canceling...");
                            return true;
                        default:
                            System.out.println("Invalid input!");
                            continue;
                    }
                    if (areThereItemsToView) {
                        String viewItem;
                        System.out.println("Would you like to view an item?" +
                                "\n1. Yes." +
                                "\n2. No.");
                        viewItem = scanner.nextLine();
                        boolean viewAgain = true;
                        switch (viewItem) {
                            case "1":
                                do {
                                    String productName;
                                    String storeName;
                                    System.out.println("Please enter the product name exactly as given.");
                                    productName = scanner.nextLine();
                                    System.out.println("Please enter the store name exactly as given.");
                                    storeName = scanner.nextLine();
                                    int storeListSize = customer.getStoreArrayList().size();
                                    int storeLocation = 0;
                                    int itemLocation = 0;
                                    for (int i = 0; i < storeListSize; i++) {
                                        int itemListSize = customer.getStoreArrayList().get(i).getItemListSize();
                                        for (int j = 0; j < itemListSize; j++) {
                                            if (customer.getStoreArrayList().get(i).getItem(j).getProductName()
                                                    .equals(productName) && customer.getStoreArrayList().get(i)
                                                    .getStoreName().equals(storeName)) {
                                                storeLocation = j;
                                                itemLocation = i;
                                                System.out.println(customer.getStoreArrayList()
                                                        .get(i).getItem(j).toString());
                                            }
                                        }
                                    }

                                    String makePurchase;
                                    System.out.println("Would you like to purchase this item?" +
                                            "\n1. Yes." +
                                            "\n2. No");
                                    makePurchase = scanner.nextLine();
                                    switch (makePurchase) {
                                        case "1":
                                            int quantity = 0;
                                            while (true) {
                                                try {
                                                    System.out.println("How many would you like to buy?");
                                                    quantity = Integer.parseInt(scanner.nextLine());
                                                    if (quantity >= 0) {
                                                        break;
                                                    } else {
                                                        System.out.println("Invalid input, please enter a valid number.");
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println("Invalid input, please enter a valid number.");
                                                }
                                            }
                                            customer.getStoreArrayList().get(storeLocation).buyItem(itemLocation, quantity, customer.getUsername());
                                            System.out.printf("You successfully purchased %s!\n", productName);
                                            return true;

                                        case "2":
                                            String answer;
                                            System.out.println("Would you like to view another Item?" +
                                                    "\n1. Yes." +
                                                    "\n2. No.");
                                            answer = scanner.nextLine();
                                            if (answer.equals("2")) {
                                                return true;
                                            }
                                            break;
                                    }
                                } while (viewAgain);
                                break;
                            case "2":
                                System.out.println("You selected to not view an item.");
                                break;
                            default:
                                System.out.println("Invalid input!");
                                continue;
                        }
                    }
                    break;
                case "2":
                    customer.purchaseHistory(customer.getUsername());
                    return true;
                //
                case "3":
                    System.out.println("Thank you " + customer.getUsername() + ", you have been successfully logged out.");
                    customer = null;
                    return false;
                default:
                    System.out.println("Please enter a valid input!");
            }
        }
    }

}
