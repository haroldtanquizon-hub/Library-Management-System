import java.io.*;
import java.time.LocalDate;
import java.util.*;


public class LibrarySystem {
   List<User> users = new ArrayList<>();
   List<Book> books = new ArrayList<>();
   List<Transaction> transactions = new ArrayList<>();
   User loggedInUser = null;




   public void loadUsers() {
       users.clear();
       try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
           String line;
           while ((line = reader.readLine()) != null) {
               String[] parts = line.split(",");
               if (parts.length == 4) {
                   users.add(new User(parts[0], parts[1], parts[2], parts[3]));
               }
           }
       } catch (FileNotFoundException e) {
           System.out.println("users.txt not found. Will create on save.");
       } catch (IOException e) {
           System.out.println("Error reading users.txt: " + e.getMessage());
       }
   }
   public boolean login(String username, String password) {
       for (User user : users) {
           if (user.getId().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
               loggedInUser = user;
               return true;
           }
       }
       return false;
   }
   public void loadBooks() {
       books.clear();
       try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))) {
           String line;
           while ((line = reader.readLine()) != null) {
               String[] parts = line.split(",");
               if (parts.length == 4) {
                   String bookId = parts[0];
                   String title = parts[1];
                   String author = parts[2];
                   boolean available = Boolean.parseBoolean(parts[3]);
                   books.add(new Book(bookId, title, author, available));
               }
           }
       } catch (FileNotFoundException e) {
           System.out.println("books.txt not found. Will create on save.");
       } catch (IOException e) {
           System.out.println("Error reading books.txt: " + e.getMessage());
       }


}


   public void loadTransactions() {
       transactions.clear();
       try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
           String line;
           while ((line = reader.readLine()) != null) {
               String[] parts = line.split(",");
               if (parts.length == 5) {
                   String transactionId = parts[0];
                   String userId = parts[1];
                   String bookId = parts[2];
                   String dateBorrowed = parts[3];
                   String dateReturned = parts[4];
                   transactions.add(new Transaction(transactionId, userId, bookId, dateBorrowed, dateReturned));
               }
           }
       } catch (FileNotFoundException e) {
           System.out.println("transactions.txt not found. Will create on save.");
       } catch (IOException e) {
           System.out.println("Error reading transactions.txt: " + e.getMessage());
       }
   }




   public void viewBooks() {
       System.out.println("======BOOK CATALOGUE======");
       System.out.println(" ");


       for (Book b : books) {
           b.displayBookDetails();
       }
   }


   public void borrowBook(Scanner sc) {
       System.out.print("Enter Book ID to borrow: ");
       String bookId = sc.nextLine();


       for (Book b : books) {
           if (b.getBookId().equals(bookId)) {
               if (b.isAvailable()) {
                   b.setAvailable(false);
                   String transId = "T" + String.format("%03d", transactions.size() + 1);
                   String today = LocalDate.now().toString();
                   transactions.add(new Transaction(transId, loggedInUser.getId(), bookId, today, "null"));
                   System.out.println("Book borrowed successfully.");
                   return;
               } else {
                   System.out.println("Book is not available.");
                   return;
               }
           }
       }
       System.out.println("Book ID not found.");
   }


   public void returnBook(Scanner sc) {
       System.out.print("Enter Book ID to return: ");
       String bookId = sc.nextLine();


       for (Transaction t : transactions) {
           if (t.getUserId().equals(loggedInUser.getId())
                   && t.getBookId().equals(bookId)
                   && t.getDateReturned().equals("null")) {


               t.setDateReturned(LocalDate.now().toString());


               for (Book b : books) {
                   if (b.getBookId().equals(bookId)) {
                       b.setAvailable(true);
                       System.out.println("Book returned successfully.");
                       return;
                   }
               }
           }
       }
       System.out.println("No record found for this borrowed book.");
   }


   public void saveBooks() {
       try (BufferedWriter bw = new BufferedWriter(new FileWriter("books.txt"))) {
           for (Book b : books) {
               bw.write(b.getBookId() + "," + b.getTitle() + "," + b.getAuthor() + "," + b.isAvailable());
               bw.newLine();
           }
       } catch (IOException e) {
           System.out.println("Error saving books.");
       }
   }


   public void saveTransactions() {
       try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt"))) {
           for (Transaction t : transactions) {
               writer.write(t.getTransactionId() + "," + t.getUserId() + "," + t.getBookId() + "," + t.getDateBorrowed() + "," + t.getDateReturned());
               writer.newLine();
           }
       } catch (IOException e) {
           System.out.println("Error writing to transactions.txt: " + e.getMessage());
       }
   }




   public void saveUsers() {
       try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt"))) {
           for (User u : users) {
               bw.write(u.getId() + "," + u.name + "," + u.getPassword() + "," + u.getRole());
               bw.newLine();
           }
       } catch (IOException e) {
           System.out.println("Error saving users.");
       }
   }


   public void manageUsers(Scanner sc) {
       int choice;
       do {
           System.out.println("======MANAGE USERS======");
           System.out.println("1. Add User");
           System.out.println("2. Update User");
           System.out.println("3. Delete User");
           System.out.println("4. Display Users");
           System.out.println("5. Return to Main Menu");
           System.out.print("Enter choice: ");


           try {
               choice = Integer.parseInt(sc.nextLine());
               switch (choice) {
                   case 1:
                       addUser(sc);
                       break;
                   case 2:
                       updateUser(sc);
                       break;
                   case 3:
                       deleteUser(sc);
                       break;
                   case 4:
                       displayUsers();
                       break;
                   case 5:
                       System.out.println("Returning to main menu...");
                       break;
                   default:
                       System.out.println("Invalid choice.");
               }
           } catch (NumberFormatException e) {
               System.out.println("Invalid input.");
               choice = 0;
           }
       } while (choice != 5);
   }


   public void addUser(Scanner sc) {
       System.out.print("Enter User ID: ");
       String id = sc.nextLine().trim();


       System.out.print("Enter Name: ");
       String name = sc.nextLine().trim();


       System.out.print("Enter Password: ");
       String password = sc.nextLine().trim();


       System.out.print("Enter Role (admin/user): ");
       String role = sc.nextLine().trim();


       // Check for duplicate IDs etc, then add user
       users.add(new User(id, name, password, role));
       saveUsers();  // save to file
       System.out.println("User added successfully.");
   }


   public void updateUser(Scanner sc) {
       System.out.print("Enter User ID to update: ");
       String id = sc.nextLine().trim();


       User user = findUserById(id);
       if (user == null) {
           System.out.println("User not found.");
           return;
       }


       System.out.print("Enter new name (or press Enter to keep '" + user.name + "'): ");
       String name = sc.nextLine().trim();
       if (!name.isEmpty()) user.name = name;


       System.out.print("Enter new password (or press Enter to keep current): ");
       String password = sc.nextLine().trim();
       if (!password.isEmpty()) {
           try {
               java.lang.reflect.Field passField = User.class.getDeclaredField("password");
               passField.setAccessible(true);
               passField.set(user, password);
           } catch (Exception e) {
               System.out.println("Error updating password.");
           }
       }


       System.out.print("Enter new role (user/admin, or press Enter to keep '" + user.getRole() + "'): ");
       String role = sc.nextLine().trim().toLowerCase();
       if (!role.isEmpty()) {
           if (role.equals("user") || role.equals("admin")) {
               try {
                   java.lang.reflect.Field roleField = User.class.getDeclaredField("role");
                   roleField.setAccessible(true);
                   roleField.set(user, role);
               } catch (Exception e) {
                   System.out.println("Error updating role.");
               }
           } else {
               System.out.println("Invalid role. Must be 'user' or 'admin'.");
           }
       }


       saveUsers();
       System.out.println("User updated.");
   }


   public void deleteUser(Scanner sc) {
       System.out.print("Enter User ID to delete: ");
       String id = sc.nextLine().trim();


       User user = findUserById(id);
       if (user == null) {
           System.out.println("User not found.");
           return;
       }


       if (user.getId().equals(loggedInUser.getId())) {
           System.out.println("You can't delete the currently logged in user.");
           return;
       }


       users.remove(user);
       saveUsers();
       System.out.println("User deleted.");
   }
   public void displayUsers() {
       System.out.println("\n--- User List ---");
       for (User u : users) {
           u.displayInfo();
       }
   }


   public void manageBooks(Scanner sc) {
       int choice;
       do {
           System.out.println("======MANAGE BOOKS======");
           System.out.println("1. Add Book");
           System.out.println("2. Update Book");
           System.out.println("3. Delete Book");
           System.out.println("4. Display Books");
           System.out.println("5. Return to Main Menu");
           System.out.print("Enter choice: ");


           try {
               choice = Integer.parseInt(sc.nextLine());
               switch (choice) {
                   case 1:
                       addBook(sc);
                       break;
                   case 2:
                       updateBook(sc);
                       break;
                   case 3:
                       deleteBook(sc);
                       break;
                   case 4:
                       displayBooks();
                       break;
                   case 5:
                       System.out.println("Returning to main menu...");
                       break;
                   default:
                       System.out.println("Invalid choice.");
               }
           } catch (NumberFormatException e) {
               System.out.println("Invalid input.");
               choice = 0;
           }
       } while (choice != 5);
   }


   public void addBook(Scanner sc) {
           System.out.print("Enter new Book ID (e.g. B005): ");
           String id = sc.nextLine().trim();


           if (findBookById(id) != null) {
               System.out.println("Book ID already exists.");
               return;
           }


           System.out.print("Enter title: ");
           String title = sc.nextLine().trim();


           System.out.print("Enter author: ");
           String author = sc.nextLine().trim();


           books.add(new Book(id, title, author, true));
           saveBooks(); // Save after add
           System.out.println("Book added successfully.");
       }




   public void updateBook(Scanner sc) {
           System.out.print("Enter Book ID to update: ");
           String id = sc.nextLine().trim();


           Book oldBook = findBookById(id);
           if (oldBook == null) {
               System.out.println("Book not found.");
               return;
           }


           System.out.print("Enter new title (or press Enter to keep '" + oldBook.getTitle() + "'): ");
           String title = sc.nextLine().trim();
           if (title.isEmpty()) title = oldBook.getTitle();


           System.out.print("Enter new author (or press Enter to keep '" + oldBook.getAuthor() + "'): ");
           String author = sc.nextLine().trim();
           if (author.isEmpty()) author = oldBook.getAuthor();


           System.out.print("Is book available? (true/false, or press Enter to keep '" + oldBook.isAvailable() + "'): ");
           String availInput = sc.nextLine().trim();
           boolean available = oldBook.isAvailable();
           if (!availInput.isEmpty()) {
               if (availInput.equalsIgnoreCase("true") || availInput.equalsIgnoreCase("false")) {
                   available = Boolean.parseBoolean(availInput);
               } else {
                   System.out.println("Invalid availability value; keeping previous.");
               }
           }


           books.remove(oldBook);
           books.add(new Book(id, title, author, available));
           saveBooks(); // Save after update
           System.out.println("Book updated.");
       }






       public void deleteBook(Scanner sc) {
               System.out.print("Enter Book ID to delete: ");
               String id = sc.nextLine().trim();


               Book book = findBookById(id);
               if (book == null) {
                   System.out.println("Book not found.");
                   return;
               }


               books.remove(book);
               saveBooks(); // Save after deletion
               System.out.println("Book deleted.");
           }


   public void displayBooks() {
       System.out.println("\n--- Book List ---");
       for (Book b : books) {
           b.displayBookDetails();
       }
   }


       public Book findBookById(String id) {
       for (Book b : books) {
           if (b.getBookId().equalsIgnoreCase(id)) {
               return b;
           }
       }
       return null;
   }


   public void displayAllTransactions() {
       System.out.println("\n--- All Transactions ---");
       for (Transaction t : transactions) {
           t.displayTransaction();
       }
   }




   public void displayMenu() {
       Scanner sc = new Scanner(System.in);
       int choice;


       do {
           System.out.println("======LIBRARY MENU=======");
           if (loggedInUser.getRole().equalsIgnoreCase("admin")) {
               System.out.println("1. View All Books");
               System.out.println("2. Borrow Book");
               System.out.println("3. Return Book");
               System.out.println("4. Manage Users (Add/Update/Delete/Display)");
               System.out.println("5. Manage Books (Add/Update/Delete/Display)");
               System.out.println("6. View Transactions");
               System.out.println("7. Exit");
               System.out.print("Enter choice: ");


               try {
                   choice = Integer.parseInt(sc.nextLine());


                   switch (choice) {
                       case 1:
                           viewBooks();
                           break;
                       case 2:
                           borrowBook(sc);
                           break;
                       case 3:
                           returnBook(sc);
                           break;
                       case 4:
                           manageUsers(sc);
                           break;
                       case 5:
                           manageBooks(sc);
                           break;
                       case 6:
                           displayAllTransactions();
                           break;
                       case 7:
                           saveBooks();
                           saveTransactions();
                           saveUsers();
                           System.out.println("Goodbye!");
                           break;
                       default:
                           System.out.println("Invalid choice.");
                   }


               } catch (NumberFormatException e) {
                   System.out.println("Invalid input.");
                   choice = 0;
               }


           } else {  // normal user menu
               System.out.println("1. View All Books");
               System.out.println("2. Borrow Book");
               System.out.println("3. Return Book");
               System.out.println("4. Exit");
               System.out.print("Enter choice: ");


               try {
                   choice = Integer.parseInt(sc.nextLine());


                   switch (choice) {
                       case 1:
                           viewBooks();
                           break;
                       case 2:
                           borrowBook(sc);
                           break;
                       case 3:
                           returnBook(sc);
                           break;
                       case 4:
                           saveBooks();
                           saveTransactions();
                           System.out.println("Goodbye!");
                           break;
                       default:
                           System.out.println("Invalid choice.");
                   }


               } catch (NumberFormatException e) {
                   System.out.println("Invalid input.");
                   choice = 0;
               }
           }
       } while ((loggedInUser.getRole().equalsIgnoreCase("admin") && choice != 7) ||
               (!loggedInUser.getRole().equalsIgnoreCase("admin") && choice != 4));
   }


   public User findUserById(String id) {
       for (User u : users) {
           if (u.getId().equalsIgnoreCase(id)) {
               return u;
           }
       }
       return null;
   }
}
