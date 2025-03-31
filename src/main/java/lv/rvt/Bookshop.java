package lv.rvt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import lv.rvt.roles.User;
import lv.rvt.tools.Helper;

import java.util.*;


public class Bookshop {


    public static ArrayList<Book> allBooks() throws Exception {
        ArrayList<Book> books = new ArrayList<>();
        BufferedReader reader = Helper.getReader("books.csv");

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            Book anotherBook = new Book(parts[0], parts[1], Integer.valueOf(parts[2]), parts[3], Double.valueOf(parts[4]));
            books.add(anotherBook);
        }
        return books;
    }

    public static ArrayList<User> allUsers() throws Exception {
        ArrayList<User> users = new ArrayList<>();
        BufferedReader reader = Helper.getReader("users.csv");

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            User anotherUser = new User(parts[0], parts[1], parts[2], parts[3]);
            users.add(anotherUser);
        }
        return users;
    }

    public static void addBook(Book book) throws Exception {
        BufferedWriter writer = Helper.getWriter("books.csv", StandardOpenOption.APPEND);
        writer.write(book.toCSV());
        writer.newLine();
        writer.close();
    }

    public static void search() throws Exception { // serach for name or author
        Scanner scan = new Scanner(System.in);
        System.out.println("Search for book or author name: ");

        String input = scan.nextLine();
        System.out.println();

        ArrayList<Book> searchBook = new ArrayList<>();
        BufferedReader reader = Helper.getReader("books.csv");

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");

            if (parts[0].toLowerCase().contains(input.toLowerCase()) || parts[1].toLowerCase().contains(input.toLowerCase())) {
                Book book = new Book(parts[0], parts[1], Integer.valueOf(parts[2]), parts[3], Double.valueOf(parts[4]));
                searchBook.add(book);
            }
        }

        for (Book book2 : searchBook) {
            System.out.println(book2);
        }
        System.out.println();
    }

    public static void sortAllBooks() throws Exception { // change in what order everything is displayed
        try {

        ArrayList<String> bookList = new ArrayList<>();
        BufferedReader reader = Helper.getReader("books.csv");

        Scanner scan = new Scanner(System.in);
        System.out.println("Sort by book [1] or author [2]: ");
        int input = Integer.valueOf(scan.nextLine());;

    //     while (true) { // Checks if input is 1 or 2
    //         String enter =  scan.nextLine();
    //         if (enter.equals("1") || enter.equals("2")) {
    //             input = Integer.valueOf(enter);
    //             break;
    //         }
    //         else { 
    //             System.out.println("Input has to be book [1] or author [2]");
    //          }
    //    }

        String line;
        reader.readLine();
        reader.mark(0);
        while ((line = reader.readLine()) != null) { //only takes the book name or auhtors and creates an array
            String[] parts = line.split(",");
            bookList.add(parts[input-1]);
        }

        Collections.sort(bookList); // sorts the array alphabetically of names or authors

        BufferedReader newReader = Helper.getReader("books.csv");
        ArrayList<Book> books = new ArrayList<>();


        // DOESNT WORK

        String newLine;
        newReader.readLine();
        int counter = 0;
        newReader.mark(1);
        while ((newLine = newReader.readLine()) != null) {
            
            String[] parts = newLine.split(",");

                
            

            // for (int i = 0; i < bookList.size(); i++) {
            Book anotherBook = new Book(parts[0], parts[1], Integer.valueOf(parts[2]), parts[3], Double.valueOf(parts[4]));
            String checkPart = parts[input-1];
            
            
         
                if (bookList.get(counter).equalsIgnoreCase(checkPart)) { ////anotherBook.getName()
                    System.out.println(bookList.get(counter));
                    // System.out.println(books.get(i));
                    books.add(anotherBook);
                    counter++;
                    newReader.reset();
                }
                // }
                

        
            
               
            
               
                // }

                // if (bookList.get(i).equalsIgnoreCase(parts[0]) || bookList.get(i).equalsIgnoreCase(parts[1])) {
                //     books.add(anotherBook);
                //     System.out.println(parts[input-1]);
                //     System.out.println(anotherBook);
                // }
                // if (parts[0].equalsIgnoreCase(bookList.get(i)) || parts[1].equalsIgnoreCase(bookList.get(i))) {
                //     books.add(anotherBook);
                    // System.out.println(parts[input-1]);
                    // System.out.println(anotherBook);
                // }
            }
        // }
        System.out.println(bookList);
        System.out.println(books);
        } catch (IOException e) {
            
            System.out.println("stop bitching");
        } 
    }

    // public static void sortBooks() throws Exception{
    //     sortAllBooks();
    // }

    public void filter() {}  // choose restrictions for displayed books
}

    // public void login() {
    //     Scanner scan = new Scanner(System.in);

    //     // if () {
            
    //     // }
    // }