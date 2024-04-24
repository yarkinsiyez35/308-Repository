CREATE database FlightSchedulerDB;
use FlightSchedulerDB; 

CREATE TABLE airports (
    airport_code VARCHAR(3) PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    airport_name VARCHAR(255) NOT NULL
);

CREATE TABLE admins (
    admin_id INT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE vehicle_types (
    vehicle_type VARCHAR(255) PRIMARY KEY,
    business_capacity INT NOT NULL,
    economy_capacity INT NOT NULL,
    senior_pilot_capacity INT NOT NULL,
    junior_pilot_capacity INT NOT NULL,
    trainee_pilot_capacity INT NOT NULL,
    senior_attendee_capacity INT NOT NULL,
    junior_attendee_capacity INT NOT NULL,
    chef_attendee_capacity INT NOT NULL,
    seating_plan TEXT NOT NULL
);

CREATE TABLE planes (
    plane_id INT PRIMARY KEY, 
    vehicle_type VARCHAR(255) NOT NULL,
    FOREIGN KEY (vehicle_type) REFERENCES vehicle_types (vehicle_type)
   );

CREATE TABLE companies (
    company_name VARCHAR(255) PRIMARY KEY,
    additional_info TEXT
);

CREATE TABLE flights (
    flight_number VARCHAR(6) PRIMARY KEY,
    flight_info VARCHAR(255) NOT NULL,
    source_airport_code VARCHAR(3) NOT NULL,
    destination_airport_code VARCHAR(3) NOT NULL,
    plane_id INT NOT NULL,
    flight_range INT, 
    departure_datetime DATETIME NOT NULL,
    landing_datetime DATETIME NOT NULL,
    shared_flight BOOLEAN NOT NULL,
    shared_flight_company VARCHAR(255),
    admin_id INT NOT NULL,
    standard_menu VARCHAR(255),
    FOREIGN KEY (source_airport_code) REFERENCES airports(airport_code),
    FOREIGN KEY (destination_airport_code) REFERENCES airports(airport_code),
    FOREIGN KEY (plane_id) REFERENCES planes(plane_id),
    FOREIGN KEY (shared_flight_company) REFERENCES companies(company_name),
    FOREIGN KEY (admin_id) REFERENCES admins (admin_id)
);

CREATE TABLE pilots (
    pilot_id INT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,  
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(8) NOT NULL,
    allowed_range INT NOT NULL,
    nationality VARCHAR(255) NOT NULL,
    seniority VARCHAR(255) NOT NULL
);

CREATE TABLE pilot_languages (
    pilot_id INT NOT NULL,
    language VARCHAR(255) NOT NULL,
    PRIMARY KEY (pilot_id, language),
    FOREIGN KEY (pilot_id) REFERENCES pilots(pilot_id)
);

CREATE TABLE pilot_assignments
(
  	pilot_id INT NOT NULL,
  	flight_number VARCHAR(6) NOT NULL,
	assignment_role VARCHAR(8) NOT NULL,
	FOREIGN KEY (pilot_id) REFERENCES pilots(pilot_id),
	FOREIGN KEY (flight_number) REFERENCES flights (flight_number),
	PRIMARY KEY (pilot_id, flight_number)
);

CREATE TABLE crew_members (
    seniority VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL, 
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(8) NOT NULL,
    nationality VARCHAR(255) NOT NULL,
    attendant_id INT PRIMARY KEY
);

CREATE TABLE cabin_crew_assignments (
    flight_number VARCHAR(6) NOT NULL,
    attendant_id INT NOT NULL,
    assignment_role VARCHAR(7) NOT NULL,
    seat_number VARCHAR(255) NOT NULL, 
    FOREIGN KEY (flight_number) REFERENCES flights(flight_number),
    FOREIGN KEY (attendant_id) REFERENCES crew_members(attendant_id),
    PRIMARY KEY (attendant_id, flight_number)
);

CREATE TABLE attendant_vehicle_types (
    attendant_id INT NOT NULL,
    vehicle_type VARCHAR(255) NOT NULL,
    PRIMARY KEY (attendant_id, vehicle_type),
    FOREIGN KEY (attendant_id) REFERENCES crew_members(attendant_id),
    FOREIGN KEY (vehicle_type) REFERENCES vehicle_types(vehicle_type)
);

CREATE TABLE dish_recipes (
    attendant_id INT,
    recipe VARCHAR(255),
    FOREIGN KEY (attendant_id) REFERENCES crew_members(attendant_id),
    PRIMARY KEY (attendant_id, recipe)
);

CREATE TABLE attendant_languages (
    attendant_id INT NOT NULL,
    language VARCHAR(255) NOT NULL,
    PRIMARY KEY (attendant_id, language),
    FOREIGN KEY (attendant_id) REFERENCES crew_members(attendant_id)
);

CREATE TABLE passengers(
    passenger_id INT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(8) NOT NULL,
    nationality VARCHAR(50) NOT NULL
);
   
CREATE TABLE passenger_flights( 
    passenger_id INT NOT NULL,
    booking_id VARCHAR(255) UNIQUE NOT NULL,
    flight_number VARCHAR(6) NOT NULL,
    is_parent VARCHAR(1) NOT NULL,
    seat_number VARCHAR(255) NOT NULL,
    FOREIGN KEY (passenger_id) REFERENCES passengers(passenger_id),
    FOREIGN KEY (flight_number) REFERENCES flights(flight_number),
    PRIMARY KEY (passenger_id, flight_number)
);


