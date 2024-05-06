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

--insert valuues to cabincrew
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
