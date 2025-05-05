package lv.rvt.mainFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import lv.rvt.bookManaging.Book;
import lv.rvt.bookManaging.BookManager;
import lv.rvt.bookManaging.UserBook;
import lv.rvt.colors.ConsoleColors;
import lv.rvt.user.User;

public class Structure extends Bookshop { // structure for the program

    public static void start() throws Throwable{  // initial terminal
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> books = BookManager.allBooks();

        boolean INTRO = true;
        while (INTRO) {

            clearScreen();
            boolean SYSTEM = Bookshop.entry();

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
                clearScreen();
                titlePage();
                System.out.println("                                   Are you sure? y/n");
                System.out.print("                                   ");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("y")) {
                    INTRO = false;
                    break;
                } else if (choice.equalsIgnoreCase("n")) {
                    break;
                } else {
                    System.out.println("Input must be [" + ConsoleColors.GREEN_BRIGHT  +"y" + ConsoleColors.RESET + "] / [" 
                    + ConsoleColors.RED_BRIGHT + "n" + ConsoleColors.RESET + "].");
                }
            }
        }
    }

    public static void show(ArrayList<Book> showBooks) throws Throwable{
        Scanner scan = new Scanner(System.in);
        String message = "";

        while (true) {
            clearScreen();
            titlePage();
            if (!message.equals("")) {
                System.out.println("                                  " + message);
            }
            System.out.println("        Show all books [a]   /   show your reading list [l]   /   exit [x].");
            
            message = "";
            System.out.print("        ");
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("x")) {
                break;
            } else if (input.equalsIgnoreCase("a")) {
                choiceAllBooks();
            } else if (input.equalsIgnoreCase("l")) {
                choiceUserList();
            } else {
                message = ConsoleColors.RED_BRIGHT + "Invalid input." + ConsoleColors.RESET;
            }
        }
    }

    public static void choiceAllBooks() throws Throwable {
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> books;
        String message = "";
        books = BookManager.allBooks();

        while (true) {
            clearScreen();
            titlePage();

            books = BookManager.allBooks();
            books = applyLastSort(books);
            books = applyLastGenreFilter(books);
            tableFormatAll(books, "");
            if (!message.equals("")) {
                System.out.println(message);
            }
            
            message = "";
            System.out.println("   Sort [s]      /     search [e]      /     filter [f]      /     add [a]      /     exit [x]");

            String input = scan.nextLine();

            if (input.equals("x")) {
                break;
            } else if (input.equalsIgnoreCase("s")) {
                sortAllBooks(books, "");
            } else if (input.equalsIgnoreCase("e")) {
                message = search(books);
            } else if (input.equalsIgnoreCase("f")) {
                filterAllBooks(books);
            } else if (input.equalsIgnoreCase("a")) {
                if (books.size() == 0) {
                    message = ConsoleColors.RED_BRIGHT + "No books available to add." + ConsoleColors.RESET;
                } else {
                    message = Structure.addBook(books, "");
                }
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                message = ConsoleColors.RED_BRIGHT + "Invalid input." + ConsoleColors.RESET;
            }      
        }
    }

    public static void choiceUserList() throws Throwable { // ADDD so that sorting and filters stay
        Scanner scan = new Scanner(System.in);

        String message = "";
        ArrayList<UserBook> books = BookManager.allUserBooks();
        books = BookshopUserList.filterForUserBooks(books, true);
        if (books.size() == 0) {
            message = ConsoleColors.BLUE_BRIGHT + "Empty reading list." + ConsoleColors.RESET;
        }
        while (true) {
            clearScreen();
            titlePage();
        
            // applies all the previous sorts and filters
            books = BookManager.allUserBooks();
            books = BookshopUserList.applyLastSortForUser(books);
            books = BookshopUserList.applyLastFilterForUser(books);
            BookshopUserList.tableFormatUser(books, "");
            if (!message.equals("")) {
                System.out.println(message);
            }

            System.out.println("Sort [s] /  search [e]   /  filter [f]   /  remove [r]   /  reset [t]   /  change status [c]   / exit [x]");
            message = "";
            String input = scan.nextLine();
            if (input.equals("x")) {
                break;
            } else if (input.equalsIgnoreCase("s")) {
                BookshopUserList.sortAllUserBooks(books, "");
            } else if (input.equalsIgnoreCase("e")) {
                message = BookshopUserList.searchUserBooks(books);
            } else if (input.equalsIgnoreCase("f")) {
                BookshopUserList.filterForUserBooks(books, false);
            } else if (input.equalsIgnoreCase("t")) {
                BookshopUserList.filterForUserBooks(books, true);
            } else if (input.equalsIgnoreCase("r")) {
                if (books.size() == 0) {
                    message = ConsoleColors.RED_BRIGHT + "Can't remove from an empty list." + ConsoleColors.RESET;
                } else {
                    Structure.removeBook("data/users/" + User.getCurrentUser() + ".csv", books, "");
                    message = ConsoleColors.GREEN_BRIGHT + "Book removed successfully." + ConsoleColors.RESET;
                }
            } else if (input.equalsIgnoreCase("c")) {
                if (books.size() == 0) {
                    message =  ConsoleColors.RED_BRIGHT + "Can't edit an empty list." + ConsoleColors.RESET;
                } else {
                 Structure.changeBookReadingStatus("data/users/" + User.getCurrentUser() + ".csv", books);
                 message = ConsoleColors.GREEN_BRIGHT + "Book status changed successfully." + ConsoleColors.RESET;
                }
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                message = ConsoleColors.RED_BRIGHT + "Invalid input." + ConsoleColors.RESET;
            }   
            
        }
    }

    public static String addBook(ArrayList<Book> givenBooks, String start) throws Exception {
        Scanner scan = new Scanner(System.in);
        ArrayList<UserBook> userBooks = BookManager.allUserBooks();

        String bookId = null;

        //Ask for a valid book ID until found in givenBooks
        while (true) {
            System.out.print("Book ID: ");
            String input = scan.nextLine();

            if (!input.matches("\\d+")) {
                System.out.println( ConsoleColors.RED_BRIGHT +"Input has to be a positive integer." + ConsoleColors.RESET);
                continue;
            }

            boolean MATCH = false;
            for (Book book : givenBooks) {
                if (book.getId() == Integer.parseInt(input)) {
                    bookId = input;
                    MATCH = true;
                    break;
                }
            }
            if (MATCH){
                break;
            } 
            System.out.println(ConsoleColors.RED_BRIGHT + "Input has to be one of the IDs." + ConsoleColors.RESET);
        }

        Book selectedBook = null;
        for (Book book : givenBooks) {
            if (book.getId() == Integer.valueOf(bookId)) {
                selectedBook = book;
                break;
            }
        }

        
        //Check if book already exists in user's list
        for (Book userBook : userBooks) {
            if (userBook.getName().equals(selectedBook.getName()) && userBook.getAuthor().equals(selectedBook.getAuthor())) {
                return ConsoleColors.RED_BRIGHT + "List already includes this book." + ConsoleColors.RESET;
            }
        }

        // Assign next available ID and add to user's reading list
        selectedBook.setId(userBooks.size() + 1);
        UserBook userSelectedBook = new UserBook(selectedBook.getId(), selectedBook.getName(), selectedBook.getAuthor(), +
        selectedBook.getYear(), selectedBook.getGenre(), selectedBook.getPrice(), false);
        Bookshop.addBookToUserReadingList(userSelectedBook);
        selectedBook.setId(Integer.valueOf(bookId));
        return ConsoleColors.GREEN_BRIGHT + "Book added to reading list." + ConsoleColors.RESET;
        
    }

    public static ArrayList<UserBook> removeBook(String filePath, ArrayList<UserBook> givenBooks, String start) throws Exception {
        String tempFile = "data/users/" + User.getCurrentUser() + "Temp.csv";
        File oldReadingList = new File(filePath);
        File newReadingList = new File(tempFile);
        
        Scanner scan = new Scanner(System.in);
        ArrayList<UserBook> modifiedBooks = new ArrayList<>();

        int deleteLine = -1;

        while (true) {
            System.out.print("Enter ID to remove: ");
            String input = scan.nextLine();
            try {
                boolean NOMATCH = true;
                int id = Integer.parseInt(input);
                for (UserBook book : givenBooks) {
                    if (id == book.getId()) {
                        deleteLine = id;
                        NOMATCH = false;
                        break;
                    }
                    
                } if (NOMATCH) {
                    System.out.println(ConsoleColors.RED_BRIGHT + "ID out of range." + ConsoleColors.RESET);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.RED_BRIGHT + "Input must be a number." + ConsoleColors.RESET);
            }
        }
    
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            String header = reader.readLine();
            writer.println(header);
    
            int newId = 1;
            ArrayList<UserBook> allBooks = BookManager.allUserBooks();
            for (Book book : allBooks) {
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

            // from the given list removes the removed book 
            if (start.equalsIgnoreCase("search")) {
                for (UserBook book : givenBooks) {
                    if (book.getId() != deleteLine) {
                        modifiedBooks.add(book);
                    }
                }
                return modifiedBooks;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        // returns the given list but without the removed book
        return modifiedBooks;
    }

    public static ArrayList<UserBook> changeBookReadingStatus(String filePath, ArrayList<UserBook> givenBooks) {
        String tempFile = "data/users/" + User.getCurrentUser() + "Temp.csv";
        File oldReadingList = new File(filePath);
        File newReadingList = new File(tempFile);
        

        Scanner scan = new Scanner(System.in);
        ArrayList<UserBook> modifiedBooks = new ArrayList<>();

        int modifyStatusID = -1;
        boolean modifiedBookOriginalStatus = false;
        while (true) {
            System.out.print("Enter ID to change status: ");
            String input = scan.nextLine();
            try {
                boolean NOMATCH = true;
                int id = Integer.parseInt(input);
                for (UserBook book : givenBooks) {
                    if (id == book.getId()) {
                        modifiedBookOriginalStatus = book.getReadingStatus();
                        modifyStatusID = id;
                        NOMATCH = false;
                        break;
                    }
                    
                } if (NOMATCH) {
                    System.out.println(ConsoleColors.RED_BRIGHT + "ID out of range." + ConsoleColors.RESET);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println(ConsoleColors.RED_BRIGHT + "Input must be a number." + ConsoleColors.RESET);
            }
        } 
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            PrintWriter writer = new PrintWriter(new FileWriter(tempFile));

            String header = reader.readLine();
            writer.println(header);
    
            ArrayList<UserBook> allBooks = BookManager.allUserBooks();
            for (UserBook book : allBooks) {
                if (book.getId() == modifyStatusID) {
                    // change status for the chosen book
                    boolean modiedStatus = !modifiedBookOriginalStatus;
                    book.setReadingStatus(modiedStatus);
                }
                writer.println(book.toCSV());
            }
        
            writer.flush();
            writer.close();
            reader.close();
            
            oldReadingList.delete();
            newReadingList.renameTo(oldReadingList);

            for (UserBook book : givenBooks) {
                if (book.getId() == modifyStatusID) {
                    book.setReadingStatus(!modifiedBookOriginalStatus);
                }
                modifiedBooks.add(book);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return modifiedBooks;
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}