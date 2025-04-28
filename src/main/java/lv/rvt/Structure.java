package lv.rvt;

import static lv.rvt.Bookshop.genreFilters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Scanner;

import lv.rvt.bookManaging.Book;
import lv.rvt.bookManaging.BookManager;
import lv.rvt.bookManaging.UserBook;
import lv.rvt.user.User;

public class Structure extends Bookshop { // structure for the program



    public static void start() throws Throwable{  // initial terminal
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> books = BookManager.allBooks();

        boolean INTRO = true;
        while (INTRO) {

            boolean SYSTEM = Bookshop.entry();
            String currentUser = User.getCurrentUser();

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

    public static void show(ArrayList<Book> showBooks) throws Throwable{
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("Show all books [a] / show your reading list [l] / exit [x].");
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("x")) {
                break;
            } else if (input.equalsIgnoreCase("a")) {
                System.out.println();
                choiceAllBooks();
            } else if (input.equalsIgnoreCase("l")) {
                ArrayList<UserBook> books = BookManager.allUserBooks();
                if (books.size() == 0) {
                    System.out.println("Empty reading list.");
                } else {
                    System.out.println();
                    choiceUserList();
                }
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    public static void choiceAllBooks() throws Throwable {
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> books = BookManager.allBooks();
        String message = "";

        while (true) {
            clearScreen();
            books = Bookshop.applyLastSort(books);
            books = Bookshop.applyLastGenreFilter(books);
            // for (Book book : books) {
            //     System.out.println(book.toString());
            // }
            tableFormatAll(books);
            if (!message.equals("")) {
                System.out.println(message);
            }
            
            message = "";
            System.out.println("Sort [s] / search [e] / filter [f] / add [a] / exit [x]");

            String input = scan.nextLine();

            if (input.equals("x")) {
                break;
            } else if (input.equalsIgnoreCase("s")) {
                books = Bookshop.sortAllBooks(books, "");
            } else if (input.equalsIgnoreCase("e")) {
                Bookshop.search(books);
            } else if (input.equalsIgnoreCase("f")) {
                books = Bookshop.filterAllBooks(books);
            } else if (input.equalsIgnoreCase("a")) {
                message = Structure.addBook(books);
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                System.out.println("Invalid input.");
            }    
            
        }
    }

    public static void choiceUserList() throws Throwable { // ADDD so that sorting and filters stay
        Scanner scan = new Scanner(System.in);
        ArrayList<UserBook> books = BookManager.allUserBooks();
        for (UserBook userBook : books) {
            System.out.println(userBook.toString());
        }

        while (true) {
            clearScreen();
            // Double totalPrice = 0.0;
            System.out.println("Reader list");
            
            // applies all the previous sorts and filters
            books = BookshopUserList.applyLastSortForUser(books);
            books = BookshopUserList.applyLastGenreFilterForUser(books);
            // for (UserBook book : books) {
            //     totalPrice += book.getPrice();
            // } 
            tableFormatUser(books);
            
            
            // BigDecimal roundedTotal = new BigDecimal(totalPrice);
            // MathContext precision = null;
            // // changes precision according to the total sum
            // if (totalPrice < 10) {
            //     precision = new MathContext(3);
            // } else if (totalPrice < 100) {
            //     precision = new MathContext(4);
            // } else if (totalPrice > 100) {
            //     precision = new MathContext(5);
            // }
             
            // roundedTotal = roundedTotal.round(precision);
            // System.out.println(roundedTotal);
            System.out.println("Sort [s] / search [e] / filter [f] / remove [r] / reset [t] / change status [c] / exit [x]");

            String input = scan.nextLine();
            if (input.equals("x")) {
                break;
            } else if (input.equalsIgnoreCase("s")) {
                books = BookshopUserList.sortAllUserBooks(books, "");
            } else if (input.equalsIgnoreCase("e")) {
                BookshopUserList.searchUserBooks(books);
            } else if (input.equalsIgnoreCase("f")) {
                books = BookshopUserList.filterForUserBooks(books);
            } else if (input.equalsIgnoreCase("r")) {
                books = Structure.removeBook("/workspaces/Eksamens_praktiskais/data/users/" + User.getCurrentUser() + ".csv", books, "");
            } else if (input.equalsIgnoreCase("c")) {
                books = Structure.changeBookReadingStatus("/workspaces/Eksamens_praktiskais/data/users/" + User.getCurrentUser() + ".csv", books);
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                System.out.println("Invalid input.");
            }   
            
        }
    }

    public static String addBook(ArrayList<Book> givenBooks) throws Exception {
        Scanner scan = new Scanner(System.in);
        ArrayList<UserBook> userBooks = BookManager.allUserBooks();

        String bookId = null;

        //Ask for a valid book ID until found in givenBooks
        while (true) {
            System.out.print("Book ID: ");
            String input = scan.nextLine();

            if (!input.matches("\\d+")) {
                System.out.println("Input has to be a positive integer.");
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
            System.out.println("Input has to be one of the IDs.");
        }

        Book selectedBook = null;
        for (Book book : givenBooks) {
            if (book.getId() == Integer.valueOf(bookId)) {
                selectedBook = book;
                break;
            }
        }
        //Check if book already exists in user's list
        // boolean alreadyExists = false;
        for (Book userBook : userBooks) {
            if (userBook.getName().equals(selectedBook.getName()) && userBook.getAuthor().equals(selectedBook.getAuthor())) {
                // alreadyExists = true;
                return "List already includes this book.";
            }
        }

        // Assign next available ID and add to user's reading list
        selectedBook.setId(userBooks.size() + 1);
        UserBook userSelectedBook = new UserBook(selectedBook.getId(), selectedBook.getName(), selectedBook.getAuthor(), +
        selectedBook.getYear(), selectedBook.getGenre(), selectedBook.getPrice(), false);
        Bookshop.addBookToUserReadingList(userSelectedBook);
        System.out.println("Book added to reading list.");
        return "Book added to reading list.";
        
    }

    public static ArrayList<UserBook> removeBook(String filePath, ArrayList<UserBook> givenBooks, String start) throws Exception {
        String tempFile = "/workspaces/Eksamens_praktiskais/data/users/" + User.getCurrentUser() + "Temp.csv";
        File oldReadingList = new File(filePath);
        File newReadingList = new File(tempFile);
        
        Scanner scan = new Scanner(System.in);
        ArrayList<UserBook> modifiedBooks = new ArrayList<>();

        int deleteLine = -1;
         String deletedBook = "";

        while (true) {
            System.out.print("Enter ID to remove: ");
            String input = scan.nextLine();
            try {
                boolean NOMATCH = true;
                int id = Integer.parseInt(input);
                for (UserBook book : givenBooks) {
                    if (id == book.getId()) {
                        deleteLine = id;
                        deletedBook = book.getName();
                        NOMATCH = false;
                        break;
                    }
                    
                } if (NOMATCH) {
                    System.out.println("ID out of range.");
                } else {
                    break;
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
            System.out.println("Book removed successfully.");

            // from the given list removes the removed book 
            if (start.equalsIgnoreCase("search")) {
                for (UserBook book : givenBooks) {
                    if (book.getId() != deleteLine) {
                        modifiedBooks.add(book);
                    }
                }
                return modifiedBooks;
            }

            ArrayList<UserBook> revampBooks = BookManager.allUserBooks();
            for (UserBook book : revampBooks) {
                if (book.getName().equals(deletedBook)) {
                    continue;
                }
                modifiedBooks.add(book);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        // returns the given list but without the removed book
        return modifiedBooks;
    }

    public static ArrayList<UserBook> changeBookReadingStatus(String filePath, ArrayList<UserBook> givenBooks) {
        String tempFile = "/workspaces/Eksamens_praktiskais/data/users/" + User.getCurrentUser() + "Temp.csv";
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
                    System.out.println("ID out of range.");
                } else {
                    break;
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
            System.out.println("Book status changed.");


            int counter = 0;
            for (UserBook userBook : givenBooks) {
                if (userBook.getReadingStatus() == true) {
                    counter++;
                }
            } 
            if (counter == givenBooks.size() || counter == 0) { // if only read or unread books it gets removed from the return list
                for (UserBook book : givenBooks) {
                    for (UserBook updatedBook : allBooks) {
                        if (book.getId() == updatedBook.getId() && givenBooks.get(0).getReadingStatus() == updatedBook.getReadingStatus()) {
                            modifiedBooks.add(updatedBook);
                            System.out.println("break");
                        }
                    }
                }

            } else {
                ArrayList<UserBook> newBookList = BookManager.allUserBooks();
                for (UserBook book : newBookList) {
                    modifiedBooks.add(book);
                }
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

    public static void tableFormatAll(ArrayList<Book> givenBooks)  {
        
        System.out.println("================================================================================================");
        System.out.printf("%-3s| ", "ID");
        System.out.printf("%-38s| ", "Book Name");
        System.out.printf("%-18s| ", "Author");
        System.out.printf("%-5s| ", "Year");
        System.out.printf("%-15s| ", "Genre");
        System.out.printf("%-6s| \n", "Price");
        System.out.println("================================================================================================");
        System.out.println("------------------------------------------------------------------------------------------------");
        for (Book book : givenBooks) {
            System.out.printf("%-3d| ", book.getId());
            System.out.printf("%-38s| ", book.getName());
            System.out.printf("%-18s| ", book.getAuthor());
            System.out.printf("%-5d| ", book.getYear());
            System.out.printf("%-15s| ", book.getGenre());
            System.out.printf("%-6.2f| \n", book.getPrice());
            System.out.println("------------------------------------------------------------------------------------------------");
        } 
        System.out.println("================================================================================================");
    }

    public static void tableFormatUser(ArrayList<UserBook> givenBooks){

        Double totalPrice = 0.0;
        int totalYear = 0;

        System.out.println("=========================================================================================================");
        System.out.printf("| %-3s| ", "ID");
        System.out.printf("%-38s| ", "Book Name");
        System.out.printf("%-18s| ", "Author");
        System.out.printf("%-5s| ", "Year");
        System.out.printf("%-15s| ", "Genre");
        System.out.printf("%-6s| ", "Price");
        System.out.printf("%-5s|\n", "Read");
        System.out.println("=========================================================================================================");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        for (UserBook book : givenBooks) {
            totalPrice += book.getPrice();
            totalYear += book.getYear();
            String status = "";
            if (book.getReadingStatus() == true) {
                status = "X";
            }
            System.out.printf("| %-3d| ", book.getId());
            System.out.printf("%-38s| ", book.getName());
            System.out.printf("%-18s| ", book.getAuthor());
            System.out.printf("%-5d| ", book.getYear());
            System.out.printf("%-15s| ", book.getGenre());
            System.out.printf("%-6.2f|   ", book.getPrice());
            System.out.printf("%-3s|\n", status);
            System.out.println("---------------------------------------------------------------------------------------------------------");
        } 
        System.out.println("=========================================================================================================");

        BigDecimal roundedTotal = new BigDecimal(totalPrice);
        BigDecimal yearTotal = new BigDecimal(totalYear);
        BigDecimal yearAverage = yearTotal.divide(
            new BigDecimal(givenBooks.size()),
            0,
            RoundingMode.HALF_UP
        );

        MathContext precision = null;
        // changes precision according to the total sum
        if (totalPrice < 10) {
            precision = new MathContext(3);
        } else if (totalPrice < 100) {
            precision = new MathContext(4);
        } else if (totalPrice > 100) {
            precision = new MathContext(5);
        } 
        roundedTotal = roundedTotal.round(precision);
        
        System.out.printf("| %13s", "Total Price: ");
        System.out.printf("%-30s|", roundedTotal.toPlainString());
        System.out.printf("%51s", "Average Year: ");
        System.out.printf("%-6s |\n", yearAverage.toPlainString());
        System.out.println("=========================================================================================================");
    }

}