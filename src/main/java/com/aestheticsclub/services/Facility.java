package com.aestheticsclub.services;

import com.aestheticsclub.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Facility {
    private static final Scanner scanner = new Scanner(System.in);
    //View All Facilities
    public static void viewAllFacilities() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Facilities");
            ResultSet rs = ps.executeQuery();

            System.out.println("Facilities List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("Id"));
                System.out.println("Name: " + rs.getString("Name"));
                System.out.println("Type: " + rs.getString("Type"));
                System.out.println("Max Quantity: " + rs.getInt("MaxQuantity"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching facilities: " + e.getMessage());
        }
    }
    //Manage Facilities
    public static void manageFacilities() {
        System.out.println("1. Create Facility");
        System.out.println("2. Update Facility");
        System.out.println("3. Delete Facility");
        System.out.println("4. Return");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> createFacility();
            case 2 -> updateFacility();
            case 3 -> deleteFacility();
            case 4 -> {
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    //Create Facility
    private static void createFacility() {
        System.out.println("Enter Facility Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Facility Type: ");
        String type = scanner.nextLine();
        System.out.println("Enter Max Quantity: ");
        int maxQuantity = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO Facilities (Name, Type, MaxQuantity) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, type);
            ps.setInt(3, maxQuantity);
            ps.executeUpdate();
            System.out.println("Facility created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating facility: " + e.getMessage());
        }
    }
    //Update Facility
    private static void updateFacility() {
        System.out.println("Enter Facility ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter New Name: ");
        String newName = scanner.nextLine();
        System.out.println("Enter New Type: ");
        String newType = scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("UPDATE Facilities SET Name = ?, Type = ? WHERE Id = ?");
            ps.setString(1, newName);
            ps.setString(2, newType);
            ps.setInt(3, id);
            ps.executeUpdate();
            System.out.println("Facility updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating facility: " + e.getMessage());
        }
    }
    //Delete Facility
    private static void deleteFacility() {
        System.out.println("Enter Facility ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Facilities WHERE Id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Facility deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting facility: " + e.getMessage());
        }
    }
    //Check Availability
    public static void checkAvailability() {
        System.out.println("Enter Facility ID: ");
        int facilityId = scanner.nextInt();
        System.out.println("Enter Date (YYYY-MM-DD): ");
        String date = scanner.next();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection
                    .prepareStatement("SELECT * FROM FacilityStatus WHERE FacilityId = ? AND Date = ?");
            ps.setInt(1, facilityId);
            ps.setDate(2, Date.valueOf(date));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int bookedQuantity = rs.getInt("BookedQuantity");
                System.out.println("Facility is available with " + bookedQuantity + " bookings.");
            } else {
                System.out.println("Facility is available with 0 bookings.");
            }
        } catch (SQLException e) {
            System.out.println("Error checking availability: " + e.getMessage());
        }
    }

}