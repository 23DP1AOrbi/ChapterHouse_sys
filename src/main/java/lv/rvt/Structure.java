package lv.rvt;

import java.util.ArrayList;
import java.util.Scanner;

import lv.rvt.roles.User;

public class Structure extends Bookshop { // structure for the program

    public static void start() throws Throwable{  // initial terminal
        Scanner scan = new Scanner(System.in);

        boolean INTRO = true;
        while (INTRO) {

            boolean SYSTEM = Bookshop.entry();
            String currentUser = User.getCurrentUser();

            System.out.println(User.getCurrentUser());
            while (SYSTEM) {
                Structure.instructions();

                String input = scan.nextLine();
                if (input.equalsIgnoreCase("stop")) {
                    break;
                }
    
                else if (input.equalsIgnoreCase("show")) {
                    show();
                }
    
                else if (input.equalsIgnoreCase("help")) {
                    System.out.println();
                    Structure.instructions();
                    System.out.println();
                }
    
                // else if (input.equalsIgnoreCase("add")) {
                //     System.out.println("Enter the name of the book: ");
                //     String name = scan.nextLine();
    
                //     System.out.println("Enter the author of the book: ");
                //     String author = scan.nextLine();
    
                //     System.out.println("Enter the year of publication: ");
                //     int year = Integer.valueOf(scan.nextLine());
    
                //     System.out.println("Enter the genre of the book: ");
                //     String genre = scan.nextLine();
    
                //     System.out.println("Enter the price of the book: ");
                //     double price = Double.valueOf(scan.nextLine());
    
                //     Book book = new Book(name, author, year, genre, price);
                //     Bookshop.addBook(book);
                //     System.out.println("Book got added");
                // }
            } 


            // if in entry part user exits without once logging in or regestering, 
            // then the extra check to ask if the user wants to exit is true
            boolean SYSTEMCHECK = false; 
            if ((!SYSTEM) ) {   
                SYSTEMCHECK = true;
            } else if (SYSTEM == true && SYSTEMCHECK == true) { // if user didn't exit immediately & if the extra check neccessity was , goes back to user entry part
                boolean CHECK = entry();
                SYSTEMCHECK = CHECK;
            }
            
            while (SYSTEMCHECK) { // extra check to ask again if user really wants to exit
                System.out.println("Are you sure? y/n");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("y")) {
                    INTRO = false;
                    break;
                } else if (choice.equalsIgnoreCase("n")) {
                    break;
                } else {
                    System.out.println("Input must be [y] / [n].");
                }
            }
        }
    }

    public static void instructions(){ // instruction output
        System.out.println("help - show what the commands do");
        System.out.println("show - show list of books");
        System.out.println("stop - end program");
    }


    public static void show() throws Throwable{
        Scanner scan = new Scanner(System.in);

        System.out.println("Show all books [a] / show your reading list [l] / exit [x].");

        while (true) {
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("a")) {
                ArrayList<Book> books = BookManager.allBooks();
                for (Book book : books) {
                    System.out.println(book);
                }
                System.out.println();
                choiceAllBooks();
                break;
            } else if (input.equalsIgnoreCase("l")) {
                ArrayList<Book> books = BookManager.allUserBooks();
                for (Book book : books) {
                    System.out.println(book);
                }
                System.out.println();
                choiceUserList();
                break;
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    public static void choiceAllBooks() throws Throwable {
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("All books");
            System.out.println("Sort [s] / search [e] / filter [f] / add [a] / exit [x]");

            String input = scan.nextLine();
            if (input.equals("x")) {
                break;
            } else if (input.equalsIgnoreCase("s")) {
                ArrayList<Book> sortedBooks =  Bookshop.sortAllBooks("");
                for (Book book : sortedBooks) {
                    System.out.println(book);
                }
                System.out.println();
            } else if (input.equalsIgnoreCase("e")) {
                Bookshop.search("");
            } else if (input.equalsIgnoreCase("f")) {
                Bookshop.filter("");
            } else if (input.equalsIgnoreCase("a")) {
                Structure.addBook();
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                System.out.println("Invalid input.");
            }    
        }
    }

    public static void choiceUserList() throws Throwable {
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("Reader list");
            System.out.println("Sort [s] / search [e] / filter [f] / remove [r] / exit [x]");

            String input = scan.nextLine();
            if (input.equals("x")) {
                break;
            } else if (input.equalsIgnoreCase("s")) {
                ArrayList<Book> sortedBooks =  Bookshop.sortAllBooks(User.getCurrentUser());
                for (Book book : sortedBooks) {
                    System.out.println(book);
                }
                System.out.println();
            } else if (input.equalsIgnoreCase("e")) {
                Bookshop.search(User.getCurrentUser());
            } else if (input.equalsIgnoreCase("f")) {
                Bookshop.filter(User.getCurrentUser());
            } else if (input.equalsIgnoreCase("r")) {
                Structure.removeBook();
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                System.out.println("Invalid input.");
            }    
        }
    }

    public static void addBook() throws Exception {
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> books = BookManager.allBooks();
        ArrayList<Book> userBooks = BookManager.allUserBooks();

        String bookId;
        while (true) { 
            System.out.println("Book ID: ");
            String valueCheck = scan.nextLine();
            if (valueCheck.contains(".") || valueCheck.contains(",")) { // checks if user input has [.] or [,] to check if its a double 
                System.out.println("Input has to be an integer.");
            } else if (!(valueCheck.contains(".")) && valueCheck.matches("-?\\d+(\\.\\d+)?")) { // if input doesnt have a [.] and is a valid number, 
                if (Integer.valueOf(valueCheck) >= 0 && Integer.valueOf(valueCheck) <= 30) {            // it goes through
                    bookId = valueCheck;                  
                    break;
                } else {
                    System.out.println("Input has to be 1-30.");
                }
            } else {
                System.out.println("Input has to be an integer.");
            }
        }

        boolean bookExists = false;
        for (Book userBook : userBooks) { // if user book list has the book, the book doesnt get added
            if (Integer.valueOf(bookId) == userBook.getId()) {
                bookExists = true;
                System.out.println("List already includes this book.");
                break;
            }
        }

        if (!bookExists) { // only adds book if it doesnt alredy exist in the users list
            for (Book book : books) {
                if (Integer.valueOf(bookId) == book.getId()) {
                    Bookshop.addBookToUserReadingList(book);
                    System.out.println("Book added to reading list.");
                }
            }
        }
            
    }

    public static void removeBook() {

    }

}