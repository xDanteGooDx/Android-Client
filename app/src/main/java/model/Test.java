package model;

public class Test {
    private int id;
    private String test_title;
    private int author;
    private String about;

    public Test(int id, String test_title, int author, String about){
        this.id = id;
        this.test_title = test_title;
        this.author = author;
        this.about = about;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTest_title() {
        return test_title;
    }

    public void setTest_title(String test_title) {
        this.test_title = test_title;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
