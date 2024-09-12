package com.aestheticsclub.admindao;

import com.aestheticsclub.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import com.aestheticsclub.services.Activity;
import com.aestheticsclub.services.Booking;
import com.aestheticsclub.services.Event;
import com.aestheticsclub.services.Facility;
import com.aestheticsclub.services.UserManager;

public class Admin implements AdminInterface {
    private static final Scanner scanner = new Scanner(System.in);

    // Displays the admin menu and handles user input
    @Override
    public void showAdminMenu() {
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Return");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> loginController();
                case 2 -> register();
                case 3 -> {
                    System.out.println("-------------");
                    return;
                }
                default ->
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void loginController() {
        System.out.println("Enter Email: ");
        String email = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();
        Boolean status = login(email, password);
        if (status) {
            System.out.println("Admin login successful!");
            adminLoggedInMenu();
        } else
            System.out.println("Invalid email or password.");
    }

    // Handles Admin Login
    @Override
    public Boolean login(String email, String password) {

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Admin WHERE Email = ? AND Password = ?");
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error during admin login: " + e.getMessage());
        }
        return true;
    }

    // Handles Admin Registration
    @Override
    public void register() {
        System.out.println("Enter Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Email: ");
        String email = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO Admin (Name, Email, Password) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            System.out.println("Admin registered successfully!");
        } catch (SQLException e) {
            System.out.println("Error during admin registration: " + e.getMessage());
        }
    }

    // Displays the admin logged in menu and handles user input
    private static void adminLoggedInMenu() {
        while (true) {
            System.out.println("1. Manage Facilities");
            System.out.println("2. Manage Activities");
            System.out.println("3. Manage Users");
            System.out.println("4. Manage Events and Webinars");
            System.out.println("5. Manage Bookings");
            System.out.println("6. View All Users");
            System.out.println("7. View All Facilities");
            System.out.println("8. View All Activities");
            System.out.println("9. View All Events and Webinars");
            System.out.println("10. View All Bookings");
            System.out.println("11. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> Facility.manageFacilities();
                case 2 -> Activity.manageActivities();
                case 3 -> UserManager.manageUsers();
                case 4 -> Event.manageEventsAndWebinars();
                case 5 -> Booking.manageBookings();
                case 6 -> UserManager.viewAllUsers();
                case 7 -> Facility.viewAllFacilities();
                case 8 -> Activity.viewAllActivities();
                case 9 -> Event.viewAllEventsAndWebinars();
                case 10 -> Booking.viewAllBookings();
                case 11 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
