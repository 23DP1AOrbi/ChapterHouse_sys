package lv.rvt;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import lv.rvt.bookManaging.BookManager;
import lv.rvt.bookManaging.UserBook;
import lv.rvt.colors.ConsoleColors;
import lv.rvt.tools.Helper;
import lv.rvt.user.User;

public class BookshopUserList extends Bookshop {
    private static String lastSortMode = "5";
    private static String lastSortDirection = "a";
    private static String temporarySortMode = "5";
    private static String temporarySortDirection = "a";

    public static ArrayList<UserBook> sortAllUserBooks(ArrayList<UserBook> unsortedBooks, String search) { // change in what order everything is displayed

        BufferedReader reader;
        try {
            reader = Helper.getReader("users/" + User.getCurrentUser() + ".csv");
            ArrayList<String> sortedList = new ArrayList<>();
            Scanner scan = new Scanner(System.in);

            System.out.println("Sort by");
            System.out.println("name [1] / author [2] / price [3] / year [4] / id [5]");
            String input;
            while (true) {
                String enter =  scan.nextLine();
                if (enter.equals("1") || enter.equals("2")) {
                    input = enter;
                    temporarySortMode = enter;
                    if (!(search.equals("search"))) {
                        lastSortMode = enter;
                    }
                    break;
                } if (enter.equals("3")) {
                    String order = sortDirection("");
                    if (!(search.equals("search"))) {
                        lastSortMode = enter;
                        lastSortDirection = order;
                    } 
                    temporarySortMode = enter;
                    temporarySortDirection = order;
                    ArrayList<UserBook> books = sortByPriceForUser(unsortedBooks, order);
                    return books;
                } if (enter.equals("4")) {
                    String order = sortDirection("");
                    if (!(search.equals("search"))) {
                        lastSortMode = enter;
                        lastSortDirection = order;
                    } 
                    temporarySortMode = enter;
                    temporarySortDirection = order;
                    ArrayList<UserBook> books = sortByYearForUser(unsortedBooks, order);
                    return books;
                } if (enter.equals("5")) {
                    String order = sortDirection("");
                    if (!(search.equals("search"))) {
                        lastSortMode = enter;
                        lastSortDirection = order;
                    } 
                    temporarySortMode = enter;
                    temporarySortDirection = order;
                    ArrayList<UserBook> books = sortByIDForUser(unsortedBooks, order);
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
        ArrayList<UserBook> bookList = new ArrayList<>();
        for (String sortedItem : sortedList) {
            for (UserBook book : unsortedBooks) {
                if (book.getName().equals(sortedItem) || book.getAuthor().equals(sortedItem)) {
                    // Add the book to the bookList if it matches either name or author
                    bookList.add(book);
                }
            }
        }

        String order = sortDirection("text");
        temporarySortDirection = order;
        // if used in search the order doesnt get saved outside it
        if (!(search.equals("search"))) {
            lastSortDirection = order;
        }
        
        if (order.equalsIgnoreCase("d")) { // returns the same list reversed
            Collections.reverse(bookList);
        }
        return bookList;
        } catch (Throwable e) {
            e.printStackTrace();
        }   return unsortedBooks;
    }

    public static ArrayList<UserBook> applyLastSortForUser(ArrayList<UserBook> givenBooks) throws Throwable {
        if (lastSortMode.equals("3")) {
            return sortByPriceForUser(givenBooks, lastSortDirection);
        } else if (lastSortMode.equals("4")) {
            return sortByYearForUser(givenBooks, lastSortDirection);
        } else if (lastSortMode.equals("5")) {
            return sortByIDForUser(givenBooks, lastSortDirection);
        } else {

            ArrayList<String> sortedList = new ArrayList<>();
            BufferedReader reader = Helper.getReader("books.csv");
            reader.readLine();
            
            for (String line; (line = reader.readLine()) != null;) {
                String[] parts = line.split(",");
                sortedList.add(parts[Integer.parseInt(lastSortMode)]);
            }
            Collections.sort(sortedList);
            ArrayList<UserBook> sortedBooks = new ArrayList<>();
            for (String sortedItem : sortedList) {
                for (UserBook book : givenBooks) {
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

    public static ArrayList<UserBook> sortByIDForUser(ArrayList<UserBook> givenBooks, String order) throws Throwable { 
        ArrayList<UserBook> sortedList = new ArrayList<>();
        ArrayList<UserBook> allBooks = BookManager.allUserBooks();

        ArrayList<Integer> IDs = new ArrayList<>();
        for (UserBook book : allBooks) {
            IDs.add(book.getId());
        }

        for (Integer id : IDs) {
            for (UserBook book : givenBooks) {
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

    public static ArrayList<UserBook> sortByPriceForUser(ArrayList<UserBook> unsortedBooks, String order) throws Exception { //similar to sortAllBooks but does it by price instead

        BufferedReader reader = Helper.getReader("users/" + User.getCurrentUser() + ".csv");
        ArrayList<Double> sortedList = new ArrayList<>();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) { //only takes the price
            String[] parts = line.split(",");
            sortedList.add(Double.valueOf(parts[5])); 
        }

        Collections.sort(sortedList); // Sorts by hihghest number
        ArrayList<UserBook> bookList = new ArrayList<>();

        // removes duplicates from sortedList
        ArrayList<Double> sortedListWithoutDuplicates = (ArrayList<Double>) sortedList.stream().distinct().collect(Collectors.toList()); 
        for (double sortedItem : sortedListWithoutDuplicates) {
            for (UserBook book : unsortedBooks) {
                if (book.getPrice() == sortedItem) { // adds book if the price matchces
                    bookList.add(book);
                }
            }
        }

        if (order.equalsIgnoreCase("d")) { // returns the same list reversed
            Collections.reverse(bookList);
        }
        return bookList;
    }

    public static ArrayList<UserBook> sortByYearForUser(ArrayList<UserBook> unsortedBooks, String order) throws Throwable {
        BufferedReader reader  = Helper.getReader("users/" + User.getCurrentUser() + ".csv");;
        ArrayList<Integer> sortedList = new ArrayList<>();

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) { //only takes the year
            String[] parts = line.split(",");
            sortedList.add(Integer.valueOf(parts[3])); 
        }

        Collections.sort(sortedList); // Sorts by hihghest number
        ArrayList<UserBook> bookList = new ArrayList<>();

        // removes duplicates from sortedList
        ArrayList<Integer> sortedListWithoutDuplicates = (ArrayList<Integer>) sortedList.stream().distinct().collect(Collectors.toList());
        for (int sortedItem : sortedListWithoutDuplicates) {
            for (UserBook book : unsortedBooks) {
                if (book.getYear() == sortedItem) { // adds book if the year matchces
                    bookList.add(book);
                }
            }
        }
        if (order.equalsIgnoreCase("d")) { // returns the same list reversed
            Collections.reverse(bookList);
        }
        return bookList;
    }

    public static String searchUserBooks (ArrayList<UserBook> givenBooks) throws Exception {
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

        ArrayList<UserBook> searchBook = new ArrayList<>();
        for (UserBook book : givenBooks) { 
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

            while (true) {
                if (searchBook.size() == 0 || search.equals("")) {
                    return ConsoleColors.RED_BRIGHT + "No matching result." + ConsoleColors.WHITE;
                }
                clearScreen();
                titlePage();
                tableFormatUser(searchBook, "search");
                
                System.out.println(message);
                System.out.println("remove [r] / sort [s] / change status [c] / exit [x]  ");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("x")) {
                    message = "";
                    break;
                } else if (choice.equalsIgnoreCase("r")) {
                    searchBook = Structure.removeBook("/Eksamens_praktiskais/data/users/" + User.getCurrentUser() + ".csv", searchBook, "search");
                    givenBooks = searchBook;
                    if (searchBook.size() == 0) {
                        return ConsoleColors.GREEN_BRIGHT + "Book removed." + ConsoleColors.WHITE;
                    }
                } else if (choice.equalsIgnoreCase("s")) {
                    searchBook = BookshopUserList.sortAllUserBooks(searchBook, "search");
                } else if (choice.equalsIgnoreCase("c")) {
                    searchBook = Structure.changeBookReadingStatus("/Eksamens_praktiskais/data/users/" + User.getCurrentUser() + ".csv", searchBook);
                } else {
                    message = ConsoleColors.RED_BRIGHT +  "Invalid input." + ConsoleColors.WHITE;
                }
            }
        return message;
    }

    private static Map<String, Boolean> genreFilters = new HashMap<>();
    private static Map<String, Boolean> statusFilters = new HashMap<>();

    static {
        genreFilters.put("Fantasy", true);
        genreFilters.put("Romance", true);
        genreFilters.put("Dystopian", true);
        genreFilters.put("Modern Fiction", true);
        genreFilters.put("Historical", true);
        genreFilters.put("Non-Fiction", true);
    }

    static {
        statusFilters.put("Finished", true);
        statusFilters.put("Unfinished", true);
    }

    public static ArrayList<UserBook> filterForUserBooks(ArrayList<UserBook> givenBooks, boolean reset) throws Throwable { // choose restrictions for displayed books
        
        Scanner scan = new Scanner(System.in);
        ArrayList<UserBook> filteredBookList = new ArrayList<>();

        boolean GENREFILTER = true;
        String input;

        if (reset == true) {
            genreFilters.put("Fantasy", true);
            genreFilters.put("Romance", true);
            genreFilters.put("Dystopian", true);
            genreFilters.put("Modern Fiction", true);
            genreFilters.put("Historical", true);
            genreFilters.put("Non-Fiction", true);

            statusFilters.put("Finished", true);
            statusFilters.put("Unfinished", true);

            lastSortMode = "5";
            lastSortDirection = "a";
            return BookManager.allUserBooks(); 
        }

        while (true) {
            System.out.println("Filter by");
            System.out.println("Genre [g] / status [s] / exit [x]");

            String enter = scan.nextLine();
            if (enter.equalsIgnoreCase("x")) {
                GENREFILTER = false;
                break;
            } else if (enter.equalsIgnoreCase("g")) {
                break;
            } else if (enter.equalsIgnoreCase("s")) {
                while (true) {
                    GENREFILTER = false;
                    System.out.println("finished [f] / not finished [n] / all [a] / exit [x]");
                    input = scan.nextLine();

                    if (input.equalsIgnoreCase("x")) {
                        GENREFILTER = true;
                        break;
                    } else if (input.equalsIgnoreCase("a")) {
                        // returns all books and switches the all key values to true
                        ArrayList<UserBook> allBooks = BookManager.allUserBooks();
                        statusFilters.put("Finished", true);
                        statusFilters.put("Unfinished", true);
                        return allBooks;
                    }

                    else if (input.equalsIgnoreCase("f")) {
                        ArrayList<UserBook> allBooks = BookManager.allUserBooks();
                        ArrayList<UserBook> readBooks = new ArrayList<>();

                        for (UserBook book : allBooks) {
                            // only adds books that return true (read) & abide by the genre filter
                            if (book.getReadingStatus() == true && genreFilters.getOrDefault(book.getGenre(), false)) {
                                readBooks.add(book);
                            } 
                        } if (readBooks.size() == 0) {
                            System.out.println("No read books.");
                        }
                        // changes hashmap keys accordingly to finished (true) books
                        statusFilters.put("Unfinished", false);
                        statusFilters.put("Finished", true);
                        return readBooks;
                    } else if (input.equalsIgnoreCase("n")) {
                        ArrayList<UserBook> allBooks = BookManager.allUserBooks();
                        ArrayList<UserBook> readBooks = new ArrayList<>();

                        for (UserBook book : allBooks) {
                            // only adds books that return false (unread) & abide by the genre filter
                            if (book.getReadingStatus() == false && genreFilters.getOrDefault(book.getGenre(), false)) {
                                readBooks.add(book);
                            }
                        } if (readBooks.size() == 0) {
                            System.out.println("No unread books.");
                        }
                        // changes hashmap keys accordingly to unfinished (false) books
                        statusFilters.put("Finished", false);
                        statusFilters.put("Unfinished", true);
                        return readBooks;
                    } else {
                        System.out.println("Invalid input.");
                    }
                }
            } 
        }
            
            while (GENREFILTER) {
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

                    ArrayList<UserBook> allBooks = BookManager.allUserBooks();
                    ArrayList<UserBook> filteredBooks = new ArrayList<>();
                    for (UserBook book : allBooks) {
                        String status;
                        if (book.getReadingStatus() == true) {
                            status = "Finished";
                        } else {
                            status = "Unfinished";
                        }
                        // adds books only by status
                        if (statusFilters.getOrDefault(status, false)) {
                                filteredBooks.add(book); 
                        }
                    }
                    return filteredBooks;
                }
                else if (enter.equalsIgnoreCase("a") || enter.equalsIgnoreCase("r")) {
                    input = enter;

                while (true) {
                    System.out.println("Fantasy [f] / romance [r] / dystopian [d] / Modern Fiction [m] / Historical [h] / Non-Fiction [n] / exit [x]");
                    String userGenre = scan.nextLine();

                    if (userGenre.equalsIgnoreCase("x")) {
                        break;
                        // changes the hashmap return according to input
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
                        System.out.println("Input has to be one of the given choices.");
                        }
                    }

                    ArrayList<UserBook> allBooks = BookManager.allUserBooks();
                    ArrayList<UserBook> filteredBooks = new ArrayList<>();

                    for (UserBook book : allBooks) {
                        String status;
                        if (book.getReadingStatus() == true) {
                            status = "Finished";
                        } else {
                            status = "Unfinished";
                        }
                        // adds books that only are the allowed genres and status
                        if (genreFilters.getOrDefault(book.getGenre(), false) && 
                            statusFilters.getOrDefault(status, false)) {
                                filteredBooks.add(book); 
                        }
                    }
                        clearScreen();
                        titlePage();
                        tableFormatUser(filteredBooks, "");

                filteredBookList = filteredBooks;
            } else {
                System.out.println("Invalid input.");
            }
        }
        if (filteredBookList.size() == 0) {
            filteredBookList = givenBooks;
        }
        return filteredBookList;
    } 

    public static ArrayList<UserBook> applyLastFilterForUser(ArrayList<UserBook> givenBooks) {
        ArrayList<UserBook> filteredBooks = new ArrayList<>();
    
        for (UserBook book : givenBooks) {
            boolean genreEnabled = genreFilters.getOrDefault(book.getGenre(), false);

            String status = String.valueOf(book.getReadingStatus());
            if (status.equals("true")) {
                status = "Finished";
            } else {
                status = "Unfinished";
            }
            boolean statusEnabled = statusFilters.getOrDefault(status, false);
            
            if (genreEnabled && statusEnabled) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }

    public static void visibleFilters(String start){

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

        System.out.println("=========================================================================================================");
        System.out.printf("| %60s", "Active Genres & Sorting Direction");
        System.out.printf("%43s\n", "|");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.printf("| %22s", "Fantasy [" + fantasyStatus + "]");
        System.out.printf("%22s", "Romance [" + romanceStatus + "]");
        System.out.printf("%22s", " Dystopian [" + dystopianStatus + "]");
        System.out.printf("%15s", "A-Z [" + alphabet + "]");
        System.out.printf("%10s", "↑ [" + ascending + "]");
        System.out.printf("%12s\n", "|");
        System.out.printf("| %22s", "Modern Fiction [" + modernStatus + "]");
        System.out.printf("%22s", "Historical [" + historicalStatus + "]");
        System.out.printf("%22s", "Non-Fiction [" + nonfictionStatus + "]");
        System.out.printf("%15s", "Z-A [" + alphReverse + "]");
        System.out.printf("%10s", "↓ [" + descending + "]");
        System.out.printf("%12s\n", "|");
        System.out.println("---------------------------------------------------------------------------------------------------------");
        System.out.println("=========================================================================================================");
    }

  
    public static void tableFormatUser(ArrayList<UserBook> givenBooks, String start) throws Exception{

        Double totalPrice = 0.0;
        int totalYear = 0;
        String idColor = "";
        String nameColor = "";
        String authorColor = "";
        String yearColor = "";
        String priceColor = "";


        // colors in the last active sorting method
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


        System.out.println("=========================================================================================================");
        System.out.printf("| %56s", User.getCurrentUser() + "'s Reading List");
        System.out.printf("%47s\n", "|");
        System.out.println("=========================================================================================================");
        System.out.print("| " + idColor + "ID" + ConsoleColors.RESET + " |");
        System.out.print(" " + nameColor + "Book Name" + ConsoleColors.RESET + "                             ");
        System.out.print("| " + authorColor + "Author" + ConsoleColors.RESET + "            ");
        System.out.print("| " + yearColor + "Year" + ConsoleColors.RESET + " ");
        System.out.print("| Genre          ");
        System.out.print("| " + priceColor + "Price" + ConsoleColors.RESET + " ");
        System.out.print("| Read |\n");
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
        

        if (givenBooks.size() != 0) {
            BigDecimal roundedTotal = new BigDecimal(totalPrice);
            BigDecimal yearTotal = new BigDecimal(totalYear);
            BigDecimal yearAverage = yearTotal.divide(
            new BigDecimal(givenBooks.size()), 0, RoundingMode.HALF_UP);

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
        ArrayList<UserBook> bookTotal = BookManager.allUserBooks();

        System.out.printf("| %13s", "Total Price: ");
        System.out.printf("%-30s|", roundedTotal.toPlainString());
        System.out.printf("%51s", "Average Year: ");
        System.out.printf("%-6s |\n", yearAverage.toPlainString());
        System.out.println("=========================================================================================================");
        System.out.printf("| %50s", "Visible Books: ");
        System.out.printf("%3s/", givenBooks.size());
        System.out.printf("%-48s|\n", bookTotal.size());
        System.out.println("=========================================================================================================");
        }  
        visibleFilters(start);
    }
}
