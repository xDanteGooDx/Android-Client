package model;

public class Auth_header {
    private String Authorization;

    public Auth_header(String Authorization){
        this.Authorization = Authorization;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }
}
