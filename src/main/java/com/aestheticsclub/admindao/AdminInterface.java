package com.aestheticsclub.admindao;

public interface AdminInterface {
    
    Boolean login(String email, String password);
    void register();
    void showAdminMenu();
}