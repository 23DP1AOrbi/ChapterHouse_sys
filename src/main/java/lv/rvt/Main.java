package lv.rvt;

import java.util.*;

public class Main 
{
    public static void main( String[] args ) throws Exception
    {

        Scanner scan = new Scanner(System.in);

        System.out.println("add - add a book to the list");
        System.out.println("help - show what the commands do");
        System.out.println("display - show list of books");
        System.out.println("stop - end program");
        System.out.println();

        while (true) {
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("stop")) {
                break;
            }

            else if (input.equalsIgnoreCase("display")) {
                ArrayList<Book> books = Bookshop.allBooks();

                for (Book book : books) {
                    System.out.println(book);
                }
                System.out.println();
            }

            else if (input.equalsIgnoreCase("help")) {
                System.out.println();
                System.out.println("add - add a book to the list");
                System.out.println("help - show what the commands do");
                System.out.println("display - show list of books");
                System.out.println("stop - end program");
                System.out.println();
            }

            else if (input.equalsIgnoreCase("add")) {
                System.out.println("Enter the name of the book: ");
                String name = scan.nextLine();

                System.out.println("Enter the author of the book: ");
                String author = scan.nextLine();

                System.out.println("Enter the year of publication: ");
                int year = Integer.valueOf(scan.nextLine());

                System.out.println("Enter the age category of the book: ");
                String ageCategory = scan.nextLine();

                System.out.println("Enter the price of the book: ");
                double price = Double.valueOf(scan.nextLine());

                Book book = new Book(name, author, year, ageCategory, price);
                Bookshop.addBook(book);
                System.out.println("Book got added");
            }
            
        }

        

        // System.out.print(Bookshop.allUsers());

    


    }
}
