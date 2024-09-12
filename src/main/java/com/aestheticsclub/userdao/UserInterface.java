package com.aestheticsclub.userdao;
public interface UserInterface {
    int login(String email, String password);
    Boolean register();
    void showUserMenu();
}
