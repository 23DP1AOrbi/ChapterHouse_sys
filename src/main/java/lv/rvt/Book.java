package lv.rvt;



public class Book {
    private String name;
    private String author;
    private int year;
    private String ageCategory;

    public Book(String name, String author, int year, String ageCategory) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.ageCategory = ageCategory;
    }

    public String toString() {
        return this.name + " " + this.author;
    }

    // public void addBook(String bkName, String bkAtuhor, int releaseYear, String ageCategory) {
    //     Book newBook = new Book(bkName, bkAtuhor, releaseYear, ageCategory);
    //     // Bookshop.allBooks();
    // }

    public String toCSV() {
        return this.name + ", " + this.author + ", " + this.year + ", " + this.ageCategory;
    }
}
