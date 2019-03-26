package model;

public class Question {
    private int id;
    private String question_text;
    private int id_test;
    private int get_score;

    public Question(int id, String question_text, int id_test, int get_score){
        this.get_score = get_score;
        this.id = id;
        this.id_test = id_test;
        this.question_text = question_text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public int getId_test() {
        return id_test;
    }

    public void setId_test(int id_test) {
        this.id_test = id_test;
    }

    public int getGet_score() {
        return get_score;
    }

    public void setGet_score(int get_score) {
        this.get_score = get_score;
    }
}
