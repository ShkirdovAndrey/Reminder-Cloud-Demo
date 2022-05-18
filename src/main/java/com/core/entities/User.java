package com.core.entities;
import lombok.Getter;
import lombok.Setter;

public class User {
    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;

    public User(String email, String password){
        this.setEmail(email);
        this.setPassword(password);
    }

    public User(String userName, String email, String password){
        this.setUserName(userName);
        this.setEmail(email);
        this.setPassword(password);
    }
}
