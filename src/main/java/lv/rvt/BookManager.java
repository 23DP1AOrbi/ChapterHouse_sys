package lv.rvt;

import java.io.BufferedReader;
import java.util.ArrayList;

import lv.rvt.roles.User;
import lv.rvt.tools.Helper;

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

    public static ArrayList<Book> allUserBooks() throws Exception {
        ArrayList<Book> books = new ArrayList<>();
        BufferedReader reader = Helper.getReader(User.getCurrentUser() + ".csv");

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            Book anotherBook = new Book(Integer.valueOf(parts[0]), parts[1], parts[2], Integer.valueOf(parts[3]), parts[4], Double.valueOf(parts[5]));
            books.add(anotherBook);
        }
        return books;
    }
}
