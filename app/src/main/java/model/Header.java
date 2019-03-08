package model;

public class Header {
    private int id;
    private int id_book;
    private String text_header;

    Header(int id, int id_book, String text_header){
        this.id = id;
        this.id_book = id_book;
        this.text_header = text_header;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_book() {
        return id_book;
    }

    public void setId_book(int id_book) {
        this.id_book = id_book;
    }

    public String getText_header() {
        return text_header;
    }

    public void setText_header(String text_header) {
        this.text_header = text_header;
    }
}
