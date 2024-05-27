package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.FlightDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTO;
import com.su.FlightScheduler.DTO.FrontEndDTOs.UserDataDTOFactory;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Entity.FlightEntitites.*;
import com.su.FlightScheduler.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.su.FlightScheduler.DTO.SeatDTOs.SeatingTypeDTO;

//TESTING: this service should be tested
@Service
@Transactional
public class FlightServiceImp implements FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final PlaneRepository planeRepository;
    private final CompanyRepository companyRepository;

    private final VehicleTypeRepository vehicleTypeRepository;

    private final PassengerFlightRepository passengerFlightRepository;
    private final AdminRepository adminRepository;

    @Autowired
    public FlightServiceImp(FlightRepository flightRepository, AirportRepository airportRepository,
                            PlaneRepository planeRepository, CompanyRepository companyRepository,
                            VehicleTypeRepository vehicleTypeRepository,
                            PassengerFlightRepository passengerFlightRepository, AdminRepository adminRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
        this.planeRepository = planeRepository;
        this.companyRepository = companyRepository;
        this.vehicleTypeRepository = vehicleTypeRepository;
        this.passengerFlightRepository = passengerFlightRepository;
        this.adminRepository = adminRepository;
    }


    // This method is used to get the FlightEntity object from the database
    // If the object is not found, it will throw an EntityNotFoundException
    @Override
    public FlightEntity getFlightOrThrow(String flightId) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isEmpty()) {
            throw new EntityNotFoundException("Flight not found");
        }
        return optionalFlight.get();
    }

    public String crateFlightNumber()
    {
        String companyLetters = "CS";
        String flightNumber = "";
        flightNumber += companyLetters;
        int flightCode;
        for(int i = 0; i < 4; i++){
            flightCode = (int)(Math.random() * 10);
            flightNumber += Integer.toString(flightCode);
        }
        return flightNumber;
    }

    public int getRange(CityEntity sourceCity, CityEntity destinationEntity){
        final int EARTH_RADIUS = 6371;

        double sourceLatitude = Math.toRadians(sourceCity.getLatitude());
        double sourceLongitude = Math.toRadians(sourceCity.getLongitude());
        double destinationLatitude = Math.toRadians(destinationEntity.getLatitude());
        double destinationLongitude = Math.toRadians(destinationEntity.getLongitude());

        // The following calculation is based on the Haversine formula
        double latDistance = destinationLatitude - sourceLatitude;
        double lonDistance = destinationLongitude - sourceLongitude;

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(sourceLatitude) * Math.cos(destinationLatitude)
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c; // convert to kilometers

        return (int) distance;
}




    @Override
    public FlightEntity createFlight(FlightDataDTO flightDataDTO, int adminId) {
        FlightEntity flightEntity = new FlightEntity();
        String flightNumberFromRequest = flightDataDTO.getFlightId();
        if(flightNumberFromRequest.equals("auto")){
            String flightNumberRandom = crateFlightNumber();
            while(flightRepository.findById(flightNumberRandom).isPresent()){
                flightNumberRandom = crateFlightNumber();
            }
            flightEntity.setFlightNumber(flightNumberRandom);
        }
        else if (flightRepository.findById(flightNumberFromRequest).isPresent())
        {
            throw new RuntimeException("Could not create flight, flight number already exists!");
        }
        else {
            flightEntity.setFlightNumber(flightNumberFromRequest);
        }

        Optional<AirportEntity> sourceAirport = airportRepository.findAirportEntityByAirportCode(flightDataDTO.getDepartureAirport());
        if(sourceAirport.isEmpty())
        {
            throw new RuntimeException("Could not create flight, airports are wrong!");
        }
        Optional<AirportEntity> landingAirport = airportRepository.findAirportEntityByAirportCode(flightDataDTO.getLandingAirport());
        if(landingAirport.isEmpty())
        {
            throw new RuntimeException("Could not create flight, airports are wrong!");
        }

        Optional<PlaneEntity> plane = planeRepository.findById(flightDataDTO.getPlaneId());
        if (plane.isEmpty())
        {
            throw new RuntimeException("Could not create flight, plane id does not exist!");
        }

        flightEntity.setFlightInfo("Regular flight"); // Might need to change this one. The info is a bit different in the document provided.
        flightEntity.setSourceAirport(sourceAirport.get());
        flightEntity.setDestinationAirport(landingAirport.get());
        flightEntity.setPlane(plane.get());


        final int allowedDistance = 1400; // This is the longest distance between two cities in Turkey, we do not allow distances longer than this.
        int distance = getRange(sourceAirport.get().getCity(), landingAirport.get().getCity());
        if(distance > allowedDistance){
            throw new RuntimeException("Could not create flight, the distance between the cities exceeds the allowed distance!");
        }
        flightEntity.setFlightRange(distance);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureDateTime = flightDataDTO.getDepartureTime();
        LocalDateTime landingDateTime = flightDataDTO.getLandingTime();

        if (departureDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Departure time cannot be in the past");
        }

        if (landingDateTime.isBefore(departureDateTime)) {
            throw new RuntimeException("Landing time cannot be before departure time");
        }

        flightEntity.setDepartureDateTime(departureDateTime);
        flightEntity.setLandingDateTime(landingDateTime);

        if (flightDataDTO.getAirlineCompany().equals("No Shared Flight"))
        {
            flightEntity.setSharedFlight(false);
            flightEntity.setSharedFlightCompany(null);
        }
        else
        {
            flightEntity.setSharedFlight(true);
            Optional<CompanyEntity> companyEntity = companyRepository.findById(flightDataDTO.getAirlineCompany());
            if (companyEntity.isPresent())
            {
                flightEntity.setSharedFlightCompany(companyEntity.get());
            }
            else
            {
                throw new RuntimeException("Could not create flight, the company does not exist!");
            }
        }

        Optional<AdminEntity> adminEntity = adminRepository.findById(adminId);
        if (adminEntity.isEmpty()) {
            throw new RuntimeException("Could not create flight, admin does not exist!");
        }
        flightEntity.setAdmin(adminEntity.get());
        flightEntity.setStandardMenu("Standard menu");
        return flightRepository.save(flightEntity);
    }


    // This has no usage, but it is here to show how to create a flight object
    @Override
    public FlightEntity saveFlightObj(FlightEntity flight) {
        return flightRepository.save(flight);
    }

    // This method is used for testing/creating a flight in the backend without the need for a front-end
    @Override
    public FlightEntity createFlightFilled(String flightNumber, String flightInfo, AirportEntity sourceAirport,
                                           AirportEntity destinationAirport, PlaneEntity plane,
                                           Integer flightRange, LocalDateTime departureDateTime,
                                           LocalDateTime landingDateTime, boolean sharedFlight,
                                           CompanyEntity sharedFlightCompany, AdminEntity admin,
                                           String standardMenu) {
        FlightEntity flight = new FlightEntity(flightNumber, flightInfo, sourceAirport, destinationAirport,
                                            plane, flightRange, departureDateTime, landingDateTime,
                                            sharedFlight, sharedFlightCompany,
                                             admin, standardMenu);
        return flightRepository.save(flight);
    }


    //------------------------------------------------------------------------------------------------------------


    // -- Find Methods --
    @Override
    public Optional<FlightEntity> findFlightByNumber(String flightNumber) {
        return flightRepository.findById(flightNumber);
    }

    @Override
    public FlightDataDTO findFlightByNumberDTO(String flightNumber) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        return new FlightDataDTO(flight);
    }

    @Override
    public List<FlightEntity> findAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public List<FlightDataDTO> findAllFlightsDTO() {
        List<FlightEntity> flights = flightRepository.findAll();
        List<FlightDataDTO> flightDataDTOS = new ArrayList<>();
        for (FlightEntity flight : flights) {
            flightDataDTOS.add(new FlightDataDTO(flight));
        }
        return flightDataDTOS;
    }


    // The following find methods only return DTO's of the flights, since these filtering will only be used in the front-end
    @Override
    public List<FlightDataDTO> findFlightsByDepartureAirport(String airportCode) {
        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCode(airportCode);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure airport");
        }

        List<FlightDataDTO> flightDataDTOS = new ArrayList<>();
        for (FlightEntity flight : flights) {
            flightDataDTOS.add(new FlightDataDTO(flight));
            }

        return flightDataDTOS;
    }

    @Override
    public List<FlightDataDTO> findFlightsByDestinationAirport(String airportCode) {
        List<FlightEntity> flights = flightRepository.findByDestinationAirportAirportCode(airportCode);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given destination airport");
        }

        List<FlightDataDTO> flightDataDTOS = new ArrayList<>();
        for (FlightEntity flight : flights) {
            flightDataDTOS.add(new FlightDataDTO(flight));
        }
        return flightDataDTOS;
    }

    @Override
    public List<FlightDataDTO> findFlightsByDepartureAndDestinationAirport(String departureAirportCode, String destinationAirportCode) {
        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCodeAndDestinationAirportAirportCode(departureAirportCode, destinationAirportCode);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure and destination airports");
        }
        List<FlightDataDTO> flightDataDTOS = new ArrayList<>();
        for (FlightEntity flight : flights) {
            flightDataDTOS.add(new FlightDataDTO(flight));
        }
        return flightDataDTOS;
    }

    @Override
    public List<FlightDataDTO> findFlightsByDepartureDateTime(LocalDateTime departureDateTime) {
        List<FlightEntity> flights = flightRepository.findByDepartureDateTime(departureDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure date and time");
        }
        List<FlightDataDTO> flightDataDTOS = new ArrayList<>();
        for (FlightEntity flight : flights) {
            flightDataDTOS.add(new FlightDataDTO(flight));
        }
        return flightDataDTOS;
    }

    @Override
    public List<FlightDataDTO> findFlightsByLandingDateTime(LocalDateTime landingDateTime) {
        List<FlightEntity> flights = flightRepository.findByLandingDateTime(landingDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given landing date and time");
        }
        List<FlightDataDTO> flightDataDTOS = new ArrayList<>();
        for (FlightEntity flight : flights) {
            flightDataDTOS.add(new FlightDataDTO(flight));
        }
        return flightDataDTOS;
    }

    @Override
    public List<FlightDataDTO> findFlightsByDepartureAndLandingDateTime(LocalDateTime departureDateTime, LocalDateTime landingDateTime) {
        List<FlightEntity> flights = flightRepository.findByDepartureDateTimeAndLandingDateTime(departureDateTime, landingDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure and landing date and time");
        }
        List<FlightDataDTO> flightDataDTOS = new ArrayList<>();
        for (FlightEntity flight : flights) {
            flightDataDTOS.add(new FlightDataDTO(flight));
        }
        return flightDataDTOS;
    }

    @Override
    public List<FlightDataDTO> findFlightsByDepartureAirportAndDepartureDateTime(String airportCode, LocalDateTime departureDateTime) {
        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCodeAndDepartureDateTime(airportCode, departureDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure airport and departure date and time");
        }
        List<FlightDataDTO> flightDataDTOS = new ArrayList<>();
        for (FlightEntity flight : flights) {
            flightDataDTOS.add(new FlightDataDTO(flight));
        }
        return flightDataDTOS;
    }

    @Override
    public List<FlightDataDTO> findFlightsByDestinationAirportAndLandingDateTime(String airportCode, LocalDateTime landingDateTime) {
        List<FlightEntity> flights = flightRepository.findByDestinationAirportAirportCodeAndLandingDateTime(airportCode, landingDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given destination airport and landing date and time");
        }
        List<FlightDataDTO> flightDataDTOS = new ArrayList<>();
        for (FlightEntity flight : flights) {
            flightDataDTOS.add(new FlightDataDTO(flight));
        }
        return flightDataDTOS;
    }

    @Override
    public List<FlightDataDTO> findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(String departureAirportCode, String destinationAirportCode, LocalDateTime departureDateTime, LocalDateTime landingDateTime) {
        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCodeAndDestinationAirportAirportCodeAndDepartureDateTimeAndLandingDateTime(departureAirportCode, destinationAirportCode, departureDateTime, landingDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure and destination airports and departure and landing date and time");
        }
        List<FlightDataDTO> flightDataDTOS = new ArrayList<>();
        for (FlightEntity flight : flights) {
            flightDataDTOS.add(new FlightDataDTO(flight));
        }
        return flightDataDTOS;
    }

    // End of Find Methods

    //------------------------------------------------------------------------------------------------------------

    // Delete Method
    @Override
    public void deleteFlightByNumber(String flightNumber) {
        getFlightOrThrow(flightNumber);
        flightRepository.deleteById(flightNumber);
    }

    @Override
    public FlightEntity updateFlight(FlightDataDTO flightDataDTO, int adminID) {
        String flightNumberFromRequest = flightDataDTO.getFlightId();

        if (!flightRepository.existsById(flightDataDTO.getFlightId()))
        {
            throw new RuntimeException("Could not update flight, id does not exist!");
        }
        Optional<AirportEntity> sourceAirport = airportRepository.findAirportEntityByAirportCode(flightDataDTO.getDepartureAirport());
        if(sourceAirport.isEmpty())
        {
            throw new RuntimeException("Could not update flight, airports are wrong!");
        }
        Optional<AirportEntity> landingAirport = airportRepository.findAirportEntityByAirportCode(flightDataDTO.getLandingAirport());
        if(landingAirport.isEmpty())
        {
            throw new RuntimeException("Could not update flight, airports are wrong!");
        }

        Optional<PlaneEntity> plane = planeRepository.findById(flightDataDTO.getPlaneId());
        if (plane.isEmpty())
        {
            throw new RuntimeException("Could not update flight, plane id does not exist!");
        }


        FlightEntity flightEntity = flightRepository.findById(flightDataDTO.getFlightId()).get();

        flightEntity.setFlightInfo("Regular flight"); // Might need to change this one. The info is a bit different in the document provided.
        flightEntity.setSourceAirport(sourceAirport.get());
        flightEntity.setDestinationAirport(landingAirport.get());


        final int allowedDistance = 1400; // This is the longest distance between two cities in Turkey, we do not allow distances longer than this.
        int distance = getRange(sourceAirport.get().getCity(), landingAirport.get().getCity());
        if(distance > allowedDistance){
            throw new RuntimeException("Could not update flight, the distance between the cities exceeds the allowed distance!");
        }
        flightEntity.setFlightRange(distance);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departureDateTime = flightDataDTO.getDepartureTime();
        LocalDateTime landingDateTime = flightDataDTO.getLandingTime();

        if (departureDateTime.isBefore(now)) {
            throw new RuntimeException("Departure time cannot be in the past");
        }

        if (landingDateTime.isBefore(departureDateTime)) {
            throw new RuntimeException("Landing time cannot be before departure time");
        }

        flightEntity.setDepartureDateTime(departureDateTime);
        flightEntity.setLandingDateTime(landingDateTime);

        if (flightDataDTO.getAirlineCompany().equals("No Shared Flight"))
        {
            flightEntity.setSharedFlight(false);
            flightEntity.setSharedFlightCompany(null);
        }
        else
        {
            flightEntity.setSharedFlight(true);
            Optional<CompanyEntity> companyEntity = companyRepository.findById(flightDataDTO.getAirlineCompany());
            if (companyEntity.isPresent())
            {
                flightEntity.setSharedFlightCompany(companyEntity.get());
            }
            else
            {
                throw new RuntimeException("Could not update flight, the company does not exist!");
            }
        }

        Optional<AdminEntity> adminEntity = adminRepository.findById(adminID);
        if (adminEntity.isEmpty()) {
            throw new RuntimeException("Could not update flight, admin does not exist!");
        }
        flightEntity.setAdmin(adminEntity.get());

        return flightRepository.save(flightEntity);


    }


    //------------------------------------------------------------------------------------------------------------

    // Update Methods
    @Override
    public FlightEntity updateFlightInfo(String flightNumber, String flightInfo) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setFlightInfo(flightInfo);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateFlightByFlightDTO(FlightDataDTO flight, int adminId) {
        FlightEntity existingFlight = getFlightOrThrow(flight.getFlightId());
        if (existingFlight.getAdmin().getAdminId() != adminId) {
            throw new RuntimeException("Admins do not match!");
        }

        // Update existing flight details instead of creating a new one
        existingFlight.setFlightInfo("Regular flight");
        existingFlight.setSourceAirport(airportRepository.findAirportEntityByAirportCode(flight.getDepartureAirport()).orElseThrow(() -> new RuntimeException("Could not create flight, airports are wrong!")));
        existingFlight.setDestinationAirport(airportRepository.findAirportEntityByAirportCode(flight.getLandingAirport()).orElseThrow(() -> new RuntimeException("Could not create flight, airports are wrong!")));
        existingFlight.setPlane(planeRepository.findById(flight.getPlaneId()).orElseThrow(() -> new RuntimeException("Could not create flight, plane id does not exist!")));
        existingFlight.setDepartureDateTime(flight.getDepartureTime());
        existingFlight.setLandingDateTime(flight.getLandingTime());
        existingFlight.setSharedFlight(!flight.getAirlineCompany().equals("No shared flight"));
        if (existingFlight.isSharedFlight()) {
            existingFlight.setSharedFlightCompany(companyRepository.findById(flight.getAirlineCompany()).orElseThrow(() -> new RuntimeException("Could not create flight, the company does not exist!")));
        } else {
            existingFlight.setSharedFlightCompany(null);
        }
        existingFlight.setFlightRange(getRange(existingFlight.getSourceAirport().getCity(), existingFlight.getDestinationAirport().getCity()));
        existingFlight.setStandardMenu("Standard menu");

        return saveFlightObj(existingFlight);
    }

    @Override
    public FlightEntity updateSourceAirport(String flightNumber, AirportEntity sourceAirport) {
        if (sourceAirport == null) {
            throw new RuntimeException("Source airport cannot be null");
        }
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setSourceAirport(sourceAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateDestinationAirport(String flightNumber, AirportEntity destinationAirport) {
        if (destinationAirport == null) {
            throw new RuntimeException("Destination airport cannot be null");
        }
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setDestinationAirport(destinationAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updatePlane(String flightNumber, PlaneEntity plane) {
        if (plane == null) {
            throw new RuntimeException("Plane cannot be null");
        }
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setPlane(plane);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateFlightRange(String flightNumber, Integer flightRange) {
        if (flightRange == null || flightRange <= 0) {
            throw new RuntimeException("Flight range must be greater than 0");
        }
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setFlightRange(flightRange);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateDepartureDateTime(String flightNumber, LocalDateTime departureDateTime) {
        if (departureDateTime == null || departureDateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Departure time cannot be in the past or null");
        }
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setDepartureDateTime(departureDateTime);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateLandingDateTime(String flightNumber, LocalDateTime landingDateTime) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        if (landingDateTime == null || landingDateTime.isBefore(flight.getDepartureDateTime())) {
            throw new RuntimeException("Landing time cannot be before departure time or null");
        }
        flight.setLandingDateTime(landingDateTime);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateSharedFlight(String flightNumber, boolean sharedFlight) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setSharedFlight(sharedFlight);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateSharedFlightCompany(String flightNumber, CompanyEntity sharedFlightCompany) {
        if (sharedFlightCompany == null) {
            throw new RuntimeException("Shared flight company cannot be null");
        }
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setSharedFlightCompany(sharedFlightCompany);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateStandardMenu(String flightNumber, String standardMenu) {
        if (standardMenu == null || standardMenu.isEmpty()) {
            throw new RuntimeException("Standard menu cannot be null or empty");
        }
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setStandardMenu(standardMenu);
        return flightRepository.save(flight);
    }

    // End of Update Methods

    //------------------------------------------------------------------------------------------------------------

    // Simple getters for the entities of the FlightEntity
    @Override
    public AirportEntity getSourceAirport(String flightNumber) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        return flight.getSourceAirport();
    }

    @Override
    public AirportEntity getDestinationAirport(String flightNumber) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        return flight.getDestinationAirport();
    }

    @Override
    public PlaneEntity getPlane(String flightNumber) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        return flight.getPlane();
    }

    @Override
    public CompanyEntity getCompany(String flightNumber) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        return flight.getSharedFlightCompany();
    }

    @Override
    public LocalDateTime getDateTime(String flightNumber) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        return flight.getDepartureDateTime();
    }

    // End of Simple getters for the entities of the FlightEntity

    //------------------------------------------------------------------------------------------------------------


    // Getters for DTOs (Projections)
    @Override
    public VehicleTypeRepository.SeatingPlanProjection findSeatingPlanByFlightNumber(String flightNumber) {
        VehicleTypeEntity vehicleTypeEntity = flightRepository.findVehicleTypeByFlightId(flightNumber);
        if (vehicleTypeEntity == null) {
            throw new EntityNotFoundException("Flight not found");
        }
        String vehicleType = vehicleTypeEntity.getVehicleType();
        return vehicleTypeRepository.findByVehicleType(vehicleType, VehicleTypeRepository.SeatingPlanProjection.class);
    }

    @Override
    public VehicleTypeRepository.AttendeeCapacityProjection findAttendeeCapacityByFlightNumber(String flightNumber) {
        VehicleTypeEntity vehicleTypeEntity = flightRepository.findVehicleTypeByFlightId(flightNumber);
        if (vehicleTypeEntity == null) {
            throw new EntityNotFoundException("Flight not found");
        }
        String vehicleType = vehicleTypeEntity.getVehicleType();
        return vehicleTypeRepository.findByVehicleType(vehicleType, VehicleTypeRepository.AttendeeCapacityProjection.class);
    }

    @Override
    public VehicleTypeRepository.PilotCapacityProjection findPilotCapacityByFlightNumber(String flightNumber) {
        VehicleTypeEntity vehicleTypeEntity = flightRepository.findVehicleTypeByFlightId(flightNumber);
        if (vehicleTypeEntity == null) {
            throw new EntityNotFoundException("Flight not found");
        }
        String vehicleType = vehicleTypeEntity.getVehicleType();
        return vehicleTypeRepository.findByVehicleType(vehicleType, VehicleTypeRepository.PilotCapacityProjection.class);
    }

    public FlightRepository.FlightDetailsProjection findFlightDetailsByFlightNumber(String flightNumber) {
        FlightRepository.FlightDetailsProjection flightDetails = flightRepository.findFlightDetailsByFlightNumber(flightNumber);
        if (flightDetails == null) {
            throw new EntityNotFoundException("Flight not found");
        }
        return flightDetails;
    }

    // End of Getters for DTOs (Projections)


    // The method for obtaining the SeatingPlan
    @Override
    public List<SeatingTypeDTO> decodeSeatingPlan(String flightNumber) {
        // Get the VehicleTypeEntity from the flight number
        VehicleTypeEntity vehicleTypeEntity = flightRepository.findVehicleTypeByFlightId(flightNumber);
        if (vehicleTypeEntity == null) {
            throw new EntityNotFoundException("Flight not found");
        }
        String vehicleType = vehicleTypeEntity.getVehicleType();

        // Get the seating plan from the vehicle type
        VehicleTypeRepository.SeatingPlanProjection seatingPlanProjection = vehicleTypeRepository.findByVehicleType(vehicleType, VehicleTypeRepository.SeatingPlanProjection.class);
        String seatingPlan = seatingPlanProjection.getSeatingPlan();

        List<SeatingTypeDTO> seatingList = new ArrayList<>();

        // Split the encoded seating plan into business and economy seating types
        String[] seatingClasses = seatingPlan.split("=");

        int currentRow = 1; // initialize the current row counter

        // Process each seating class (e.g., business, economy)
        for (String seatingClass : seatingClasses) {
            String[] components = seatingClass.split("\\*");
            String[] columnGroups = components[0].split("\\|");
            int rows = Integer.parseInt(components[1]);

            SeatingTypeDTO seatingTypeDTO = new SeatingTypeDTO();
            seatingTypeDTO.setType(currentRow == 1 ? "business" : "economy");
            seatingTypeDTO.setStartRow(currentRow);
            seatingTypeDTO.setEndRow(currentRow + rows - 1);
            seatingTypeDTO.setColumns(decodeColumns(columnGroups));

            seatingList.add(seatingTypeDTO);

            currentRow += rows; // update the current row counter
        }

        return seatingList;
    }


    private String decodeColumns(String[] columns) {
        StringBuilder decodedColumns = new StringBuilder();
        int columnIndex = 0;

        for (int i = 0; i < columns.length; i++) {
            int numSeats = Integer.parseInt(columns[i]);
            char startColumn = (char) ('A' + columnIndex);
            char endColumn = (char) (startColumn + numSeats - 1);
            if (decodedColumns.length() > 0) {
                decodedColumns.append('/');
            }
            decodedColumns.append(startColumn).append('-').append(endColumn);
            columnIndex += numSeats;
        }

        return decodedColumns.toString();
    }



    @Override
    public List<SeatingDTO> findBookedFlightsByFlightNumber(String flightNumber) {

        FlightEntity flight = getFlightOrThrow(flightNumber);

        List<PassengerFlight> passengers = passengerFlightRepository.findPassengerFlightByFlight(flight);
        int businessEndRowNonFinal = 0;
        List<SeatingTypeDTO> seatingList = decodeSeatingPlan(flightNumber);
        for (SeatingTypeDTO seating : seatingList) {
            if ("business".equals(seating.getType())) {
                businessEndRowNonFinal = seating.getEndRow();
                break;
            }
        }
        final int businessEndRow = businessEndRowNonFinal;

        // Map each PassengerFlight to a Seat
        List<SeatingDTO> seats = passengers.stream().map(passengerFlight -> {
            SeatingDTO seat = new SeatingDTO();
            seat.setSeatPosition(passengerFlight.getSeatNumber());
            seat.setSeatType(getSeatType(passengerFlight.getSeatNumber(), businessEndRow));
            seat.setStatus(true);
            seat.setUserId(passengerFlight.getPassenger().getPassengerId());
            return seat;
        }).collect(Collectors.toList());

        // Add all seats from seating plan
        seatingList.forEach(seating -> {
            int startRow = seating.getStartRow();
            int endRow = seating.getEndRow();
            String[] columnGroups = seating.getColumns().split("/");
            for (int row = startRow; row <= endRow; row++) {
                for (String group : columnGroups) {
                    String[] columns = group.split("-");
                    char startColumn = columns[0].charAt(0);
                    char endColumn = columns[1].charAt(0);
                    for (char col = startColumn; col <= endColumn; col++) {
                        String seatPosition = row + String.valueOf(col);
                        boolean isBooked = seats.stream().anyMatch(seat -> seat.getSeatPosition().equals(seatPosition));
                        if (!isBooked) {
                            SeatingDTO seat = new SeatingDTO();
                            seat.setSeatPosition(seatPosition);
                            seat.setSeatType(seating.getType());
                            seat.setStatus(false);
                            seat.setUserId(-1);
                            seats.add(seat);
                        }
                    }
                }
            }
        });

        // Sort the list by seat position
        seats.sort(Comparator.comparing(SeatingDTO::getSeatPosition, new Comparator<String>() {
            @Override
            public int compare(String seat1, String seat2) {
                // Extract row and column
                int row1 = Integer.parseInt(seat1.replaceAll("[^0-9]", ""));
                int row2 = Integer.parseInt(seat2.replaceAll("[^0-9]", ""));
                char column1 = seat1.replaceAll("[^A-Za-z]", "").charAt(0);
                char column2 = seat2.replaceAll("[^A-Za-z]", "").charAt(0);

                // Compare row first, then column
                if (row1 != row2) {
                    return Integer.compare(row1, row2);
                } else {
                    return Character.compare(column1, column2);
                }
            }
        }));

        return seats;
    }

    private String getSeatType(String seatNumber, int businessEndRow) {
        // Determine the seat type based on the row number
        int rowNumber = Integer.parseInt(seatNumber.replaceAll("[^0-9]", ""));
        return rowNumber <= businessEndRow ? "business" : "economy";
    }

    public List<UserDataDTO> getUsersDTOByFlightNumber(String flightNumber) {
        if (flightNumber == null || flightNumber.isEmpty()) {
            throw new IllegalArgumentException("Flight number cannot be null or empty");
        }

        FlightEntity flight = getFlightOrThrow(flightNumber);
        if (flight == null) {
            throw new EntityNotFoundException("Flight not found");
        }

        List<PassengerFlight> passengers = passengerFlightRepository.findPassengerFlightByFlight(flight);
        if (passengers == null || passengers.isEmpty()) {
            throw new EntityNotFoundException("No passengers found for the given flight");
        }

        return passengers.stream().map(passengerFlight -> {
            if (passengerFlight == null || passengerFlight.getPassenger() == null) {
                throw new EntityNotFoundException("Passenger not found");
            }

            UserDataDTO user = UserDataDTOFactory.create_passenger_with_passenger_flight(passengerFlight);
            return user;
        }).collect(Collectors.toList());
    }


    @Override
    public List<String> getAvailableSeats(String flightNumber){

        FlightEntity flight = getFlightOrThrow(flightNumber);

        List<SeatingDTO> seats = findBookedFlightsByFlightNumber(flightNumber);

        List<String> availableSeats = new ArrayList<>();
        for(SeatingDTO seat : seats){
            if(!seat.isStatus()){
                String seatPosition = seat.getSeatPosition();
                availableSeats.add(seatPosition);
            }
        }
        return availableSeats;
    }

}
