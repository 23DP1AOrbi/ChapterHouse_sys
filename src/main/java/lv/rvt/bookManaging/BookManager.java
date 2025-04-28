package lv.rvt.bookManaging;

import java.io.BufferedReader;
import java.util.ArrayList;

import lv.rvt.tools.Helper;
import lv.rvt.user.User;

public class BookManager {

 public static ArrayList<Book> allBooks() throws Exception {

        ArrayList<Book> books = new ArrayList<>();
        BufferedReader reader = Helper.getReader("books.csv");

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            Book anotherBook = new Book(Integer.valueOf(parts[0]), parts[1], parts[2], Integer.valueOf(parts[3]), parts[4], Double.valueOf(parts[5]));
            books.add(anotherBook);
        }
        return books;
    }

    public static ArrayList<UserBook> allUserBooks() throws Exception {
        ArrayList<UserBook> books = new ArrayList<>();
        BufferedReader reader = Helper.getReader( "/users/" + User.getCurrentUser() + ".csv");

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            UserBook anotherBook = new UserBook(Integer.valueOf(parts[0]), parts[1], parts[2], +
            Integer.valueOf(parts[3]), parts[4], Double.valueOf(parts[5]), Boolean.valueOf(parts[6]));
            books.add(anotherBook);
        }
        return books;
    }
}
