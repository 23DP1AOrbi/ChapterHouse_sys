package lv.rvt.bookManaging;

public class Book {
    private int id;
    private String name;
    private String author;
    private int year;
    private String genre;
    private double price;

    public Book(int id, String name, String author, int year, String genre, double price) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
        this.genre = genre;
        this.price = price;
    }

    public String toString() {
        return this.id + "| " + this.name + " by " + this.author + ": €" + this.price;
    }

    public String toCSV() {
        return this.id + "," + this.name + "," + this.author + "," + this.year + "," + this.genre + "," + this.price;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public String getAuthor() {
        return this.author;
    }

    public double getPrice() {
        return this.price;
    }

    public int getYear() {
        return this.year;
    }
    
    public String getGenre() {
        return this.genre;
    }
}
