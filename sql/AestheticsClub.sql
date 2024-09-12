CREATE DATABASE IF NOT EXISTS AestheticsClub;

USE AestheticsClub;

CREATE TABLE IF NOT EXISTS Membership (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL 
);


CREATE TABLE IF NOT EXISTS Benefit (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS MembershipBenefit (
    MembershipId INT,
    BenefitId INT,
    PRIMARY KEY (MembershipId, BenefitId),
    FOREIGN KEY (MembershipId) REFERENCES Membership(Id),
    FOREIGN KEY (BenefitId) REFERENCES Benefit(Id)
);


CREATE TABLE IF NOT EXISTS Users (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    MembershipId INT,
    FOREIGN KEY (MembershipId) REFERENCES Membership(Id) ON DELETE SET NULL 
);

CREATE TABLE IF NOT EXISTS Facilities (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    Type VARCHAR(255) NOT NULL,
    MaxQuantity INT 
);

CREATE TABLE IF NOT EXISTS Activity (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    FacilityId INT,
    Name VARCHAR(255) NOT NULL,
    Description VARCHAR(255),
    FOREIGN KEY (FacilityId) REFERENCES Facilities(Id)
);

CREATE TABLE IF NOT EXISTS Bookings (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    UserId INT,
    FacilityId INT,
    ActivityId INT,
    BookingDate DATE,
    FOREIGN KEY (UserId) REFERENCES Users(Id),
    FOREIGN KEY (FacilityId) REFERENCES Facilities(Id),
    FOREIGN KEY (ActivityId) REFERENCES Activity(Id)
);





CREATE TABLE IF NOT EXISTS FacilityStatus (
    FacilityId INT,
    Date DATE,
    BookedQuantity INT DEFAULT 0,
    PRIMARY KEY (FacilityId, Date),
    FOREIGN KEY (FacilityId) REFERENCES Facilities(Id)
);


CREATE TABLE IF NOT EXISTS Admin (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    Email VARCHAR(255) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS EventsAndWebinars (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    Description TEXT,
    Date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS MembershipPlans (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    Name VARCHAR(255) NOT NULL,
    Benefits TEXT
);

INSERT INTO Membership (Name) VALUES ('Premium'), ('Standard'), ('Basic');
INSERT INTO Benefit (Name) VALUES ('Access to all facilities'), ('Free personal training session'), ('Discount on events'), ('Priority booking'), ('Monthly newsletter');

-- Associating benefits with Membership plans
-- Premium Membership: All benefits
INSERT INTO MembershipBenefit (MembershipId, BenefitId) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);

-- Standard Membership: Some benefits
INSERT INTO MembershipBenefit (MembershipId, BenefitId) VALUES
(2, 1), (2, 3), (2, 4);

-- Basic Membership: Limited benefits
INSERT INTO MembershipBenefit (MembershipId, BenefitId) VALUES
(3, 1), (3, 3);




SELECT * FROM MembershipPlans;
SELECT * FROM EventsAndWebinars;
SELECT * FROM Bookings;
SELECT * FROM Membership;
SELECT * FROM Users;
SELECT * FROM Admin;
SELECT * FROM Facilities;
SELECT * FROM Activity;
SELECT * FROM benefit;
SELECT * FROM facilitystatus;
SELECT * FROM Membershipbenefit;




