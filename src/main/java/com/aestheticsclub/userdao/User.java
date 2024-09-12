package com.aestheticsclub.userdao;

import com.aestheticsclub.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.aestheticsclub.services.Activity;
import com.aestheticsclub.services.Booking;
import com.aestheticsclub.services.Facility;

//Handles User Login and Registration
public class User implements UserInterface {

    private static final Scanner scanner = new Scanner(System.in);

    // Displays the user menu and handles user input
    @Override
    public void showUserMenu() {
        User user = new User();

        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Return to Main Menu");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid choice. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    loginController();
                }
                case 2 -> {
                    Boolean status = user.register();
                    if (status)
                        System.out.println("User registered successfully!");

                    else
                        System.out.println("Invalid email or password.");

                }
                case 3 -> {
                    System.out.println("Returning to the main menu...");
                    System.out.println("-------------");
                    return;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private void loginController() {
        User user = new User();

        System.out.println("Enter Email: ");
        String email = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();
        int userId = user.login(email, password);
        if (userId > 0) {
            System.out.println("Login successful!");
            try {
                Thread userSessionThread = new Thread(() -> {
                    try {
                        userLoggedInMenu(userId);
                    } catch (Exception e) {
                        System.out.println("An error occurred in user session: " + e.getMessage());
                    }
                });
                userSessionThread.setUncaughtExceptionHandler((t, e) -> {
                    System.out.println("Uncaught exception in thread " + t.getName() + ": " + e.getMessage());
                });
                userSessionThread.start();
                userSessionThread.join(); // Wait for the user session thread to finish

            } catch (InterruptedException e) {
                System.out.println("An error occurred in user session: " + e.getMessage());
            }
        } else
            System.out.println("Invalid email or password.");
    }

    // Handles User Login
    @Override
    public int login(String email, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Users WHERE Email = ? AND Password = ?");
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("Id");

                return userId;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
        }
        return -1;
    }

    // Handles User Registration
    @Override
    public Boolean register() {
        System.out.println("Enter Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Email: ");
        String email = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        System.out.println("Select Membership Type:");
        System.out.println("1. Premium");
        System.out.println("2. Standard");
        System.out.println("3. Basic");
        int membershipChoice;
        try {
            membershipChoice = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Invalid choice. No membership assigned.");
            membershipChoice = 0;
        }

        int membershipId = switch (membershipChoice) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 3;
            default -> {
                System.out.println("Invalid choice. No membership assigned.");
                yield 0;
            }
        };

        if (membershipId != 0) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                PreparedStatement checkMembership = connection
                        .prepareStatement("SELECT Id FROM Membership WHERE Id = ?");
                checkMembership.setInt(1, membershipId);
                ResultSet rs = checkMembership.executeQuery();

                if (!rs.next()) {
                    System.out.println("Invalid Membership ID.");
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("Error checking Membership ID: " + e.getMessage());
                return false;
            }
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Users (Name, Email, Password, MembershipId) VALUES (?, ?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            if (membershipId == 0) {
                ps.setNull(4, java.sql.Types.INTEGER);
            } else {
                ps.setInt(4, membershipId);
            }
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error during registration: " + e.getMessage());
            return false;
        }
    }

    // Displays User Menu
    private void userLoggedInMenu(int userId) {
        while (true) {
            System.out.println("-------------");
            System.out.println("1. View Profile");
            System.out.println("2. Check Facilities");
            System.out.println("3. Check Activities");
            System.out.println("4. Check Availability");
            System.out.println("5. Book Facilities");
            System.out.println("6. View Bookings");
            System.out.println("7. Logout");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid choice. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> viewUserProfile(userId);
                case 2 -> Facility.viewAllFacilities();
                case 3 -> Activity.viewAllActivities();
                case 4 -> Facility.checkAvailability();
                case 5 -> Booking.bookFacility(userId);
                case 6 -> Booking.viewUserBookings(userId);
                case 7 -> {
                    System.out.println("Logged out.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
            System.out.println("-------------");
        }
    }

    // Displays User Profile
    private void viewUserProfile(int userId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Users WHERE Id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("User Profile:");
                System.out.println("ID: " + rs.getInt("Id"));
                System.out.println("Name: " + rs.getString("Name"));
                System.out.println("Email: " + rs.getString("Email"));

                int membershipId = rs.getInt("MembershipId");
                if (membershipId != 0) {
                    PreparedStatement psMembership = connection
                            .prepareStatement("SELECT Name FROM Membership WHERE Id = ?");
                    psMembership.setInt(1, membershipId);
                    ResultSet rsMembership = psMembership.executeQuery();

                    if (rsMembership.next()) {
                        System.out.println("Membership Plan: " + rsMembership.getString("Name"));
                        PreparedStatement psBenefits = connection.prepareStatement(
                                "SELECT b.Name FROM Benefit b " +
                                        "JOIN MembershipBenefit mb ON b.Id = mb.BenefitId " +
                                        "WHERE mb.MembershipId = ?");
                        psBenefits.setInt(1, membershipId);
                        ResultSet rsBenefits = psBenefits.executeQuery();

                        System.out.println("Benefits:");
                        while (rsBenefits.next()) {
                            System.out.println("- " + rsBenefits.getString("Name"));
                        }
                    } else {
                        System.out.println("Membership details not found.");
                    }
                } else {
                    System.out.println("No membership assigned.");
                }
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user profile: " + e.getMessage());
        }
    }

}
