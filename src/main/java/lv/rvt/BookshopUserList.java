package lv.rvt;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import lv.rvt.roles.User;
import lv.rvt.tools.Helper;

public class BookshopUserList extends Bookshop {

    public static ArrayList<UserBook> sortAllUserBooks(ArrayList<UserBook> unsortedBooks) { // change in what order everything is displayed

        BufferedReader reader;
        try {
            reader = Helper.getReader("users/" + User.getCurrentUser() + ".csv");
        ArrayList<String> sortedList = new ArrayList<>();
        Scanner scan = new Scanner(System.in);

        System.out.println("Sort by");
        System.out.println("name [1] / author [2] / price [3] / year [4] / id [5]");
        String input;
        while (true) { // Checks if input is 1/2/3/4
            String enter =  scan.nextLine();
            if (enter.equals("1") || enter.equals("2")) {
                input = enter;
                break;
            } if (enter.equals("3")) {
                ArrayList<UserBook> books = sortByPriceForUser(unsortedBooks);
                return books;
            } if (enter.equals("4")) {
                ArrayList<UserBook> books = sortByYearForUser(unsortedBooks);
                return books;
            } if (enter.equals("5")) {
                ArrayList<UserBook> books = sortByIDForUser(unsortedBooks);
                return books;
            }
            else { 
                System.out.println("Input has to be name [1] / author [2] / price [3] / year [4] / id [5]");
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

        String order = sortDirection();
        if (order.equalsIgnoreCase("d")) { // returns the same list reversed
            Collections.reverse(bookList);
        }
        return bookList;
        } catch (Throwable e) {
            e.printStackTrace();
        }   return unsortedBooks;
    }

    public static ArrayList<UserBook> sortByIDForUser(ArrayList<UserBook> givenBooks) throws Throwable { 
        ArrayList<UserBook> sortedList = new ArrayList<>();
        ArrayList<UserBook> allBooks = BookManager.allUserBooks();

        int highestID = 0;
        for (UserBook book : givenBooks) {
            if (book.getId() > highestID) {
                highestID = book.getId();
            }
        }

        for (int i = 1; i <= highestID; i++) {
            for (UserBook userBook : allBooks) {
                if (userBook.getId() == i) {
                    sortedList.add(userBook);
                }
            }
        }
        return sortedList;
    }

    public static ArrayList<UserBook> sortByPriceForUser(ArrayList<UserBook> unsortedBooks) throws Exception { //similar to sortAllBooks but does it by price instead

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

        String order = sortDirection();
        if (order.equalsIgnoreCase("d")) { // returns the same list reversed
            Collections.reverse(bookList);
        }
        return bookList;
    }

    public static ArrayList<UserBook> sortByYearForUser(ArrayList<UserBook> unsortedBooks) throws Throwable {
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

        String order = sortDirection();
        if (order.equalsIgnoreCase("d")) { // returns the same list reversed
            Collections.reverse(bookList);
        }
        return bookList;
    }

    public static void searchUserBooks (ArrayList<UserBook> givenBooks) throws Exception {
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

        if (searchBook.size() == 0) {
            System.out.println("No matching result.");
        } else {
            for (Book book2 : searchBook) {
                System.out.println(book2);
            }
            System.out.println();
            System.out.println("Matching results: " + searchBook.size());
            System.out.println();

            while (true) {
                if (searchBook.size() == 0) {
                    break;
                }
                System.out.println("remove [r] / exit [x] / sort [s]");
                String choice = scan.nextLine();
                if (choice.equalsIgnoreCase("x")) {
                    break;
                } else if (choice.equalsIgnoreCase("r")) {
                    searchBook = Structure.removeBook("/workspaces/Eksamens_praktiskais/data/" + User.getCurrentUser() + ".csv", searchBook);
                    givenBooks = searchBook;
                } else if (choice.equalsIgnoreCase("s")) {
                    ArrayList<UserBook> sortedSearch = BookshopUserList.sortAllUserBooks(searchBook);
                    for (UserBook book : sortedSearch) {
                        System.out.println(book.toString());
                    }
                }
                else {
                    System.out.println("Invalid input.");
                }
            }
        } 
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

    private static Map<String, Boolean> statusFilters = new HashMap<>();

    static {
        statusFilters.put("Finished", true);
        statusFilters.put("Unfinished", true);
    }

    public static ArrayList<UserBook> filterForUserBooks(ArrayList<UserBook> givenBooks) throws Exception { // choose restrictions for displayed books
        
        Scanner scan = new Scanner(System.in);
        ArrayList<UserBook> filteredBookList = new ArrayList<>();

            boolean GENREFILTER = true;
            String input;

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
                            break;
                        } else if (input.equalsIgnoreCase("a")) {
                            ArrayList<UserBook> allBooks = BookManager.allUserBooks();
                            statusFilters.put("Finished", true);
                            statusFilters.put("Unfinished", true);
                            return allBooks;
                        }

                        else if (input.equalsIgnoreCase("f")) {
                            ArrayList<UserBook> allBooks = BookManager.allUserBooks();
                            ArrayList<UserBook> readBooks = new ArrayList<>();

                            for (UserBook book : allBooks) {
                                if (book.getReadingStatus() == true && genreFilters.getOrDefault(book.getGenre(), false)) {
                                    readBooks.add(book);
                                } 
                            } if (readBooks.size() == 0) {
                                System.out.println("No read books.");
                            }
                            statusFilters.put("Unfinished", false);
                            return readBooks;
                        } else if (input.equalsIgnoreCase("n")) {
                            ArrayList<UserBook> allBooks = BookManager.allUserBooks();
                            ArrayList<UserBook> readBooks = new ArrayList<>();

                            for (UserBook book : allBooks) {
                                if (book.getReadingStatus() == false && genreFilters.getOrDefault(book.getGenre(), false)) {
                                    readBooks.add(book);
                                }
                            } if (readBooks.size() == 0) {
                                System.out.println("No unread books.");
                            }
                            statusFilters.put("Finished", false);
                            return readBooks;
                        } else {
                            System.out.println("Invalid input.");
                        }
                    }
                } 
            }
            
            while (GENREFILTER) {
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

                    ArrayList<UserBook> allBooks = BookManager.allUserBooks();
                    ArrayList<UserBook> filteredBooks = new ArrayList<>();
                    for (UserBook book : allBooks) {
                        String status;
                        if (book.getReadingStatus() == true) {
                            status = "Finished";
                        } else {
                            status = "Unfinished";
                        }
                        if (genreFilters.getOrDefault(book.getGenre(), false) && 
                            statusFilters.getOrDefault(status, false)) {
                                filteredBooks.add(book);
                                System.out.println(book.toString());
                        }
                    }

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

}
