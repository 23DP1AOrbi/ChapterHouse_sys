package lv.rvt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import lv.rvt.roles.User;

public class Structure extends Bookshop { // structure for the program

    public static void start() throws Throwable{  // initial terminal
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> books = BookManager.allBooks();

        boolean INTRO = true;
        while (INTRO) {

            boolean SYSTEM = Bookshop.entry();
            String currentUser = User.getCurrentUser();

            System.out.println(User.getCurrentUser());
            while (SYSTEM) {

                show(books);
                break;
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


    public static void show(ArrayList<Book> showBooks) throws Throwable{
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("Show all books [a] / show your reading list [l] / exit [x].");
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("x")) {
                break;
            } else if (input.equalsIgnoreCase("a")) {
                ArrayList<Book> books = BookManager.allBooks();
                for (Book book : books) {
                    System.out.println(book);
                }
                System.out.println();
                choiceAllBooks();
                // break;
            } else if (input.equalsIgnoreCase("l")) {
                ArrayList<Book> books = BookManager.allUserBooks();
                if (books.size() == 0) {
                    System.out.println("Empty reading list.");
                } else {
                    for (Book book : books) {
                        System.out.println(book);
                    }
                    System.out.println();
                    choiceUserList();
                    // break;
                }
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    public static void choiceAllBooks() throws Throwable {
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> books = BookManager.allBooks();

        while (true) {
            System.out.println("All books");
            System.out.println("Sort [s] / search [e] / filter [f] / add [a] / exit [x]");

            String input = scan.nextLine();
            if (input.equals("x")) {
                break;
            } else if (input.equalsIgnoreCase("s")) {
                books = Bookshop.sortAllBooks("", books);
                for (Book sortedBook : books) {
                    System.out.println(sortedBook);
                }
            } else if (input.equalsIgnoreCase("e")) {
                Bookshop.search("", books);
            } else if (input.equalsIgnoreCase("f")) {
                books = Bookshop.filter("", books);
            } else if (input.equalsIgnoreCase("a")) {
                Structure.addBook(books);
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                System.out.println("Invalid input.");
            }    
        }
    }

    public static void choiceUserList() throws Throwable {
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> books = BookManager.allUserBooks();

        while (true) {
            System.out.println("Reader list");
            System.out.println("Sort [s] / search [e] / filter [f] / remove [r] / exit [x]");

            String input = scan.nextLine();
            if (input.equals("x")) {
                break;
            } else if (input.equalsIgnoreCase("s")) {
                books = Bookshop.sortAllBooks(User.getCurrentUser(), books);
                for (Book sortedBook : books) {
                    System.out.println(sortedBook);
                }
            } else if (input.equalsIgnoreCase("e")) {
                Bookshop.search(User.getCurrentUser(), books);
            } else if (input.equalsIgnoreCase("f")) {
                books = Bookshop.filter(User.getCurrentUser(), books);
            } else if (input.equalsIgnoreCase("r")) {
                Structure.removeBook("/workspaces/Eksamens_praktiskais/data/" + User.getCurrentUser() + ".csv", books);
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                System.out.println("Invalid input.");
            }    
        }
    }

    public static void addBook(ArrayList<Book> givenBooks) throws Exception {
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> userBooks = BookManager.allUserBooks();

        String bookId = null;
        boolean NOMATCH = true;
        while (NOMATCH) { 
            System.out.println("Book ID: ");
            String valueCheck = scan.nextLine();
            if (valueCheck.contains(".") || valueCheck.contains(",")) { // checks if user input has [.] or [,] to check if its a double 
                System.out.println("Input has to be an integer.");
            } else if (!(valueCheck.contains(".")) && valueCheck.matches("-?\\d+(\\.\\d+)?")) { // if input doesnt have a [.] and is a valid number, 
                    for (Book book : givenBooks) {
                        if (Integer.valueOf(valueCheck) == book.getId()) {
                            bookId = String.valueOf(book.getId());
                            NOMATCH = false;
                            break;
                        }
                    }
                    if (NOMATCH) {
                        System.out.println("Input has to be one of the Id.");
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
            for (Book book : givenBooks) {
                if (Integer.valueOf(bookId) == book.getId()) {
                    book.setId(userBooks.size() + 1);
                    Bookshop.addBookToUserReadingList(book);
                    System.out.println("Book added to reading list.");
                }
            }
        }
            
    }

    public static void removeBook(String filePath, ArrayList<Book> givenBooks) {
        String tempFile = "/workspaces/Eksamens_praktiskais/data/" + User.getCurrentUser() + "Temp.csv";
        File oldReadingList = new File(filePath);
        File newReadingList = new File(tempFile);
        Scanner scan = new Scanner(System.in);

        int deleteLine = -1;

        while (true) {
            System.out.print("Enter ID to remove: ");
            String input = scan.nextLine();
            try {
                int id = Integer.parseInt(input);
                if (id > 0 && id <= givenBooks.size()) {
                    deleteLine = id;
                    break;
                } else {
                    System.out.println("ID out of range.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input must be a number.");
            }
        }
    
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            String header = reader.readLine();
            writer.println(header);
    
            int newId = 1;
            for (Book book : givenBooks) {
                if (book.getId() == deleteLine) {
                    // skip the book to delete
                    continue; 
                }
                book.setId(newId); 
                writer.println(book.toCSV());
                newId++;
            }
        
            writer.flush();
            writer.close();
            reader.close();

            oldReadingList.delete();
            newReadingList.renameTo(oldReadingList);
            System.out.println("Book removed successfully.");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}