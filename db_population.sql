use FlightSchedulerDB;

-- insert values to pilots 
INSERT INTO pilots (pilot_id, email, password, first_name, surname, age, gender, allowed_range, nationality, seniority) VALUES 
	(1, "bob.plane@gmail.com", "password1234", "Bob", "Plane", 40, "male", 10000, "American", "senior"), 
    (2, "josh_flight@gmail.com", "pswrd4321", "Josh", "Flight", 30, "male", 5000, "British", "junior"), 
    (3, "jane_craft@gmail.com", "pswrdpswrd", "Jane", "Craft", 30, "female", 5000, "American", "junior"),
    (4, "john_high@gmail.com", "pswpsw123", "John", "High", 35, "male", 5000, "British", "junior"),
    (5, "mary_wings@gmail.com", "mrywngs", "Mary", "Wings", 25, "female", 2500, "American", "attendee");
    
    
-- insert values to pilot_languages 
INSERT INTO pilot_languages (pilot_id, language) VALUES
	(1,"english"),
    (1, "german"),
    (2, "english"),
    (2, "french"),
    (3, "english"),
    (4, "english"),
    (4, "turkish"),
    (4, "german"),
    (5, "english");

-- insert values to passengers
INSERT INTO passengers(passenger_id, first_name, surname, email, password, age, gender, nationality) VALUES 
	(1, "John", "Doe", "john.doe@gmail.com", "password1", 40, "male", "American"), 
    (2, "Jane", "Done", "jane.done@gmail.com", "password2", 35, "female", "American"), 
    (3, "Jason", "Martin", "jason.martin@gmail.com", "password3", 30, "male", "Portuguese"),
    (4, "Ricardo", "James", "ricardo.james@gmail.com", "password4", 25, "male", "Spanish"),
    (5, "Camilla", "Caso", "camilla.caso@gmail.com", "password5", 20, "female", "American");

-- insert values to cabincrew
INSERT INTO crew_members (attendant_id, email, password, first_name, surname, age, gender, nationality, seniority)
VALUES
(1, 'john.cena@gmail.com', 'password325', 'John', 'Cena', 32, 'Male', 'USA', 'Senior'),
(2, 'jane.brand@gmail.com', 'password564', 'Jane', 'Brand', 28, 'Female', 'Canada', 'Junior'),
(3, 'alice.punk@gmail.com', 'password869', 'Alice', 'Punk', 29, 'Female', 'UK', 'Chef'),
(4, 'Brad.brown@gmail.com', 'password7455', 'Brad', 'Brown', 35, 'Male', 'Australia', 'Senior'),
(5, 'carol.pitt@gmail.com', 'password432', 'Carol', 'Pitt', 26, 'Female', 'New Zealand', 'Junior');

INSERT INTO attendant_languages (attendant_id, language) VALUES
	(1,"english"),
    (1, "german"),
    (2, "english"),
    (2, "french"),
    (3, "english"),
    (4, "english"),
    (4, "turkish"),
    (4, "german"),
    (5, "english");

-- insert values to admins
INSERT INTO admins (admin_id, first_name, surname, email, password) VALUES 
(1, 'John', 'Doe', 'john.doe@example.com', 'password123'),
(2, 'Jane', 'Smith', 'jane.smith@example.com', 'password123'),
(3, 'Alice', 'Johnson', 'alice.johnson@example.com', 'password123'),
(4, 'Bob', 'Brown', 'bob.brown@example.com', 'password123'),
(5, 'Charlie', 'Davis', 'charlie.davis@example.com', 'password123');

