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