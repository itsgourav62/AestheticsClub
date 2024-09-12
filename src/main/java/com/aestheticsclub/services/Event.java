package com.aestheticsclub.services;

import com.aestheticsclub.connection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Event {
    private static final Scanner scanner = new Scanner(System.in);
    //View All Events and Webinars
    public static void viewAllEventsAndWebinars() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM EventsAndWebinars");
            ResultSet rs = ps.executeQuery();

            System.out.println("Events and Webinars List:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("Id"));
                System.out.println("Title: " + rs.getString("Name"));
                System.out.println("Description: " + rs.getString("Description"));
                System.out.println("Date: " + rs.getDate("Date"));
                System.out.println("---------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching events and webinars: " + e.getMessage());
        }
    }

    //Manage Events and Webinars
    public static void manageEventsAndWebinars() {
        System.out.println("1. Create Event/Webinar");
        System.out.println("2. Update Event/Webinar");
        System.out.println("3. Cancel Event/Webinar");
        System.out.println("4. Return");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> createEventOrWebinar();
            case 2 -> updateEventOrWebinar();
            case 3 -> cancelEventOrWebinar();
            case 4 -> {
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    //Create Event or Webinar
    private static void createEventOrWebinar() {
        System.out.println("Enter Event/Webinar Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Description: ");
        String description = scanner.nextLine();
        System.out.println("Enter Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO EventsAndWebinars (Name, Description, Date) VALUES (?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, date);
            ps.executeUpdate();
            System.out.println("Event/Webinar created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating event/webinar: " + e.getMessage());
        }
    }
    //Update Event or Webinar
    private static void updateEventOrWebinar() {
        System.out.println("Enter Event/Webinar ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter New Name: ");
        String newName = scanner.nextLine();
        System.out.println("Enter New Description: ");
        String newDescription = scanner.nextLine();
        System.out.println("Enter New Date (YYYY-MM-DD): ");
        String newDate = scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection
                    .prepareStatement("UPDATE EventsAndWebinars SET Name = ?, Description = ?, Date = ? WHERE Id = ?");
            ps.setString(1, newName);
            ps.setString(2, newDescription);
            ps.setString(3, newDate);
            ps.setInt(4, id);
            ps.executeUpdate();
            System.out.println("Event/Webinar updated successfully!");
        } catch (SQLException e) {
            System.out.println("Error updating event/webinar: " + e.getMessage());
        }
    }
    //Cancel Event or Webinar
    private static void cancelEventOrWebinar() {
        System.out.println("Enter Event/Webinar ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM EventsAndWebinars WHERE Id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Event/Webinar canceled successfully!");
        } catch (SQLException e) {
            System.out.println("Error canceling event/webinar: " + e.getMessage());
        }
    }
}
