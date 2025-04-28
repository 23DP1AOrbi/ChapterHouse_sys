package lv.rvt.bookManaging;

public class UserBook extends Book {
    private boolean readingStatus;

    public UserBook(int id, String name, String author, int year, String genre, double price, boolean read) {
        super(id, name, author, year, genre, price);
        this.readingStatus = read;
    }

    public boolean getReadingStatus() {
        return this.readingStatus;
    }

    public void setReadingStatus(boolean status) {
        this.readingStatus = status;
    }

    public String toCSV() {
        return super.toCSV() + "," + this.readingStatus;
    }

    public String toString() {
        return super.toString() + " " + this.readingStatus;
    }
    
}
