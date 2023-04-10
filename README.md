# README
The descriptions of the products should not exceed 200 characters.
We did not implement a JUnit test class, because from our understanding, a main method utilizing all classes and methods was sufficient. All testing is and should be done with the main method that is implemented inside of the Market class. 
For testing, you must use/create a seller first and add an item in order to create or login as a buyer and buy a product. 
(Think of it this way: how can a buyer buy a product that doesn't exist).
But, we handled the case where this occurs, so try creating and logging in as a buyer, to test the edge case where no products exist. 
The program runs infinitely until you enter 3 to cancel, so you can test all cases without having to re-run the program multiple times. 
Here is an example test case where brackets indicate input:
Hello an welcome to Emezon, the right place to be for all your shopping needs!
Would you like to 
1. log-in
2. sign-up?
3. Cancel
[2]
Please enter your username:
[jack]
Please enter your password:
[1234]
What would you like to sign up as?
1. shopper
2. seller
3. Cancel
[2]
Thank you for signing up, your account has been created, and are now logged in!
Hello jack what would you like to do?
1. Manage my stores.
2. View your Finances.
3. Manage Account.
4. Logout.
[1]
What would you like to do?
1. Edit a Store.
2. Add a Store.
3. Remove a Store.
4. Cancel.
[2]
Please enter the name of the store:
Target
You have added the store Target to your account.
You will now be able to edit your store in "Edit a Store".
What would you like to do?
1. Edit a Store.
2. Add a Store.
3. Remove a Store.
4. Cancel.
[1]
Which store would you like to edit?
1. Target
2. Cancel
[1]
What do you want to do to this store?
1. Modify Items.
2. Modify the Stores information.
3. Cancel.
[1]
How would you like to modify your Items?
1. Add Item.
2. Remove Item.
3. Edit an Item.
4. Cancel.
[1]
Please enter the product name:
[Water Bottle]
Please enter the product description name:
[An insulated water bottle]
Please enter the quantity of available products:
[3]
Please enter the price:
[10.00]
How would you like to modify your Items?
1. Add Item.
2. Remove Item.
3. Edit an Item.
4. Cancel.
[4]
Cancelling...
What do you want to do to this store?
1. Modify Items.
2. Modify the Stores information.
3. Cancel.
[3]
Cancelling...
Which store would you like to edit?
1. Target
2. Cancel
[2]
Cancelling...
What would you like to do?
1. Edit a Store.
2. Add a Store.
3. Remove a Store.
4. Cancel.
[4]
Returning to main menu...
Hello jack what would you like to do?
1. Manage my stores.
2. View your Finances.
3. Manage Account.
4. Logout.
[4]
Thank you jack, you have been successfully logged out.
Hello an welcome to Emezon, the right place to be for all your shopping needs!
Would you like to 
1. log-in
2. sign-up?
3. Cancel
[3]
Goodbye, come back soon!

Note: for some reason, GitHub thinks that after the first numbered list, the rest of the test is numbered for some reason.
If this type of test case is not sufficient and we should use JUnit instead, are we allowed to get the points back on PJ5? We were on a great time constraint here for various reasons.
