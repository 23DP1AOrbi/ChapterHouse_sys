package lv.rvt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import lv.rvt.roles.User;
import lv.rvt.tools.Helper;

import java.util.*;
import java.util.stream.Collectors;


public class Bookshop {
    

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

    public static void addBookToUserReadingList(Book book) throws IOException {
        BufferedWriter writer = Helper.getWriter(User.getCurrentUser() + ".csv", StandardOpenOption.APPEND);
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

    public static void search(String user, ArrayList<Book> givenBooks) throws Throwable { // serach for name or author
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
        for (Book book : givenBooks) { 
            // adds only books that match user search with name
            if (input.equals("1") && book.getName().toLowerCase().contains(search.toLowerCase())) {
                searchBook.add(book);
            // adds only books that match user search with author
            } if (input.equals("2") && book.getAuthor().toLowerCase().contains(search.toLowerCase())) {
                searchBook.add(book);
            // adds any book that matches user search
            } if (input.equals("3")) {
                if (book.getName().toLowerCase().contains(search.toLowerCase()) || book.getAuthor().toLowerCase().contains(search.toLowerCase())) {
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
            System.out.println();
            System.out.println("Matching results: " + searchBook.size());
            System.out.println();

            while (user.equals("")) {
                System.out.println("add [a] / exit [x] / sort [s]");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("x")) {
                    break;
                } else if (choice.equalsIgnoreCase("a")) {
                    Structure.addBook(searchBook);
                } else if (choice.equalsIgnoreCase("s")) {
                    ArrayList<Book> sortedSearch = Bookshop.sortAllBooks(user, searchBook);
                    for (Book book : sortedSearch) {
                        System.out.println(book.toString());
                    }
                }
                else {
                    System.out.println("Invalid input.");
                }
            }
        } 
        
    }

    public static ArrayList<Book> sortAllBooks(String user, ArrayList<Book> unsortedBooks) throws Throwable { // change in what order everything is displayed

        ArrayList<String> sortedList = new ArrayList<>();
        BufferedReader reader;

        
            if (user.equals("")) {
                BufferedReader bookReader = Helper.getReader("books.csv");
                reader = bookReader;
            } else {
                BufferedReader bookReader = Helper.getReader(User.getCurrentUser() + ".csv");
                reader = bookReader;
            }
        

        Scanner scan = new Scanner(System.in);
        System.out.println("Sort by book name [1], author [2], price [3] or year [4]: ");
        String input;

        while (true) { // Checks if input is 1/2/3/4
            String enter =  scan.nextLine();
            if (enter.equals("1") || enter.equals("2")) {
                input = enter;
                break;
            } if (enter.equals("3")) {
                ArrayList<Book> books = sortByPrice(user, unsortedBooks);
                return books;
            } if (enter.equals("4")) {
                ArrayList<Book> books = sortByYear(user, unsortedBooks);
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
            sortedList.add(parts[Integer.valueOf(input)]); 
        }

        Collections.sort(sortedList); // sorts the array alphabetically of names or authors
        ArrayList<Book> bookList = new ArrayList<>();
      
        for (String sortedItem : sortedList) {
            for (Book book : unsortedBooks) {
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

    public static ArrayList<Book> sortByPrice(String user, ArrayList<Book> unsortedBooks) throws Exception { //similar to sortAllBooks but does it by price instead
        BufferedReader reader;
        ArrayList<Double> sortedList = new ArrayList<>();

       
            if (user.equals("")) {
                BufferedReader bookReader = Helper.getReader("books.csv");
                reader = bookReader;
            } else {
                BufferedReader bookReader = Helper.getReader(User.getCurrentUser() + ".csv");
                reader = bookReader;
            }
        

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) { //only takes the price
            String[] parts = line.split(",");
            sortedList.add(Double.valueOf(parts[5])); 
        }

        Collections.sort(sortedList); // Sorts by hihghest number
        ArrayList<Book> bookList = new ArrayList<>();

        // removes duplicates from sortedList
        ArrayList<Double> sortedListWithoutDuplicates = (ArrayList<Double>) sortedList.stream().distinct().collect(Collectors.toList()); 
        for (double sortedItem : sortedListWithoutDuplicates) {
            for (Book book : unsortedBooks) {
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

    public static ArrayList<Book> sortByYear(String user, ArrayList<Book> unsortedBooks) throws Throwable {
        BufferedReader reader;
        ArrayList<Integer> sortedList = new ArrayList<>();

        
            if (user.equals("")) {
                BufferedReader bookReader = Helper.getReader("books.csv");
                // ArrayList<Book> bookList = BookManager.allBooks();
                // ogList = bookList;
                reader = bookReader;
            } else {
                BufferedReader bookReader = Helper.getReader(User.getCurrentUser() + ".csv");
                // ArrayList<Book> bookList = BookManager.allUserBooks();
                // ogList = bookList;
                reader = bookReader;
            }
        

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) { //only takes the year
            String[] parts = line.split(",");
            sortedList.add(Integer.valueOf(parts[3])); 
        }

        Collections.sort(sortedList); // Sorts by hihghest number
        ArrayList<Book> bookList = new ArrayList<>();

        // removes duplicates from sortedList
        ArrayList<Integer> sortedListWithoutDuplicates = (ArrayList<Integer>) sortedList.stream().distinct().collect(Collectors.toList());
        for (int sortedItem : sortedListWithoutDuplicates) {
            for (Book book : unsortedBooks) {
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
        System.out.println("A - ascending ↑");
        System.out.println("D - descending ↓");

        while (true) { // Checks if input is a/A or d/D
            String enter =  scan.nextLine();
            if (enter.equalsIgnoreCase("a") || enter.equalsIgnoreCase("d")) {
                return enter;
            } else { 
                System.out.println("Input has to be ascending [A] or descending [D].");
            }
        }
    }

    public static boolean filterGenre(String input) {
        return input.equalsIgnoreCase("a");
    }

    private static Map<String, Boolean> genreFilters = new HashMap<>();

    static {
        genreFilters.put("Fantasy", true);
        genreFilters.put("Romance", true);
        genreFilters.put("Dystopian", true);
        genreFilters.put("Contemporary Fiction", true);
        genreFilters.put("Historical Fiction", true);
        genreFilters.put("Non-Fiction", true);
    }

    public static ArrayList<Book> filter(String user, ArrayList<Book> givenBooks) throws Exception { // choose restrictions for displayed books
        
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> filteredBookList = new ArrayList<>();


            String input;
            
            while (true) {
                System.out.println("Add [a] / remove [r] / exit [x]");

                String enter = scan.nextLine();
                if (enter.equalsIgnoreCase("x")) {
                    break;
                } else if (enter.equalsIgnoreCase("a") || enter.equalsIgnoreCase("r")) {
                    input = enter;

                while (true) {
                    System.out.println("Fantasy [f] / romance [r] / dystopian [d] / Contemporary Fiction [c] / Historical [h] / Non-Fiction [n] / exit [x]");
                    String userGenre = scan.nextLine();

                    if (userGenre.equalsIgnoreCase("x")) {
                        break;
                    } else if (userGenre.equalsIgnoreCase("f")) {
                        genreFilters.put("Fantasy", filterGenre(input));
                        break;
                    } else if (userGenre.equalsIgnoreCase("r")) {
                        genreFilters.put("Romance", filterGenre(input));
                        break;
                    } else if (userGenre.equalsIgnoreCase("d")) {
                        genreFilters.put("Dystopian", filterGenre(input));
                        break;
                    } else if (userGenre.equalsIgnoreCase("c")) {
                        genreFilters.put("Contemporary Fiction", filterGenre(input));
                        break;
                    } else if (userGenre.equalsIgnoreCase("h")) {
                        genreFilters.put("Historical Fiction", filterGenre(input));
                        break;
                    } else if (userGenre.equalsIgnoreCase("n")) {
                        genreFilters.put("Non-Fiction", filterGenre(input));
                        break;
                    } else {
                        System.out.println("Input has to be one of the given choices.");
                        }
                    }

                    ArrayList<Book> allBooks;
                    if (user.equals("")) {
                        ArrayList<Book> allBookshopBooks = BookManager.allBooks();
                        allBooks = allBookshopBooks;
                    } else {
                        ArrayList<Book> allUserBooks = BookManager.allUserBooks();
                        allBooks = allUserBooks;
                    }


                    
                    ArrayList<Book> filteredBooks = new ArrayList<>();
                    for (Book book : allBooks) {
                        boolean genreEnabled = genreFilters.getOrDefault(book.getGenre(), false);
                    
                        if (genreEnabled) {
                            // for (Book givenBook : givenBooks) {
                            //     if (givenBook.getGenre().equals(book.getGenre())) {
                                    filteredBooks.add(book);
                                    // break;
                                // }
                            }
                        }
                    // }
                    // for (Book book : allBooks) {
                    //     if (genreFilters.get("Fantasy")) {
                    //         for (Book givenBook : givenBooks) {
                    //             if (givenBook.getName().equals(book.getName())) {
                    //                 filteredBooks.add(book);
                    //                 break;
                    //             }
                    //         }
                    //     } else if (genreFilters.get("Romance")) {
                    //         for (Book givenBook : givenBooks) {
                    //             if (givenBook.getName().equals(book.getName())) {
                    //                 filteredBooks.add(book);
                    //                 break;
                    //             }
                    //         }
                    //     } else if (genreFilters.get("Dystopian")) {
                    //         for (Book givenBook : givenBooks) {
                    //             if (givenBook.getName().equals(book.getName())) {
                    //                 filteredBooks.add(book);
                    //                 break;
                    //             }
                    //         }
                    //     } else if (genreFilters.get("Contemporary Fiction")) {
                    //         for (Book givenBook : givenBooks) {
                    //             if (givenBook.getName().equals(book.getName())) {
                    //                 filteredBooks.add(book);
                    //                 break;
                    //             }
                    //         }
                    //     } else if (genreFilters.get("Historical Fiction")) {
                    //         for (Book givenBook : givenBooks) {
                    //             if (givenBook.getName().equals(book.getName())) {
                    //                 filteredBooks.add(book);
                    //                 break;
                    //             }
                    //         }
                    //     } else if (genreFilters.get("Non-Fiction")) {
                    //         for (Book givenBook : givenBooks) {
                    //             if (givenBook.getName().equals(book.getName())) {
                    //                 filteredBooks.add(book);
                    //                 break;
                    //             }
                    //         }
                    //     }
                        
                    // }

                // System.out.println(genreFilters.get("Fantasy") + " " + genreFilters.get("Romance") + " " + genreFilters.get("Dystopian") + " " 
                // + genreFilters.get("Contemporary Fiction") + " " + genreFilters.get("Historical Fiction") + " " + genreFilters.get("Non-Fiction"));
                // ArrayList<Book> filteredBooks = new ArrayList<>();
                // for (Book book : givenBooks) {
              

                for (Book book : filteredBooks) {
                    if (genreFilters.getOrDefault(book.getGenre(), false)) {
                        System.out.println(book.toString());
  
                    } 
                }
                filteredBookList = filteredBooks;
                // System.out.println(filteredBooks);
            } else {
                System.out.println("Invalid input.");
            }
        }
        return filteredBookList;
    } 

    public static boolean entry() throws Exception {
        Scanner scan = new Scanner(System.in);
       
        while (true) {
            System.out.println("login / register / exit");
            String choice = scan.nextLine();

            if (choice.equalsIgnoreCase("exit")) {
                return false;
            }

            if (choice.equalsIgnoreCase("login")) {
               return login();
                
            } else if (choice.equalsIgnoreCase("register")) {
                return register();
                
            } else {
                System.out.println("Must input one of the choices.");
            }      
        } 
    }

    public static boolean login() throws Exception {
        Scanner scan = new Scanner(System.in);
        ArrayList<User> users = allUsers();
        boolean REGISTRY = true;
        boolean QUIT = false;

        while (REGISTRY) {

        String username;
            while (true) { // username check - cant be blank or shorter than 4 characters
                System.out.println("Enter username: ");
                String name = scan.nextLine();

                if (!(name.equals(null)) && name.length() > 3) { 
                    username = name;
                    break;
                } else {
                    System.out.println("Username must be at least 4 characters.");
                }
            }

            String email;
            String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            

            while (true) { // checks if input matches email pattern
                System.out.println("Enter email: ");
                String emailRtr = scan.nextLine();

                if (emailRtr.matches(emailPattern)) {
                    email = emailRtr;
                    break;
                }
                System.out.println("Email must be the right pattern.");
                System.out.println();            
            }

            boolean userExists = false;

            
            for (User userInfo : users) { // if user exists stops the method and returns true
                if (userInfo.getName().matches(username) && userInfo.getEmail().matches(email)) {
                    userInfo.setCurrentUser(username);
                    userExists = true;
                    REGISTRY = false;
                    
                    return true;
                } else if (userInfo.getEmail().matches(email)) {
                    System.out.println("Wrong username.");
                }
            }

            while (!userExists) {
                System.out.println("User doesn't exist");
                System.out.println("try again [a] / exit [e]");
                String choice = scan.nextLine();

                if (choice.equalsIgnoreCase("a")) {
                    break;
                } else if (choice.equalsIgnoreCase("e")) { // if user exits, the while cycle for registering ends
                    REGISTRY = false;                                    // and goes back to the intro for registering choice
                    boolean FAIL = entry();
                    QUIT = FAIL;
                    break;
                } else {
                    System.out.println("Input must be a or e");
                    System.out.println();
                }
            }
        }
        return QUIT;
    }

    public static boolean register() throws Exception {
        Scanner scan = new Scanner(System.in);
        ArrayList<User> users = allUsers();
        boolean REGISTRY = true;
        boolean QUIT = false;

        while (REGISTRY) {
            
            String username;
            while (true) { // username check - cant be blank or shorter than 4 characters
                System.out.println("Enter username: ");
                String name = scan.nextLine();

                if (!(name.equals(null)) && name.length() > 3) {
                    username = name;
                    break;
                } else {
                    System.out.println("Username has to be at least 4 characters.");
                }
            }

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
                } else if (userInfo.getName().equals(user.getName())) {
                    System.out.println("Username is taken.");
                    userExists = true;
                    break;
                }
            }

            if (!userExists) { // if the user doesnt exist user gets added
                Bookshop.addUser(user);
                user.setCurrentUser(username);
                System.out.println("New user added");

                // make new file to store added books
                File file = new File("/workspaces/Eksamens_praktiskais/data/"+ user.getName() +".csv");
                file.createNewFile();

                BufferedWriter writer = Helper.getWriter(user.getName()+".csv", StandardOpenOption.APPEND);

                writer.write("Id,Name,Author,Year,Genre,Price (EUR)");
                writer.newLine();
                writer.close();
                return true;
            }

            while (true) {
                System.out.println("try again [a] / exit [e]");
                String choice = scan.nextLine();

                if (choice.equalsIgnoreCase("a")) {
                    break;
                } else if (choice.equalsIgnoreCase("e")) { // if user exits, the while cycle for registering ends
                    REGISTRY = false;                                    // and goes back to the intro for registering choice
                    boolean FAIL = entry();
                    QUIT = FAIL;
                    break;
                } else {
                    System.out.println("Input must be a or e");
                    System.out.println();
                }
            }
        }
        return QUIT;
    }
}