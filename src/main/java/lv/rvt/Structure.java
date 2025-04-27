package lv.rvt;

import java.io.BufferedReader;
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

    public static void show(ArrayList<Book> showBooks) throws Throwable{
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.println("Show all books [a] / show your reading list [l] / exit [x].");
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("x")) {
                break;
            } else if (input.equalsIgnoreCase("a")) {
                // ArrayList<Book> books = BookManager.allBooks();
                // for (Book book : books) {
                //     System.out.println(book);
                // }
                System.out.println();
                choiceAllBooks();
                // break;
            } else if (input.equalsIgnoreCase("l")) {
                ArrayList<UserBook> books = BookManager.allUserBooks();
                if (books.size() == 0) {
                    System.out.println("Empty reading list.");
                } else {
                    // for (UserBook book : books) {
                    //     System.out.println(book);
                    // }
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
            books = Bookshop.applyLastSort(books);
            books = Bookshop.applyLastGenreFilter(books);
            for (Book book : books) {
                System.out.println(book.toString());
            }
            System.out.println("Sort [s] / search [e] / filter [f] / add [a] / exit [x]");

            String input = scan.nextLine();

            if (input.equals("x")) {
                break;
            } else if (input.equalsIgnoreCase("s")) {
                books = Bookshop.sortAllBooks(books);
            } else if (input.equalsIgnoreCase("e")) {
                Bookshop.search(books);
            } else if (input.equalsIgnoreCase("f")) {
                books = Bookshop.filterAllBooks(books);
            } else if (input.equalsIgnoreCase("a")) {
                Structure.addBook(books);
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

        while (true) {
            // ArrayList<UserBook> books = userBooks;
            System.out.println("Reader list");
            for (UserBook book : books) {
                System.out.println(book.toString());
            }
            System.out.println("Sort [s] / search [e] / filter [f] / remove [r] / change status [c] / exit [x]");

            String input = scan.nextLine();
            if (input.equals("x")) {
                break;
            } else if (input.equalsIgnoreCase("s")) {
                books = BookshopUserList.sortAllUserBooks(books);
            } else if (input.equalsIgnoreCase("e")) {
                BookshopUserList.searchUserBooks(books);
            } else if (input.equalsIgnoreCase("f")) {
                books = BookshopUserList.filterForUserBooks(books);
            } else if (input.equalsIgnoreCase("r")) {
                books = Structure.removeBook("/workspaces/Eksamens_praktiskais/data/users/" + User.getCurrentUser() + ".csv", books);
            } else if (input.equalsIgnoreCase("c")) {
                books = Structure.changeBookReadingStatus("/workspaces/Eksamens_praktiskais/data/users/" + User.getCurrentUser() + ".csv", books);
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                System.out.println("Invalid input.");
            }   
        }
    }

    public static void addBook(ArrayList<Book> givenBooks) throws Exception {
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

        //Check if book already exists in user's list (based on name and author)
        boolean alreadyExists = false;
        for (Book userBook : userBooks) {
            if (userBook.getName().equals(selectedBook.getName()) && userBook.getAuthor().equals(selectedBook.getAuthor())) {
                alreadyExists = true;
                break;
            }
        }

        if (alreadyExists) {
            System.out.println("List already includes this book.");
        } else {
            // Assign next available ID and add to user's reading list
            selectedBook.setId(userBooks.size() + 1);
            UserBook userSelectedBook = new UserBook(selectedBook.getId(), selectedBook.getName(), selectedBook.getAuthor(), +
            selectedBook.getYear(), selectedBook.getGenre(), selectedBook.getPrice(), false);
            Bookshop.addBookToUserReadingList(userSelectedBook);
            System.out.println("Book added to reading list.");
        }
    }

    public static ArrayList<UserBook> removeBook(String filePath, ArrayList<UserBook> givenBooks) throws Exception {
        String tempFile = "/workspaces/Eksamens_praktiskais/data/" + User.getCurrentUser() + "Temp.csv";
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
            // String wait = scan.nextLine();

            oldReadingList.delete();
            newReadingList.renameTo(oldReadingList);
            System.out.println("Book removed successfully.");

            // from the given list removes the removed book 
            int i = 1;
            System.out.println(deleteLine);
            for (UserBook book : givenBooks) {
                if (book.getId() != deleteLine) {
                    book.setId(i);
                    modifiedBooks.add(book);
                    i++;
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        // returns the given list but without the removed book
        return modifiedBooks;
    }

    public static ArrayList<UserBook> changeBookReadingStatus(String filePath, ArrayList<UserBook> givenBooks) {
        String tempFile = "/workspaces/Eksamens_praktiskais/data/" + User.getCurrentUser() + "Temp.csv";
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
            // String wait = scan.nextLine();

            oldReadingList.delete();
            newReadingList.renameTo(oldReadingList);
            System.out.println("Book status changed.");


            int counter = 0;
            for (UserBook userBook : givenBooks) {
                if (userBook.getReadingStatus() == true) {
                    counter++;
                }
            } 
            System.out.println(counter);
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
                for (UserBook book : givenBooks) {
                    if (book.getId() != modifyStatusID) {
                        modifiedBooks.add(book);
                    } else {
                        book.setReadingStatus(!modifiedBookOriginalStatus);
                        modifiedBooks.add(book);
                    }
                    
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return modifiedBooks;
    }
}