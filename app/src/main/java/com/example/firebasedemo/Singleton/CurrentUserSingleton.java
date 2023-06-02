package com.example.firebasedemo.Singleton;

public class CurrentUserSingleton {
    private static CurrentUserSingleton instance;
    private String currentUserId;

    private CurrentUserSingleton() {
        // Private constructor to prevent instantiation
    }

    public static CurrentUserSingleton getInstance() {
        if (instance == null) {
            instance = new CurrentUserSingleton();
        }
        return instance;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
}
