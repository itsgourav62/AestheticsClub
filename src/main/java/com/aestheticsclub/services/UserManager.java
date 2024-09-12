package com.aestheticsclub.services;

import com.aestheticsclub.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserManager {
    private static final Scanner scanner = new Scanner(System.in);
    //View All Users
    public static void viewAllUsers() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Users");
            ResultSet rs = ps.executeQuery();

            System.out.println("Users List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("Id"));
                System.out.println("Name: " + rs.getString("Name"));
                System.out.println("Email: " + rs.getString("Email"));
                System.out.println("Password: " + rs.getString("Password"));
                System.out.println("Membership ID: " + rs.getInt("MembershipId"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }
    //Manage Users
    public static void manageUsers() {
        System.out.println("1. Create User");
        System.out.println("2. Update User");
        System.out.println("3. Delete User");
        System.out.println("4. Return");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> createUser();
            case 2 -> updateUser();
            case 3 -> deleteUser();
            case 4 -> {
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    //Create User
    private static void createUser() {
        System.out.println("Enter User Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter User Email: ");
        String email = scanner.nextLine();
        System.out.println("Enter User Password: ");
        String password = scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO Users (Name, Email, Password) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.executeUpdate();
            System.out.println("User created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }
    //Update User
    private static void updateUser() {
        System.out.println("Enter User ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter New Name: ");
        String newName = scanner.nextLine();
        System.out.println("Enter New Email: ");
        String newEmail = scanner.nextLine();
        System.out.println("Enter New Password: ");
        String newPassword = scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection
                    .prepareStatement("UPDATE Users SET Name = ?, Email = ?, Password = ? WHERE Id = ?");
            ps.setString(1, newName);
            ps.setString(2, newEmail);
            ps.setString(3, newPassword);
            ps.setInt(4, id);
            ps.executeUpdate();
            System.out.println("User updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }
    //Delete User
    private static void deleteUser() {
        System.out.println("Enter User ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Users WHERE Id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("User deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

}
