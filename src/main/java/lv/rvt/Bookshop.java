package lv.rvt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.StandardOpenOption;
import lv.rvt.roles.User;
import lv.rvt.tools.Helper;

import java.util.*;
import java.util.stream.Collectors;


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
            User anotherUser = new User(parts[0], parts[1], Boolean.valueOf(parts[2]));
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

    public static void addUser(User user) throws Exception {
        BufferedWriter writer = Helper.getWriter("users.csv", StandardOpenOption.APPEND);
        writer.write(user.toCSV());
        writer.newLine();
        writer.close();
    }

    public static void search() throws Exception { // serach for name or author
        Scanner scan = new Scanner(System.in);
        System.out.println("Search by book name [1], author [2] or both [3]: ");

        String input;
        while (true) { // Checks if input is 1 or 2 or 3
            String enter =  scan.nextLine();
            if (enter.equals("1") || enter.equals("2") || enter.equals("3")) {
                input = enter;
                break;
            }
            else { 
                System.out.println("Input has to be book [1], author [2] or both [3].");
             }
        }
        System.out.print("Your search: "); // user search input
        String search =  scan.nextLine();

        ArrayList<Book> searchBook = new ArrayList<>();
        BufferedReader reader = Helper.getReader("books.csv");

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");

            if (input.equals("1") && parts[0].toLowerCase().contains(search.toLowerCase())) { // adds only books that match user input through book names
                Book book = new Book(parts[0], parts[1], Integer.valueOf(parts[2]), parts[3], Double.valueOf(parts[4]));
                searchBook.add(book);
            } else if (input.equals("2") && parts[1].toLowerCase().contains(search.toLowerCase())) { // adds only books that match user input through author name
                Book book = new Book(parts[0], parts[1], Integer.valueOf(parts[2]), parts[3], Double.valueOf(parts[4]));
                searchBook.add(book);
            } else if (input.equals("3")) {
                if (parts[0].toLowerCase().contains(search.toLowerCase()) || parts[1].toLowerCase().contains(search.toLowerCase())) { // adds any book that contains the search in either book or author name
                    Book book = new Book(parts[0], parts[1], Integer.valueOf(parts[2]), parts[3], Double.valueOf(parts[4]));
                    searchBook.add(book);
                }
            }
        }
        if (searchBook.size() == 0) {
            System.out.println("No matching result.");
        } else {
            for (Book book2 : searchBook) {
                System.out.println(book2);
            }
        } 
    }

    public static ArrayList<Book> sortAllBooks() throws Throwable { // change in what order everything is displayed

        ArrayList<String> sortedList = new ArrayList<>();
        BufferedReader reader = Helper.getReader("books.csv");

        Scanner scan = new Scanner(System.in);
        System.out.println("Sort by book name [1], author [2], price [3] or year [4]: ");
        String input = null;

        while (true) { // Checks if input is 1 or 2
            String enter =  scan.nextLine();
            if (enter.equals("1") || enter.equals("2")) {
                input = enter;
                break;
            } if (enter.equals("3")) {
                ArrayList<Book> books = sortByPrice();
                return books;
            } if (enter.equals("4")) {
                ArrayList<Book> books = sortByYear();
                return books;
            }
            else { 
                System.out.println("Input has to be book name [1], author [2], price [3] or year [4].");
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

        String order = sortDirection();
        if (order.equalsIgnoreCase("a")) {
            return bookList;
        } else if (order.equalsIgnoreCase("d")) { // returns the same list reversed
            Collections.reverse(bookList);
            return bookList;
        }
        return null;
    }

    public static ArrayList<Book> sortByPrice() throws Exception { //similar to sortAllBooks but does it by price instead
        BufferedReader reader = Helper.getReader("books.csv");
        ArrayList<Double> sortedList = new ArrayList<>();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) { //only takes the price
            String[] parts = line.split(",");
            sortedList.add(Double.valueOf(parts[4])); 
        }

        Collections.sort(sortedList); // Sorts by hihghest number
        ArrayList<Book> ogList = allBooks();
        ArrayList<Book> bookList = new ArrayList<>();

        ArrayList<Double> sortedListWithoutDuplicates = (ArrayList<Double>) sortedList.stream().distinct().collect(Collectors.toList()); // removes duplicates from sortedList
        for (double sortedItem : sortedListWithoutDuplicates) {
            for (Book book : ogList) {
                if (book.getPrice() == sortedItem) { // adds book if the price matchces
                    bookList.add(book);
                }
            }
        }

        String order = sortDirection(); // gets how to display the list
        if (order.equalsIgnoreCase("a")) {
            return bookList;
        } else if (order.equalsIgnoreCase("d")) { // returns the same list reversed
            Collections.reverse(bookList);
            return bookList;
        } return null;
    }

    public static ArrayList<Book> sortByYear() throws Throwable {
        BufferedReader reader = Helper.getReader("books.csv");
        ArrayList<Integer> sortedList = new ArrayList<>();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) { //only takes the year
            String[] parts = line.split(",");
            sortedList.add(Integer.valueOf(parts[2])); 
        }

        Collections.sort(sortedList); // Sorts by hihghest number
        ArrayList<Book> ogList = allBooks();
        ArrayList<Book> bookList = new ArrayList<>();

        ArrayList<Integer> sortedListWithoutDuplicates = (ArrayList<Integer>) sortedList.stream().distinct().collect(Collectors.toList()); // removes duplicates from sortedList
        for (int sortedItem : sortedListWithoutDuplicates) {
            for (Book book : ogList) {
                if (book.getYear() == sortedItem) { // adds book if the year matchces
                    bookList.add(book);
                }
            }
        }

        String order = sortDirection(); // gets how to display the list
        if (order.equalsIgnoreCase("a")) {
            return bookList;
        } else if (order.equalsIgnoreCase("d")) { // returns the same list reversed
            Collections.reverse(bookList);
            return bookList;
        } return null;
    }

    public static String sortDirection() { // Checks how to display the sorted list 
        Scanner scan = new Scanner(System.in);
        System.out.println("In ascending [A-Z] or descending [Z-A] order.");
        System.out.println("A - ascending");
        System.out.println("D - descending");

        while (true) { // Checks if input is a/A or d/D
            String enter =  scan.nextLine();
            if (enter.equalsIgnoreCase("a") || enter.equalsIgnoreCase("d")) {
                return enter;
            } else { 
                System.out.println("Input has to be ascending [A] or descending [D].");
            }
        }
    }

    public void filter() {}  // choose restrictions for displayed books


    public static boolean entry() throws Exception {
        Scanner scan = new Scanner(System.in);
       
        while (true) {
            System.out.println("login / register / exit");
            String choice = scan.nextLine();

            if (choice.equalsIgnoreCase("exit")) {
                return false;
            }

            if (choice.equalsIgnoreCase("login")) {
               login();
               return true;
                
            } else if (choice.equalsIgnoreCase("register")) {
                register();
                return true;
                
            } else {
                System.out.println("Must input one of the choices.");
            }      
        } 
    }

    public static void login() throws Exception {
        Scanner scan = new Scanner(System.in);
        ArrayList<User> users = allUsers();

        System.out.println("Enter username: ");
        String username = scan.nextLine();

        String email;
        String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        while (true) {
            System.out.println("Enter email: ");
            String emailRtr = scan.nextLine();

            if (emailRtr.matches(emailPattern)) {
                email = emailRtr;
                break;
            }
            System.out.println("Email must be the right pattern.");
            System.out.println();            
        }

        for (User userInfo : users) {
            if (userInfo.getName().matches(username) && userInfo.getEmail().matches(email)) {
                System.out.println("login works");
                break;
            }
        }
    }

    public static void register() throws Exception {
        Scanner scan = new Scanner(System.in);
        ArrayList<User> users = allUsers();
        boolean REGISTRY = true;

        while (REGISTRY) {
            
            System.out.println("Enter username: ");
            String username = scan.nextLine();

            String email;
            String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

            while (true) { // checks email pattern
                System.out.println("Enter email: ");
                String emailRtr = scan.nextLine();

                if (emailRtr.matches(emailPattern)) {
                    email = emailRtr;
                    break;
                }
                System.out.println("Email must be the right pattern.");
                System.out.println();            
            }

            User user = new User(username, email, false);
            boolean userExists = false;

            for (User userInfo : users) { // checks if user with the same name & email is already being used
                if (userInfo.getName().equals(user.getName()) && userInfo.getEmail().equals(user.getEmail())) {
                    System.out.println("User already exists.");
                    userExists = true;
                    break;
                } else if (userInfo.getEmail().equals(user.getEmail())) { // checks if the email is being used
                    System.out.println("Email already in use.");
                    userExists = true;
                    break;
                } 
            }

            if (!userExists) { // if the user doesnt exist user gets added
                Bookshop.addUser(user);
                System.out.println("New user added");
                break;
            }

            while (true) {
                System.out.println("try again [a] / exit [e]");
                String choice = scan.nextLine();

                if (choice.equalsIgnoreCase("a")) {
                    break;
                } else if (choice.equalsIgnoreCase("e")) { // if user exits, while cycle for registering ends
                    REGISTRY = false;
                    break;
                } else {
                    System.out.println("Input must be a or e");
                    System.out.println();
                }
            }
        }
    }

}