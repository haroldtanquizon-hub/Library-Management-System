import java.util.Scanner;


public class Main {
   public static void main(String[] args) {
       LibrarySystem librarySystem = new LibrarySystem();
       librarySystem.loadUsers();
       librarySystem.loadBooks();
       librarySystem.loadTransactions();


       Scanner scanner = new Scanner(System.in);
       int attempts = 0;
       boolean loggedIn = false;


       while (attempts < 3 && !loggedIn) {
           System.out.println("=== Library Login ===");
           System.out.print("Username (UserID): ");
           String username = scanner.nextLine();
           System.out.print("Password: ");
           String password = scanner.nextLine();


           try {
               if (librarySystem.login(username, password)) {
                   System.out.println("Login successful!\nWelcome, " + librarySystem.loggedInUser.name + "!");
                   loggedIn = true;
                   librarySystem.displayMenu();
               } else {
                   attempts++;
                   System.out.println("Invalid credentials. Attempt " + attempts + " of 3.");
               }
           } catch (Exception e) {
               System.out.println("Unexpected error: " + e.getMessage());
           }
       }


       if (!loggedIn) {
           System.out.println("Too many failed attempts. Exiting...");
       }


       scanner.close();
   }
}
