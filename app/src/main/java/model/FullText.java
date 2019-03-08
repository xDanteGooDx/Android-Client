package model;

public class FullText {
    private int id;
    private String text_html;
    private int id_book;

    public FullText(int id, String html, int id_book){
        this.id = id;
        this.text_html = html;
        this.id_book = id_book;
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

    public String getText_html() {
        return text_html;
    }

    public void setText_html(String text_html) {
        this.text_html = text_html;
    }
}
