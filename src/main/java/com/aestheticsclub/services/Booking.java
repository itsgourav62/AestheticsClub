package com.aestheticsclub.services;

import com.aestheticsclub.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Booking {
    private static final Scanner scanner = new Scanner(System.in);
    //View All Bookings
    public static void viewAllBookings() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Bookings");
            ResultSet rs = ps.executeQuery();

            System.out.println("Bookings List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("Id"));
                System.out.println("User ID: " + rs.getInt("UserId"));
                System.out.println("Facility ID: " + rs.getInt("FacilityId"));
                System.out.println("Activity ID: " + rs.getInt("ActivityId"));
                System.out.println("Event ID: " + rs.getInt("EventId"));
                System.out.println("Booking Date: " + rs.getDate("BookingDate"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching bookings: " + e.getMessage());
        }
    }
    //Manage Bookings
    public static void manageBookings() {
        System.out.println("1. Create Booking");
        System.out.println("2. Update Booking");
        System.out.println("3. Delete Booking");
        System.out.println("4. Return");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> createBooking();
            case 2 -> updateBooking();
            case 3 -> deleteBooking();
            case 4 -> {
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    //Create Booking
    private static void createBooking() {
        System.out.println("Enter User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Facility ID (or 0 if none): ");
        int facilityId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Activity ID (or 0 if none): ");
        int activityId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter Event/Webinar ID (or 0 if none): ");
        int eventId = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Bookings (UserId, FacilityId, ActivityId, EventId) VALUES (?, ?, ?, ?)");
            ps.setInt(1, userId);
            ps.setInt(2, facilityId);
            ps.setInt(3, activityId);
            ps.setInt(4, eventId);
            ps.executeUpdate();
            System.out.println("Booking created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating booking: " + e.getMessage());
        }
    }
    //Update Booking
    private static void updateBooking() {
        System.out.println("Enter Booking ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter New User ID: ");
        int newUserId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter New Event/Webinar ID: ");
        int newEventId = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection
                    .prepareStatement("UPDATE Bookings SET UserId = ?, EventId = ? WHERE Id = ?");
            ps.setInt(1, newUserId);
            ps.setInt(2, newEventId);
            ps.setInt(3, id);
            ps.executeUpdate();
            System.out.println("Booking updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating booking: " + e.getMessage());
        }
    }
    //Delete Booking    
    private static void deleteBooking() {
        System.out.println("Enter Booking ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Bookings WHERE Id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Booking deleted successfully!");
        } catch (SQLException e) {
            System.out.println("Error deleting booking: " + e.getMessage());
        }
    }
    //Book Facility
    public static void bookFacility(int userId) {
        System.out.println("Enter Facility ID: ");
        int facilityId = scanner.nextInt();
        System.out.println("Enter Activity ID: ");
        int activityId = scanner.nextInt();
        System.out.println("Enter Booking Date (YYYY-MM-DD): ");
        String bookingDate = scanner.next();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement psCheckActivity = connection.prepareStatement("SELECT * FROM Activity WHERE Id = ?");
            psCheckActivity.setInt(1, activityId);
            ResultSet rs = psCheckActivity.executeQuery();

            if (!rs.next()) {
                System.out.println("Error: Activity ID does not exist.");
                return;
            }

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Bookings (UserId, FacilityId, ActivityId, BookingDate) VALUES (?, ?, ?, ?)");
            ps.setInt(1, userId);
            ps.setInt(2, facilityId);
            ps.setInt(3, activityId);
            ps.setDate(4, Date.valueOf(bookingDate));
            ps.executeUpdate();

            PreparedStatement psStatus = connection.prepareStatement(
                    "INSERT INTO FacilityStatus (FacilityId, Date, BookedQuantity) VALUES (?, ?, 1) " +
                            "ON DUPLICATE KEY UPDATE BookedQuantity = BookedQuantity + 1");
            psStatus.setInt(1, facilityId);
            psStatus.setDate(2, Date.valueOf(bookingDate));
            psStatus.executeUpdate();

            System.out.println("Facility booked successfully!");
        } catch (SQLException e) {
            System.out.println("Error during booking: " + e.getMessage());
        }
    }
    //View User Bookings
    public static void viewUserBookings(int userId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT b.BookingDate, f.Name AS FacilityName, a.Name AS ActivityName " +
                            "FROM Bookings b " +
                            "JOIN Facilities f ON b.FacilityId = f.Id " +
                            "JOIN Activity a ON b.ActivityId = a.Id " +
                            "WHERE b.UserId = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            System.out.println("Your Bookings:");
            while (rs.next()) {
                System.out.println("Date: " + rs.getDate("BookingDate"));
                System.out.println("Facility: " + rs.getString("FacilityName"));
                System.out.println("Activity: " + rs.getString("ActivityName"));
                System.out.println("-------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching bookings: " + e.getMessage());
        }
    }
}