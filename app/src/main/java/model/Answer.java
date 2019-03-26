package model;


public class Answer {
    private int id;
    private String answer_text;
    private int id_question;
    private boolean is_right;

    public Answer(int id, String answer_text, int id_question, boolean is_right) {
        this.id = id;
        this.id_question = id_question;
        this.answer_text = answer_text;
        this.is_right = is_right;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public int getId_question() {
        return id_question;
    }

    public void setId_question(int id_question) {
        this.id_question = id_question;
    }

    public boolean isIs_right() {
        return is_right;
    }

    public void setIs_right(boolean is_right) {
        this.is_right = is_right;
    }
}
