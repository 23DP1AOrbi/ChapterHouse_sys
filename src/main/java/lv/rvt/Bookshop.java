package lv.rvt;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import lv.rvt.tools.Helper;


public class Bookshop {
    private Book book;

    public static ArrayList<Book> allBooks() throws Exception {
        ArrayList<Book> books = new ArrayList<>();
        BufferedReader reader = Helper.getReader("books.csv");

        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(", ");
            Book anotheBook = new Book(parts[0], parts[1], Integer.valueOf(parts[2]), parts[3]);
            books.add(anotheBook);
        }
        return books;
    }
    
}