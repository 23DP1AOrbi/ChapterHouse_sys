package lv.rvt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.StandardOpenOption;

import lv.rvt.bookManaging.Book;
import lv.rvt.bookManaging.BookManager;
import lv.rvt.bookManaging.UserBook;
import lv.rvt.colors.ConsoleColors;
import lv.rvt.tools.Helper;
import lv.rvt.user.User;

import java.util.*;
import java.util.stream.Collectors;


public class Bookshop {
    private static String lastSortMode = "5";
    private static String lastSortDirection = "a";
    private static String temporarySortMode = "5";
    private static String temporarySortDirection = "a";
    

    public static ArrayList<User> allUsers() throws Exception {
        ArrayList<User> users = new ArrayList<>();
        BufferedReader reader = Helper.getReader("users.csv");

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            User anotherUser = new User(parts[0], parts[1]);
            users.add(anotherUser);
        }
        return users;
    }

    public static void addBookToUserReadingList(UserBook selectedBook) throws IOException {
        BufferedWriter writer = Helper.getWriter("users/" + User.getCurrentUser() + ".csv", StandardOpenOption.APPEND);
        writer.write(selectedBook.toCSV());
        writer.newLine();
        writer.close();
    }

    public static void addUser(User user) throws Exception {
        BufferedWriter writer = Helper.getWriter("users.csv", StandardOpenOption.APPEND);
        writer.write(user.toCSV());
        writer.newLine();
        writer.close();
    }

    public static String search(ArrayList<Book> givenBooks) throws Throwable { // serach for name or author
        Scanner scan = new Scanner(System.in);
        

        String input;
        while (true) { // Checks if input is 1 or 2 or 3
            System.out.println("Search by book name [1], author [2] or both [3]: ");
            String enter =  scan.nextLine();
            if (enter.equals("1") || enter.equals("2") || enter.equals("3")) {
                input = enter;
                break;
            }
            else { 
                System.out.println(ConsoleColors.RED_BRIGHT +  "Invalid input." + ConsoleColors.WHITE);
             }
        }

        System.out.print("Your search: "); // user search input
        String search =  scan.nextLine();

        while (search.equals("")) {
            System.out.println(ConsoleColors.RED_BRIGHT +  "Invalid input." + ConsoleColors.WHITE);
            search = scan.nextLine();
        }

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
        String message = "";

        if (searchBook.size() == 0) {
            return ConsoleColors.BLUE_BRIGHT + "No matching results." + ConsoleColors.RESET;
        } else {
            while (true) {
                clearScreen();
                titlePage();
                tableFormatAll(searchBook, "search");

                if (!message.equals("")) {
                    System.out.println(message);
                }
                System.out.println("add [a] / sort [s] / exit [x]");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("x")) {
                    break;
                } else if (choice.equalsIgnoreCase("a")) {
                    message =  Structure.addBook(searchBook, "search");
                    System.out.println(searchBook);
                } else if (choice.equalsIgnoreCase("s")) {
                    searchBook = Bookshop.sortAllBooks(searchBook, "search");
                } else {
                    message = ConsoleColors.RED_BRIGHT +  "Invalid input." + ConsoleColors.WHITE;
                }
            }
            return "";
        } 
    }

    public static ArrayList<Book> sortAllBooks(ArrayList<Book> unsortedBooks, String search) throws Throwable  { // change in what order everything is displayed

        ArrayList<String> sortedList = new ArrayList<>();
        BufferedReader reader = Helper.getReader("books.csv");

        Scanner scan = new Scanner(System.in);
        System.out.println("Sort by");
        System.out.println("name [1] / author [2] / price [3] / year [4] / id [5]");
        String input;
        while (true) {
            String enter =  scan.nextLine();
            if (enter.equals("1") || enter.equals("2")) {
                input = enter;
                if (!(search.equals("search"))) {
                    lastSortMode = enter;
                }
                temporarySortMode = enter;
                break;
            } if (enter.equals("3")) {
                String order = sortDirection("");
                if (!(search.equals("search"))) {
                    lastSortMode = enter;
                    lastSortDirection = order;
                } 
                temporarySortMode = enter;
                temporarySortDirection = order;
                ArrayList<Book> books = sortByPrice(unsortedBooks, order);
                return books;
            } if (enter.equals("4")) {
                String order = sortDirection("");
                if (!(search.equals("search"))) {
                    lastSortMode = enter;
                    lastSortDirection = order;
                } 
                temporarySortMode = enter;
                temporarySortDirection = order;
                ArrayList<Book> books = sortByYear(unsortedBooks, order);
                return books;
            } if (enter.equals("5")) {
                String order = sortDirection("");
                if (!(search.equals("search"))) {
                    lastSortMode = enter;
                    lastSortDirection = order;
                } 
                temporarySortMode = enter;
                temporarySortDirection = order;
                ArrayList<Book> books = sortByID(unsortedBooks, order);
                return books;
            }
            else { 
                System.out.println(ConsoleColors.RED_BRIGHT + "Invalid input." + ConsoleColors.RESET);
                System.out.println("name [1] / author [2] /price [3] / year [4] / id [5]");
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
        String order = sortDirection("text");
        if (!(search.equals("search"))) {
            lastSortDirection = order;
        }
        // returns the same list reversed
        if (order.equalsIgnoreCase("d")) { 
            Collections.reverse(bookList);
        }
        return bookList;
    }

    public static ArrayList<Book> applyLastSort(ArrayList<Book> givenBooks) throws Throwable {
        if (lastSortMode.equals("3")) {
            return sortByPrice(givenBooks, lastSortDirection);
        } else if (lastSortMode.equals("4")) {
            return sortByYear(givenBooks, lastSortDirection);
        } else if (lastSortMode.equals("5")) {
            return sortByID(givenBooks, lastSortDirection);
        } else {

            ArrayList<String> sortedList = new ArrayList<>();

            for (Book book : givenBooks) {
                if (lastSortMode.equals("1")) {
                    sortedList.add(book.getName());
                } else if (lastSortMode.equals("2")) {
                    sortedList.add(book.getAuthor());
                }
            }
            Collections.sort(sortedList);
            ArrayList<Book> sortedBooks = new ArrayList<>();
            for (String sortedItem : sortedList) {
                for (Book book : givenBooks) {
                    if (book.getName().equals(sortedItem) || book.getAuthor().equals(sortedItem)) {
                        sortedBooks.add(book);
                    }
                }
            }
            if (lastSortDirection.equalsIgnoreCase("d")) {
                Collections.reverse(sortedBooks);
            }
            return sortedBooks;
        }
    }

    public static ArrayList<Book> sortByPrice(ArrayList<Book> unsortedBooks, String order) throws Exception { //similar to sortAllBooks but does it by price instead
        BufferedReader reader = Helper.getReader("books.csv");
        ArrayList<Double> sortedList = new ArrayList<>();

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
        if (order.equalsIgnoreCase("d")) {
            Collections.reverse(bookList);
        }
        return bookList;
    }

    public static ArrayList<Book> sortByYear(ArrayList<Book> unsortedBooks, String order) throws Throwable {
        BufferedReader reader = Helper.getReader("books.csv");
        ArrayList<Integer> sortedList = new ArrayList<>();

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
        if (order.equalsIgnoreCase("d")) {
            Collections.reverse(bookList);
        }
        return bookList;
    }

    public static ArrayList<Book> sortByID(ArrayList<Book> givenBooks, String order) throws Throwable { 
        ArrayList<Book> sortedList = new ArrayList<>();
        ArrayList<Book> allBooks = BookManager.allBooks();

        ArrayList<Integer> IDs = new ArrayList<>();
        for (Book book : allBooks) {
            IDs.add(book.getId());
        }

        for (Integer id : IDs) {
            for (Book book : givenBooks) {
                if (book.getId() == id) {
                    sortedList.add(book);
                }
            }
        }
        if (order.equalsIgnoreCase("d")) {
            Collections.reverse(sortedList);
        }
        return sortedList;
    }

    public static String sortDirection(String sortingVersion) { // Checks how to display the sorted list 
        Scanner scan = new Scanner(System.in);
        System.out.println("Order by");
        
        
        if (sortingVersion.equals("text")) {
            System.out.println("A -> [A-Z]");
            System.out.println("D -> [Z-A]");
        } else {
            System.out.println("A - ↑");
            System.out.println("D - ↓");
        }

        System.out.println("Ascending  [a] / descending [d]");
        while (true) { // Checks if input is a/A or d/D
            String enter =  scan.nextLine();
            if (enter.equalsIgnoreCase("a") || enter.equalsIgnoreCase("d")) {
                return enter;
            } else { 
                System.out.println(ConsoleColors.RED_BRIGHT + "Input has to be ascending [A] or descending [D]." + ConsoleColors.RESET);
            }
        }
    }

    public static boolean filterGenre(String input) {
        return input.equalsIgnoreCase("a");
    }

    protected static Map<String, Boolean> genreFilters = new HashMap<>();

    static {
        genreFilters.put("Fantasy", true);
        genreFilters.put("Romance", true);
        genreFilters.put("Dystopian", true);
        genreFilters.put("Modern Fiction", true);
        genreFilters.put("Historical", true);
        genreFilters.put("Non-Fiction", true);
    }

    public static ArrayList<Book> filterAllBooks(ArrayList<Book> givenBooks) throws Throwable { // choose restrictions for displayed books
        
        Scanner scan = new Scanner(System.in);
        ArrayList<Book> filteredBookList = new ArrayList<>();

            String input;
            while (true) {
                System.out.println("Add [a] / add all [l] / remove [r] / exit [x]");

                String enter = scan.nextLine();
                if (enter.equalsIgnoreCase("x")) {
                    break;
                } else if (enter.equalsIgnoreCase("l")) {
                    genreFilters.put("Fantasy", true);
                    genreFilters.put("Romance", true);
                    genreFilters.put("Dystopian", true);
                    genreFilters.put("Modern Fiction", true);
                    genreFilters.put("Historical", true);
                    genreFilters.put("Non-Fiction", true);

                    return givenBooks;
                }
                else if (enter.equalsIgnoreCase("a") || enter.equalsIgnoreCase("r")) {
                    input = enter;

                while (true) {
                    System.out.println("Fantasy [f] / romance [r] / dystopian [d] / Modern Fiction [m] / Historical [h] / Non-Fiction [n] / exit [x]");
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
                    } else if (userGenre.equalsIgnoreCase("m")) {
                        genreFilters.put("Modern Fiction", filterGenre(input));
                        break;
                    } else if (userGenre.equalsIgnoreCase("h")) {
                        genreFilters.put("Historical", filterGenre(input));
                        break;
                    } else if (userGenre.equalsIgnoreCase("n")) {
                        genreFilters.put("Non-Fiction", filterGenre(input));
                        break;
                    } else {
                        System.out.println(ConsoleColors.RED_BRIGHT + "Invalid input." + ConsoleColors.RESET);
                        }
                    }


                    ArrayList<Book> allBooks = BookManager.allBooks();
                    ArrayList<Book> filteredBooks = new ArrayList<>();
                    for (Book book : allBooks) {
                        // adds books which genres return true
                        boolean genreEnabled = genreFilters.getOrDefault(book.getGenre(), false);
                        if (genreEnabled) {
                                filteredBooks.add(book);
                                System.out.println("Book genre:" + book.getGenre());
                            }
                    } 
                    filteredBooks = Bookshop.applyLastSort(filteredBooks);

                    clearScreen();
                    titlePage();
                    tableFormatAll(filteredBooks, "");


                filteredBookList = filteredBooks;
            } else {
                System.out.println(ConsoleColors.RED_BRIGHT + "Invalid input." + ConsoleColors.RESET);
            }
        }
        return filteredBookList;
    } 

    public static ArrayList<Book> applyLastGenreFilter(ArrayList<Book> givenBooks) {
        ArrayList<Book> filteredBooks = new ArrayList<>();
    
        for (Book book : givenBooks) {
            boolean genreEnabled = genreFilters.getOrDefault(book.getGenre(), false);
            if (genreEnabled) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public static boolean entry() throws Exception {
        Scanner scan = new Scanner(System.in);
        
        while (true) {
            clearScreen();
            titlePage();
            
            System.out.println("                               login / register / exit");
            System.out.println();
            System.out.print("                                       ");
            String choice = scan.nextLine();
            if (choice.equalsIgnoreCase("exit")) {
                clearScreen();
                return false;
            }
            if (choice.equalsIgnoreCase("login")) {
               return login();
                
            } else if (choice.equalsIgnoreCase("register")) {
                return register();
            } else {
                System.out.println(ConsoleColors.RED_BRIGHT + "Must input one of the choices." + ConsoleColors.WHITE);
            }      
        } 
    }

    public static boolean login() throws Exception {
        Scanner scan = new Scanner(System.in);
        ArrayList<User> users = allUsers();
        boolean REGISTRY = true;
        boolean QUIT = false;

        while (REGISTRY) {
            clearScreen();
            titlePage();
            
            String username;
            while (true) { // username check - cant be blank or shorter than 4 characters
                System.out.println("                                   Enter username: ");
                System.out.print("                                        ");
                String name = scan.nextLine();

                if (!(name.equals(null)) && name.length() > 3) { 
                    username = name;
                    break;
                } else {
                    System.out.println(ConsoleColors.BLUE_BRIGHT +"                        Username must be at least 4 characters." + ConsoleColors.WHITE);
                }
            }

            String email;
            String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
            while (true) { // checks if input matches email pattern
                System.out.println();
                System.out.println("                                   Enter email: ");
                System.out.print("                                      ");
                String emailRtr = scan.nextLine();

                if (emailRtr.matches(emailPattern)) {
                    email = emailRtr;
                    break;
                }
                System.out.println(ConsoleColors.RED_BRIGHT + "                           Email must be the right pattern." + ConsoleColors.WHITE);           
            }

            boolean userExists = false;
            for (User userInfo : users) { // if user exists stops the method and returns true
                if (userInfo.getName().matches(username) && userInfo.getEmail().matches(email)) {
                    userInfo.setCurrentUser(username);
                    userExists = true;
                    REGISTRY = false;
                    
                    return true;
                } else if (userInfo.getEmail().matches(email)) {
                    System.out.println(ConsoleColors.RED_BRIGHT + "                                  Wrong username." + ConsoleColors.WHITE);
                }
            }

            while (!userExists) {
                clearScreen();
                titlePage();
                System.out.println();
                System.out.println(ConsoleColors.RED_BRIGHT + "                                  User doesn't exist" + ConsoleColors.WHITE);
                System.out.println();
                System.out.println("                                try again [a] / exit [e]");
                System.out.print("                                   ");
                String choice = scan.nextLine();

                if (choice.equalsIgnoreCase("a")) {
                    break;
                } else if (choice.equalsIgnoreCase("e")) { // if user exits, the while cycle for registering ends
                    REGISTRY = false;                                    // and goes back to the intro for registering choice
                    boolean FAIL = entry();
                    QUIT = FAIL;
                    break;
                } else {
                    System.out.println(ConsoleColors.RED_BRIGHT + "                                Input must be a or e." + ConsoleColors.WHITE);
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
            clearScreen();
            titlePage();
            
            String username;
            while (true) { // username check - cant be blank or shorter than 4 characters
                System.out.println("                                   Enter username: ");
                System.out.print("                                        ");
                String name = scan.nextLine();

                if (!(name.equals(null)) && name.length() > 3) {
                    username = name;
                    break;
                } else {
                    System.out.println(ConsoleColors.BLUE_BRIGHT + "                        Username has to be at least 4 characters." + ConsoleColors.WHITE);
                }
            }

            String email;
            String emailPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

            while (true) { // checks email pattern
                System.out.println("                                   Enter email: ");
                System.out.print("                                      ");
                String emailRtr = scan.nextLine();

                if (emailRtr.matches(emailPattern)) {
                    email = emailRtr;
                    break;
                }
                System.out.println(ConsoleColors.RED_BRIGHT +"                           Email must be the right pattern." + ConsoleColors.WHITE);
                System.out.println();            
            }

            User user = new User(username, email);
            boolean userExists = false;
            
            for (User userInfo : users) { // checks if user with the same name & email is already being used
                if (userInfo.getName().equals(user.getName()) && userInfo.getEmail().equals(user.getEmail())) {
                    System.out.println(ConsoleColors.RED_BRIGHT + "                                  User already exists." + ConsoleColors.WHITE);
                    userExists = true;
                    break;
                } else if (userInfo.getEmail().equals(user.getEmail())) { // checks if the email is being used
                    System.out.println(ConsoleColors.RED_BRIGHT + "                                  Email already in use." + ConsoleColors.WHITE);
                    userExists = true;
                    break;
                } else if (userInfo.getName().equals(user.getName())) {
                    System.out.println(ConsoleColors.RED_BRIGHT + "                                  Username is taken." + ConsoleColors.WHITE);
                    userExists = true;
                    break;
                }
            }

            if (!userExists) { // if the user doesnt exist user gets added
                Bookshop.addUser(user);
                user.setCurrentUser(username);
                System.out.println("New user added");

                // make new file to store added books
                File file = new File("/workspaces/Eksamens_praktiskais/data/users/"+ user.getName() +".csv");
                file.createNewFile(); 

                BufferedWriter writer = Helper.getWriter("users/" + user.getName()+".csv", StandardOpenOption.APPEND);

                writer.write("Id,Name,Author,Year,Genre,Price (EUR),Finished reading");
                writer.newLine();
                writer.close();
                return true;
            }

            while (true) {
                System.out.println("                                try again [a] / exit [e]");
                System.out.print("                                   ");
                String choice = scan.nextLine();

                if (choice.equalsIgnoreCase("a")) {
                    break;
                } else if (choice.equalsIgnoreCase("e")) { // if user exits, the while cycle for registering ends
                    REGISTRY = false;                                    // and goes back to the intro for registering choice
                    boolean FAIL = entry();
                    QUIT = FAIL;
                    break;
                } else {
                    System.out.println(ConsoleColors.RED_BRIGHT + "                                Input must be a or e" + ConsoleColors.WHITE);
                    System.out.println();
                }
            }
        }
        return QUIT;
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public static void tableFormatAll(ArrayList<Book> givenBooks, String start) throws Exception  {

        String idColor = "";
        String nameColor = "";
        String authorColor = "";
        String yearColor = "";
        String priceColor = "";

        if (start.equals("search")) {
            if (temporarySortMode.equals("5")) {
                idColor = ConsoleColors.PURPLE;
            } else if (temporarySortMode.equals("1")) {
                nameColor = ConsoleColors.PURPLE;
            } else if (temporarySortMode.equals("2")) {
                authorColor = ConsoleColors.PURPLE;
            } else if (temporarySortMode.equals("3")) {
                priceColor = ConsoleColors.PURPLE;
            } else if (temporarySortMode.equals("4")) {
                yearColor = ConsoleColors.PURPLE;
            }
        } else {
            if (lastSortMode.equals("5")) {
                idColor = ConsoleColors.PURPLE;
            } else if (lastSortMode.equals("1")) {
                nameColor = ConsoleColors.PURPLE;
            } else if (lastSortMode.equals("2")) {
                authorColor = ConsoleColors.PURPLE;
            } else if (lastSortMode.equals("3")) {
                priceColor = ConsoleColors.PURPLE;
            } else if (lastSortMode.equals("4")) {
                yearColor = ConsoleColors.PURPLE;
            }
        }

        System.out.println("==================================================================================================");
        System.out.print("| " + idColor + "ID" + ConsoleColors.RESET + " |");
        System.out.print(" " + nameColor + "Book Name" + ConsoleColors.RESET + "                             ");
        System.out.print("| " + authorColor + "Author" + ConsoleColors.RESET + "            ");
        System.out.print("| " + yearColor + "Year" + ConsoleColors.RESET + " ");
        System.out.print("| Genre          ");
        System.out.print("| " + priceColor + "Price" + ConsoleColors.RESET + " |\n");
        System.out.println("==================================================================================================");
        System.out.println("--------------------------------------------------------------------------------------------------");

        for (Book book : givenBooks) {
            System.out.printf("| %-3d| ", book.getId());
            System.out.printf("%-38s| ", book.getName());
            System.out.printf("%-18s| ", book.getAuthor());
            System.out.printf("%-5d| ", book.getYear());
            System.out.printf("%-15s| ", book.getGenre());
            System.out.printf("%-6.2f| \n", book.getPrice());
            System.out.println("--------------------------------------------------------------------------------------------------");
        } 
        System.out.println("==================================================================================================");

        ArrayList<Book> bookTotal = BookManager.allBooks();
        System.out.printf("| %50s", "Visible Books: ");
        System.out.printf("%3s/", givenBooks.size());
        System.out.printf("%-41s|\n", bookTotal.size());
        System.out.println("==================================================================================================");
        visibleFilters(start);
    }

    public static void titlePage() {

        System.out.println(ConsoleColors.YELLOW +"             _____  _                    _");
        System.out.println("            /  __ \\| |                  | |");
        System.out.println("            | /  \\/| |__    __ _  _ __  | |_   ___  _ __");
        System.out.println("            | |    | '_ \\  / _` || '_ \\ | __| / _ \\| '__|");
        System.out.println(ConsoleColors.PURPLE + "            | \\__/\\| | | || (_| || |_) || |_ |  __/| |");
        System.out.println("             \\____/|_| |_| \\__,_|| .__/  \\__| \\___||_| ");
        System.out.println("                                 |_|" + ConsoleColors.YELLOW +" _   _ ");
        System.out.println("                                    | |_| |  ___   _   _  ___   ___ ");
        System.out.println("                                    |  _  | / _ \\ | | | |/ __| / _ \\" + ConsoleColors.PURPLE);
        System.out.println("                                    | | | || (_) || |_| |\\__ \\|  __/");
        System.out.println("                                    \\_| |_/ \\___/  \\__,_||___/ \\___|");
        System.out.println(ConsoleColors.WHITE + "");
        System.out.println("");


        System.out.println("                          .-~~~~~~~~~-._       _.-~~~~~~~~~-.");
        System.out.println("                      __.'              ~.   .~              `.__");
        System.out.println("                    .'//                  \\./                  \\\\`.");
        System.out.println("                  .'//                     |                     \\\\`.");
        System.out.println("                .'// .-~\"\"\"\"\"\"\"~~~~-._     |     _,-~~~~\"\"\"\"\"\"\"~-. \\\\`.");
        System.out.println("              .'//.-\"                 `-.  |  .-'                 \"-.\\\\`.");
        System.out.println("           .'//______.============-..   \\ | /   ..-============.______\\\\`.");
        System.out.println("         .'______________________________\\|/______________________________`.");
        System.out.println("      =========================================================================");
        System.out.println();
    }

    public static void visibleFilters(String start) {
        // genre filter
        String fantasyStatus = " ";
        String romanceStatus = " ";
        String dystopianStatus = " ";
        String modernStatus = " ";
        String historicalStatus = " ";
        String nonfictionStatus = " ";

        // sort direction 
        String alphabet = " ";
        String alphReverse = " ";
        String ascending = " ";
        String descending = " ";

        if (start.equals("search")) {
            if (temporarySortMode.equals("1") || temporarySortMode.equals("2")) {
                if (temporarySortDirection.equals("a")) {
                    alphabet = "X";
                } else {
                    alphReverse = "X";
                }
            } else {
                if (temporarySortDirection.equals("a")) {
                    ascending = "X";
                } else {
                    descending = "X";
                }
            }
        } else {
            if (lastSortMode.equals("1") || lastSortMode.equals("2")) {
                if (lastSortDirection.equals("a")) {
                    alphabet = "X";
                } else {
                    alphReverse = "X";
                }
            } else {
                if (lastSortDirection.equals("a")) {
                    ascending = "X";
                } else {
                    descending = "X";
                }
            }
        }
        

        if (genreFilters.get("Fantasy")) {
            fantasyStatus = "X";
        } if (genreFilters.get("Romance")) {
            romanceStatus = "X";
        } if (genreFilters.get("Dystopian")) {
            dystopianStatus = "X";
        } if (genreFilters.get("Modern Fiction")) {
            modernStatus = "X";
        } if (genreFilters.get("Historical")) {
            historicalStatus = "X";
        } if (genreFilters.get("Non-Fiction")) {
            nonfictionStatus = "X";
        }

        System.out.println("==================================================================================================");
        System.out.printf("| %60s", "Active Genres & Sorting Direction");
        System.out.printf("%36s\n", "|");
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.printf("| %22s", "Fantasy [" + fantasyStatus + "]");
        System.out.printf("%22s", "Romance [" + romanceStatus + "]");
        System.out.printf("%22s", " Dystopian [" + dystopianStatus + "]");
        System.out.printf("%15s", "A-Z [" + alphabet + "]");
        System.out.printf("%10s", "↑ [" + ascending + "]");
        System.out.printf("%5s\n", "|");
        System.out.printf("| %22s", "Modern Fiction [" + modernStatus + "]");
        System.out.printf("%22s", "Historical [" + historicalStatus + "]");
        System.out.printf("%22s", "Non-Fiction [" + nonfictionStatus + "]");
        System.out.printf("%15s", "Z-A [" + alphReverse + "]");
        System.out.printf("%10s", "↓ [" + descending + "]");
        System.out.printf("%5s\n", "|");
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.println("==================================================================================================");
    }

}