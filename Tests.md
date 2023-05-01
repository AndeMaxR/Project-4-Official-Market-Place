Test 1: User Sign Up

Steps:
  1. User launches application
  2. User selects the username labeled text box
  3. User enters username
  4. User selects the password labeled text box
  5. User enters password
  6. User selects the "Sign Up" button
  7. Select the type of object this user should be (customer or seller).
  8. Select "OK" to confirm the object.
 
These steps should result in a successful sign up.

Test 2: User Log-in

Steps:
  1. User (who already has an account) launches application
  2. User enters their username with the username labeled text box
  3. User enters their password with the password labeled text box
  4. User selects the "Log In" button

These steps should result in a successful login.

Test 3: Add Store

Steps:
  1. User uses test 1 or test 2 to login to their account
  2. User must be a seller for this feature.
  3. User selects "Manage Stores" button.
  4. User selects "Add Store" button
  5. User selects the text field labeled with "Store Name" 
  6. User enters the desired store name.
  7. User selects the "Enter" button to confirm.
 
These steps should result in a successful addition of a store.
To test this, you can go to the Remove Store interface (which will be discussed in the next test case) and see if the store that was added is inside of the drop down menu for removal.

Test 4: Remove Store

Steps:
  1. Use tests 1-3 to login and then create a store.
  2. Assuming the user is in the "Manage Stores" interface, select the "Remove Store" button.
  3. Use the drop down menu to find the store desired to be removed, and then select Enter to confirm.

These steps should result in the deletion of a previously added store.

The following tests deal with the Manage Inventory interface.

Test 5: Add Item

Steps:
  1. Use test 1 or 2 to login to the system.
  2. Create a store with test 3.
  3. In the Manage Stores Interface, click on the "Manage Inventory" button
  4. Click "Add Item" button.
  5. User enters the desired product name in the Product Name labeled text field.
  6. User enters the desired store name in the Store Name labeled text field.
  7. User enters the desired description of the product in the Product Description labeled text field.
  8. User enters the desired amount of that item are available in the Quantity Available labeled text field.
  9. User selects the Confirm button to add the item.

These steps should result in a successful item addition.
To test this, use the Remove Item interface to check if the item name results in a deletion.

Test 6: Remove Item

Steps:
  1. Use test 1 or 2 to login to the system.
  2. Create a store with test 3.
  3. Create a new item with test 5.
  4. Assuming the user is in the Manage Inventory interface now, click the "Remove Item" button.
  5. Enter the desired item name that is to be removed in the text box, then select the Confirm button.

These steps should result in a successful deletion of a previously added item.

MORE TO ADD TO THIS!
