package lv.rvt;

import java.util.*;

public class Main 
{
    public static void main( String[] args ) throws Exception
    {
        ArrayList<Book> books = Bookshop.allBooks();

        for (Book book : books) {
            System.out.println(book);
        }

    }
}
