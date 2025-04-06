package lv.rvt;

import java.util.ArrayList;
import java.util.Scanner;

public class Structure { // structure for the program

    public static void start() throws Exception{  // initial terminal
        Scanner scan = new Scanner(System.in);

        Structure.instructions();

        while (true) {
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("stop")) {
                break;
            }

            else if (input.equalsIgnoreCase("show")) {
                ArrayList<Book> books = Bookshop.allBooks();

                for (Book book : books) {
                    System.out.println(book);
                }
                System.out.println();
            }

            else if (input.equalsIgnoreCase("help")) {
                System.out.println();
                Structure.instructions();
                System.out.println();
            }
            else if (input.equalsIgnoreCase("search")) {
                Bookshop.search();
            }

            else if (input.equalsIgnoreCase("sort")) {
                ArrayList<Book> sortedBooks =  Bookshop.sortAllBooks();

                for (Book book : sortedBooks) {
                    System.out.println(book);
                }
                System.out.println();
            }

            else if (input.equalsIgnoreCase("add")) {
                System.out.println("Enter the name of the book: ");
                String name = scan.nextLine();

                System.out.println("Enter the author of the book: ");
                String author = scan.nextLine();

                System.out.println("Enter the year of publication: ");
                int year = Integer.valueOf(scan.nextLine());

                System.out.println("Enter the genre of the book: ");
                String genre = scan.nextLine();

                System.out.println("Enter the price of the book: ");
                double price = Double.valueOf(scan.nextLine());

                Book book = new Book(name, author, year, genre, price);
                Bookshop.addBook(book);
                System.out.println("Book got added");
            }
        }
    }

    public static void instructions(){ // instruction output
        System.out.println("add - add a book to the list");
        System.out.println("help - show what the commands do");
        System.out.println("search - search for a book or author");
        System.out.println("filter - filter books and sort with differing criteria");
        System.out.println("sort - decide how books are sorted");
        System.out.println("show - show list of books");
        System.out.println("stop - end program");
    }
}