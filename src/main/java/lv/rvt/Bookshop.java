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

            if (parts[1].toLowerCase().contains(input.toLowerCase()) || parts[2].toLowerCase().contains(input.toLowerCase())) { // checks if the input contains the book name or author
                Book book = new Book(parts[1], parts[2], Integer.valueOf(parts[3]), parts[4], Double.valueOf(parts[5]));
                searchBook.add(book);
            }
        }
        for (Book book2 : searchBook) {
            System.out.println(book2);
        }
        System.out.println();
    }

    public static void sortAllBooks() throws Exception { // change in what order everything is displayed

        ArrayList<String> sortedList = new ArrayList<>();
        BufferedReader reader = Helper.getReader("books.csv");

        Scanner scan = new Scanner(System.in);
        System.out.println("Sort by book [1] or author [2]: ");
        String input;

        while (true) { // Checks if input is 1 or 2
            String enter =  scan.nextLine();
            if (enter.equals("1") || enter.equals("2")) {
                input = enter;
                break;
            }
            else { 
                System.out.println("Input has to be book [1] or author [2]");
             }
        }

       System.out.println("In ascending [A-Z] or descending [Z-A] order.");
       System.out.println("A - ascending");
       System.out.println("D - descending");
       String order;

       while (true) { // Checks if input is a/A or d/D
            String enter =  scan.nextLine();
            if (enter.equalsIgnoreCase("a") || enter.equalsIgnoreCase("d")) {
                order = enter;
                break;
            }
            else { 
                System.out.println("Input has to be ascending [A] or descending [D]");
            }
        }
        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) { //only takes the book name or auhtors and creates an array
            String[] parts = line.split(",");
            sortedList.add(parts[Integer.valueOf(input)-1]);
        }

        Collections.sort(sortedList); // sorts the array alphabetically of names or authors
        ArrayList<Book> ogList = allBooks();
        ArrayList<Book> bookList = new ArrayList<>();
      
        for (String sortedItem : sortedList) {
            for (Book book : ogList) {
                if (book.getName().equals(sortedItem) || book.getAuthor().equals(sortedItem)) {
                    // Add the book to the bookList if it matches either name or author
                    bookList.add(book);
                }
            }
        }
        if (order.equalsIgnoreCase("a")) {
            for (Book book : bookList) {
                System.out.println(book);
            } 
        } else if (order.equalsIgnoreCase("d")) {
            Collections.reverse(bookList);
            for (Book book : bookList) {
                System.out.println(book);
            } 
        }
    }

    public void filter() {}  // choose restrictions for displayed books
}

    // public void login() {
    //     Scanner scan = new Scanner(System.in);

    //     // if () {
            
    //     // }
    // }