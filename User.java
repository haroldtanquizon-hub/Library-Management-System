import java.util.ArrayList;


public class User extends Person {
   private String password;
   private String role;
   private ArrayList<String> borrowedBooks;


   public User(String id, String name, String password, String role) {
       super(id, name);
       this.password = password;
       this.role = role;
       this.borrowedBooks = new ArrayList<>();
   }


   public String getPassword() {
       return password;
   }


   public String getRole() {
       return role;
   }


   public String getId() {
       return id;
   }


   @Override
   public void displayInfo() {
       System.out.println("User ID: " + id + ", Name: " + name + ", Role: " + role);
   }


   public ArrayList<String> getBorrowedBooks() {
       return borrowedBooks;
   }


   public void addBorrowedBook(String bookId) {
       borrowedBooks.add(bookId);
   }


   public void removeBorrowedBook(String bookId) {
       borrowedBooks.remove(bookId);
   }
}
