package lv.rvt;



public class Book {
    private String name;
    private String author;
    private int year;
    private String ageCategory;
    private double price;

    public Book(String name, String author, int year, String ageCategory, double price) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.ageCategory = ageCategory;
        this.price = price;
    }

    public String toString() {
        return this.name + " " + this.author + ": €" + this.price;
    }

    // public void addBook(String bkName, String bkAtuhor, int releaseYear, String ageCategory) {
    //     Book newBook = new Book(bkName, bkAtuhor, releaseYear, ageCategory);
    //     // Bookshop.allBooks();
    // }

    public String toCSV() {
        return this.name + "," + this.author + "," + this.year + "," + this.ageCategory + "," + this.price;
    }
}
