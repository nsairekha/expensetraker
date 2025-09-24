package com.expensemanager.expense_backend.model;

public class UserLoginResponse {
    private String token;
    private String message;
    private User user;

    public UserLoginResponse(String token, String message, User user) {
        this.token = token;
        this.message = message;
        this.user = user;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
