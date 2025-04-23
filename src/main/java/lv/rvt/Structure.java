package lv.rvt;

import java.util.ArrayList;
import java.util.Scanner;

public class Structure { // structure for the program
    private static Bookshop user;

    public static void start() throws Throwable{  // initial terminal
        Scanner scan = new Scanner(System.in);

        boolean INTRO = true;

        // ArrayList<Book> bookie = Bookshop.allBooks();
        while (INTRO) {

            boolean SYSTEM = Bookshop.entry();
            while (SYSTEM) {
                Structure.instructions();

                System.out.println(user);

                String input = scan.nextLine();
                if (input.equalsIgnoreCase("stop")) {
                    break;
                }
    
                else if (input.equalsIgnoreCase("show")) {
                    ArrayList<Book> books = Bookshop.allBooks();
    
                    for (Book book : books) {
                        System.out.println(book);
                    }
                    System.out.println();
                }
    
                else if (input.equalsIgnoreCase("help")) {
                    System.out.println();
                    Structure.instructions();
                    System.out.println();
                }
    
                else if (input.equalsIgnoreCase("search")) {
                    Bookshop.search();
                }
    
                else if (input.equalsIgnoreCase("sort")) {
                    ArrayList<Book> sortedBooks =  Bookshop.sortAllBooks();
    
                    for (Book book : sortedBooks) {
                        System.out.println(book);
                    }
                    System.out.println();
                }
    
                else if (input.equalsIgnoreCase("add")) {
                    System.out.println("Enter the name of the book: ");
                    String name = scan.nextLine();
    
                    System.out.println("Enter the author of the book: ");
                    String author = scan.nextLine();
    
                    System.out.println("Enter the year of publication: ");
                    int year = Integer.valueOf(scan.nextLine());
    
                    System.out.println("Enter the genre of the book: ");
                    String genre = scan.nextLine();
    
                    System.out.println("Enter the price of the book: ");
                    double price = Double.valueOf(scan.nextLine());
    
                    Book book = new Book(name, author, year, genre, price);
                    Bookshop.addBook(book);
                    System.out.println("Book got added");
                }
            } 


            // if in entry part user exits without once logging in or regestering, 
            // then the extra check to ask if the user wants to exit is true
            boolean SYSTEMCHECK = false; 
            if ((!SYSTEM) ) {   
                SYSTEMCHECK = true;
            } else if (SYSTEM == true && SYSTEMCHECK == true) { // if user didn't exit immediately & if the extra check neccessity was , goes back to user entry part
                boolean CHECK = Bookshop.entry();
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
                    System.out.println("Input must be y/n.");
                }
            }
        }
        
    }

    public static void instructions(){ // instruction output
        System.out.println("add - add a book to the list");
        System.out.println("help - show what the commands do");
        System.out.println("search - search for a book or author");
        System.out.println("filter - filter books and sort with differing criteria");
        System.out.println("sort - decide how books are sorted");
        System.out.println("show - show list of books");
        System.out.println("stop - end program");
    }

}