package com.aestheticsclub.main;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.aestheticsclub.admindao.Admin;
import com.aestheticsclub.userdao.User;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Main menu
        while (true) {
            try {
                System.out.println("-------------");
                System.out.println("Welcome to Aesthetics Club!");
                System.out.println("1. User");
                System.out.println("2. Admin");
                System.out.println("3. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1 -> {
                        System.out.println("-------------");
                        User user = new User();
                        Thread userThread = new Thread(user::showUserMenu);
                        userThread.start();
                        userThread.join(); // Wait for the user thread to finish
                    }
                    case 2 -> {
                        System.out.println("-------------");
                        Admin admin = new Admin();
                        admin.showAdminMenu();
                    }
                    case 3 -> {
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice, please try again.");
                }
                
                System.out.println("-------------");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }
}
