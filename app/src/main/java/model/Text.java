package model;

public class Text {
    private int id;
    private String text_html;
    private int id_header;

    public Text(int id, String text_html, int id_header){
        this.id = id;
        this.id_header = id_header;
        this.text_html = text_html;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText_html() {
        return text_html;
    }

    public void setText_html(String text_html) {
        this.text_html = text_html;
    }

    public int getId_header() {
        return id_header;
    }

    public void setId_header(int id_header) {
        this.id_header = id_header;
    }
}
