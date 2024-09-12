package com.aestheticsclub.services;
import com.aestheticsclub.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Activity {
    private static final Scanner scanner = new Scanner(System.in);
    //View All Activities
    public static void viewAllActivities() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Activity");
            ResultSet rs = ps.executeQuery();

            System.out.println("Activities List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("Id"));
                System.out.println("Facility ID: " + rs.getInt("FacilityId"));
                System.out.println("Name: " + rs.getString("Name"));
                System.out.println("Description: " + rs.getString("Description"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching activities: " + e.getMessage());
        }
    }


    // Displays the activity menu and handles user input
    public static void manageActivities() {
        System.out.println("1. Create Activity");
        System.out.println("2. Update Activity");
        System.out.println("3. Delete Activity");
        System.out.println("4. Return");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> createActivity();
            case 2 -> updateActivity();
            case 3 -> deleteActivity();
            case 4 -> {
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    //Create Activity
    private static void createActivity() {
        System.out.println("Enter Facility ID: ");
        int facilityId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Activity Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Activity Description: ");
        String description = scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Activity (FacilityId, Name, Description) VALUES (?, ?, ?)");
            ps.setInt(1, facilityId);
            ps.setString(2, name);
            ps.setString(3, description);
            ps.executeUpdate();
            System.out.println("Activity created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating activity: " + e.getMessage());
        }
    }
    //Update Activity
    private static void updateActivity() {
        System.out.println("Enter Activity ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter New Name: ");
        String newName = scanner.nextLine();
        System.out.println("Enter New Description: ");
        String newDescription = scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection
                    .prepareStatement("UPDATE Activity SET Name = ?, Description = ? WHERE Id = ?");
            ps.setString(1, newName);
            ps.setString(2, newDescription);
            ps.setInt(3, id);
            ps.executeUpdate();
            System.out.println("Activity updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating activity: " + e.getMessage());
        }
    }
    //Delete Activity
    private static void deleteActivity() {
        System.out.println("Enter Activity ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Activity WHERE Id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Activity deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting activity: " + e.getMessage());
        }
    }

}