-- insert values to airports
INSERT INTO airports (airport_code, city, country, airport_name) VALUES
('LAX', 'Los Angeles', 'USA', 'Los Angeles International Airport'),
('JFK', 'New York', 'USA', 'John F. Kennedy International Airport'),
('SFO', 'San Francisco', 'USA', 'San Francisco International Airport'),
('ORD', 'Chicago', 'USA', 'O\'Hare International Airport'),
('MIA', 'Miami', 'USA', 'Miami International Airport'),
('ATL', 'Atlanta', 'USA', 'Hartsfield-Jackson Atlanta International Airport'),
('SEA', 'Seattle', 'USA', 'Seattle-Tacoma International Airport'),
('DEN', 'Denver', 'USA', 'Denver International Airport'),
('BOS', 'Boston', 'USA', 'Logan International Airport'),
('MCO', 'Orlando', 'USA', 'Orlando International Airport');

-- insert values to vehicle_types
INSERT INTO vehicle_types (
    vehicle_type, 
    business_capacity, 
    economy_capacity, 
    senior_pilot_capacity, 
    junior_pilot_capacity, 
    trainee_pilot_capacity, 
    senior_attendee_capacity, 
    junior_attendee_capacity, 
    chef_attendee_capacity, 
    seating_plan
) VALUES
(
    'Boeing 747', 50, 350, 4, 4, 2, 10, 20, 5, 'INSERT JSON OR SEATING ARRANGEMENT HERE'
),
(
    'Airbus A320', 20, 120, 2, 2, 1, 5, 10, 3, 'INSERT JSON OR SEATING ARRANGEMENT HERE'
),
(
    'Boeing 737', 30, 150, 2, 2, 1, 5, 10, 3, 'INSERT JSON OR SEATING ARRANGEMENT HERE'
),
(
    'Airbus A380', 75, 500, 6, 6, 3, 15, 30, 7, 'INSERT JSON OR SEATING ARRANGEMENT HERE'
),
(
    'Boeing 787', 40, 200, 3, 3, 2, 8, 15, 4, 'INSERT JSON OR SEATING ARRANGEMENT HERE'
);

-- insert values to planes
INSERT INTO planes (plane_id, vehicle_type) VALUES
(101, 'Boeing 747'),
(102, 'Airbus A320'),
(103, 'Boeing 737'),
(104, 'Airbus A380'),
(105, 'Boeing 787');

-- insert values to companies
INSERT INTO companies (company_name, additional_info) VALUES
('Delta', 'Delta Air Lines, Inc. is a major American airline with its headquarters in Atlanta, Georgia.'),
('United', 'United Airlines Holdings, Inc. is a major American airline headquartered at Willis Tower in Chicago, Illinois.');

-- insert values to flights
INSERT INTO flights (
    flight_number, flight_info, source_airport_code, destination_airport_code, 
    plane_id, flight_range, departure_datetime, landing_datetime, 
    shared_flight, shared_flight_company, admin_id, standard_menu
) VALUES 
(
    'FL1234', 'Non-stop flight from LAX to JFK', 'LAX', 'JFK', 
    101, 2475, '2024-06-01 08:00:00', '2024-06-01 16:00:00', 
    FALSE, NULL, 1, 'Standard Menu A'
),
(
    'FL5678', 'Direct flight from SFO to ORD', 'SFO', 'ORD', 
    102, 1846, '2024-06-02 09:00:00', '2024-06-02 15:00:00', 
    TRUE, 'Delta', 2, 'Standard Menu B'
),
(
    'FL9101', 'Connecting flight from MIA to ATL via DFW', 'MIA', 'ATL', 
    103, 604, '2024-06-03 10:00:00', '2024-06-03 12:00:00', 
    FALSE, NULL, 3, 'Standard Menu C'
),
(
    'FL1121', 'Non-stop flight from SEA to DEN', 'SEA', 'DEN', 
    104, 1020, '2024-06-04 11:00:00', '2024-06-04 14:00:00', 
    TRUE, 'United', 4, 'Standard Menu D'
),
(
    'FL3141', 'Direct flight from BOS to MCO', 'BOS', 'MCO', 
    105, 1121, '2024-06-05 12:00:00', '2024-06-05 15:00:00', 
    FALSE, NULL, 5, 'Standard Menu E'
);
