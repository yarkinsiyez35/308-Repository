package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.DTO.FrontEndDTOs.FlightDataDTO;
import com.su.FlightScheduler.DTO.SeatDTOs.SeatingDTO;
import com.su.FlightScheduler.Entity.*;
import com.su.FlightScheduler.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public FlightEntity getFlightOrThrow(String flightId) {
        Optional<FlightEntity> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isEmpty()) {
            throw new EntityNotFoundException("Flight not found");
        }
        return optionalFlight.get();
    }


    @Override
    public FlightEntity saveFlight(FlightDataDTO flightDataDTO, int adminId) {
        FlightEntity flightEntity = new FlightEntity();
        Optional<AirportEntity> sourceAirport = airportRepository.findAirportEntityByAirportCode(flightDataDTO.getDepartureAirport());
        Optional<AirportEntity> landingAirport = airportRepository.findAirportEntityByAirportCode(flightDataDTO.getLandingAirport());
        Optional<PlaneEntity> plane = planeRepository.findById(flightDataDTO.getPlaneId());
        if(sourceAirport.isEmpty() || landingAirport.isEmpty())
        {
            throw new RuntimeException("Could not create flight, airports are wrong!");
        }
        if (plane.isEmpty())
        {
            throw new RuntimeException("Could not create flight, plane id does not exist!");
        }
        flightEntity.setFlightNumber(flightDataDTO.getFlightId());
        flightEntity.setFlightInfo("Regular flight");
        flightEntity.setSourceAirport(sourceAirport.get());
        flightEntity.setDestinationAirport(landingAirport.get());
        flightEntity.setPlane(plane.get());
        //do this later with tables
        flightEntity.setFlightRange(2000);
        flightEntity.setDepartureDateTime(flightDataDTO.getDepartureTime());
        flightEntity.setLandingDateTime(flightDataDTO.getLandingTime());
        if (flightDataDTO.getAirlineCompany().equals("No shared flight"))
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
                CompanyEntity company = new CompanyEntity(flightDataDTO.getAirlineCompany(), "");
                CompanyEntity savedCompany = companyRepository.save(company);
                flightEntity.setSharedFlightCompany(savedCompany);
            }
        }
        Optional<AdminEntity> adminEntity = adminRepository.findById(adminId);
        if (adminEntity.isEmpty())
        {
            throw new RuntimeException("Could not create flight, admin does not exist!");
        }
        flightEntity.setAdmin(adminEntity.get());
        flightEntity.setStandardMenu("Standard menu");
        return flightRepository.save(flightEntity);
    }

    @Override
    public FlightEntity saveFlightObj(FlightEntity flight) {
        return flightRepository.save(flight);
    }

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




    // Multi-Layered Create Method
    @Override
    public FlightEntity createFlight(String flightNumber, String flightInfo, AdminEntity admin) {
        FlightEntity flight = new FlightEntity(flightNumber, flightInfo);
        flight.setAdmin(admin);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity addFlightParams1(String flightNumber, PlaneEntity plane, AirportEntity sourceAirport, AirportEntity destinationAirport) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setPlane(plane);
        flight.setSourceAirport(sourceAirport);
        flight.setDestinationAirport(destinationAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity addFlightParams2(String flightNumber, Integer flightRange, LocalDateTime departureDateTime, LocalDateTime landingDateTime) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setFlightRange(flightRange);
        flight.setDepartureDateTime(departureDateTime);
        flight.setLandingDateTime(landingDateTime);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity addFlightParams3(String flightNumber, boolean sharedFlight, CompanyEntity sharedFlightCompany) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setSharedFlight(sharedFlight);
        flight.setSharedFlightCompany(sharedFlightCompany);
        return flightRepository.save(flight);
    }
    // End of Multi-Layered Create Method

    // -----------------------------------------------------------------------------------------------


    // Find Methods
    @Override
    public Optional<FlightEntity> findFlightByNumber(String flightNumber) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        return Optional.of(flight);
    }

    @Override
    public List<FlightEntity> findAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public List<FlightEntity> findFlightsByDepartureAirport(String airportCode) {
    List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCode(airportCode);
    if (flights == null || flights.isEmpty()) {
        throw new EntityNotFoundException("No flights found for the given departure airport");
    }
    return flights;
    }

    @Override
    public List<FlightEntity> findFlightsByDestinationAirport(String airportCode) {
        List<FlightEntity> flights = flightRepository.findByDestinationAirportAirportCode(airportCode);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given destination airport");
        }
        return flights;
    }

    @Override
    public List<FlightEntity> findFlightsByDepartureAndDestinationAirport(String departureAirportCode, String destinationAirportCode) {
        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCodeAndDestinationAirportAirportCode(departureAirportCode, destinationAirportCode);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure and destination airports");
        }
        return flights;
    }

    @Override
    public List<FlightEntity> findFlightsByDepartureDateTime(LocalDateTime departureDateTime) {
        List<FlightEntity> flights = flightRepository.findByDepartureDateTime(departureDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure date and time");
        }
        return flights;
    }

    @Override
    public List<FlightEntity> findFlightsByLandingDateTime(LocalDateTime landingDateTime) {
        List<FlightEntity> flights = flightRepository.findByLandingDateTime(landingDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given landing date and time");
        }
        return flights;
    }

    @Override
    public List<FlightEntity> findFlightsByDepartureAndLandingDateTime(LocalDateTime departureDateTime, LocalDateTime landingDateTime) {
        List<FlightEntity> flights = flightRepository.findByDepartureDateTimeAndLandingDateTime(departureDateTime, landingDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure and landing date and time");
        }
        return flights;
    }

    @Override
    public List<FlightEntity> findFlightsByDepartureAirportAndDepartureDateTime(String airportCode, LocalDateTime departureDateTime) {
        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCodeAndDepartureDateTime(airportCode, departureDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure airport and departure date and time");
        }
        return flights;
    }

    @Override
    public List<FlightEntity> findFlightsByDestinationAirportAndLandingDateTime(String airportCode, LocalDateTime landingDateTime) {
        List<FlightEntity> flights = flightRepository.findByDestinationAirportAirportCodeAndLandingDateTime(airportCode, landingDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given destination airport and landing date and time");
        }
        return flights;
    }

    @Override
    public List<FlightEntity> findFlightsByDepartureAndDestinationAirportAndDepartureAndLandingDateTime(String departureAirportCode, String destinationAirportCode, LocalDateTime departureDateTime, LocalDateTime landingDateTime) {
        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCodeAndDestinationAirportAirportCodeAndDepartureDateTimeAndLandingDateTime(departureAirportCode, destinationAirportCode, departureDateTime, landingDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure and destination airports and departure and landing date and time");
        }
        return flights;
    }

    @Override
    public List<FlightEntity> findFlightsByDepartureAirportAndDestinationAirportAndDepartureAndLandingDateTime(String departureAirportCode, String destinationAirportCode, LocalDateTime departureDateTime, LocalDateTime landingDateTime) {
        List<FlightEntity> flights = flightRepository.findBySourceAirportAirportCodeAndDestinationAirportAirportCodeAndDepartureDateTimeAndLandingDateTime(departureAirportCode, destinationAirportCode, departureDateTime, landingDateTime);
        if (flights == null || flights.isEmpty()) {
            throw new EntityNotFoundException("No flights found for the given departure and destination airports and departure and landing date and time");
        }
        return flights;
    }
    // End of Find Methods

    //------------------------------------------------------------------------------------------------------------

    // Delete Method
    @Override
    public void deleteFlightByNumber(String flightNumber) {
        getFlightOrThrow(flightNumber);
        flightRepository.deleteById(flightNumber);
    }


    //------------------------------------------------------------------------------------------------------------

    // Update Methods
    @Override
    public FlightEntity updateFlightInfo(String flightNumber, String flightInfo) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setFlightInfo(flightInfo);
        return flightRepository.save(flight);
    }

    // This method will not be used, but it is here to show how to update an object
    @Override
    public FlightEntity updateFlight(FlightEntity flight) {
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateSourceAirport(String flightNumber, AirportEntity sourceAirport) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setSourceAirport(sourceAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateDestinationAirport(String flightNumber, AirportEntity destinationAirport) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setDestinationAirport(destinationAirport);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updatePlane(String flightNumber, PlaneEntity plane) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setPlane(plane);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateFlightRange(String flightNumber, Integer flightRange) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setFlightRange(flightRange);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateDepartureDateTime(String flightNumber, LocalDateTime departureDateTime) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setDepartureDateTime(departureDateTime);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateLandingDateTime(String flightNumber, LocalDateTime landingDateTime) {
        FlightEntity flight = getFlightOrThrow(flightNumber);
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
        FlightEntity flight = getFlightOrThrow(flightNumber);
        flight.setSharedFlightCompany(sharedFlightCompany);
        return flightRepository.save(flight);
    }

    @Override
    public FlightEntity updateStandardMenu(String flightNumber, String standardMenu) {
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

    // Getters for Requests to thin out the Controller
    @Override
    public AirportEntity getAirportFromRequest(Map<String, Object> request, String key) {
        String airportCode = (String) request.get(key);
        return airportRepository.findAirportEntityByAirportCode(airportCode)
                .orElseThrow(() -> new RuntimeException("Airport not found"));
    }

    @Override
    public PlaneEntity getPlaneFromRequest(Map<String, Object> request, String key) {
        String planeId = (String) request.get(key);
        return planeRepository.findById(planeId)
                .orElseThrow(() -> new RuntimeException("Plane not found"));
    }

    @Override
    public CompanyEntity getCompanyFromRequest(Map<String, Object> request, String key) {
        String companyId = (String) request.get(key);
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @Override
    public LocalDateTime getDateTimeFromRequest(Map<String, Object> request, String key) {
        String dateTimeString = (String) request.get(key);
        return LocalDateTime.parse(dateTimeString);
    }

    // End of Getters for Requests

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

        for (String seatingClass : seatingClasses) {
            // Split the seating class into individual seating types
            String[] seatingTypes = seatingClass.split("\\*");

            for (String seatingType : seatingTypes) {
                SeatingTypeDTO seating = new SeatingTypeDTO();

                // Split the seating type into rows and columns
                String[] rowsAndColumns = seatingType.split("\\|");

                // Set the start row, end row, and columns for this seating type
                seating.setStartRow(Integer.parseInt(rowsAndColumns[0]));
                seating.setEndRow(Integer.parseInt(rowsAndColumns[1]));
                seating.setColumns(rowsAndColumns[2]);

                // Add this seating type to the list
                seatingList.add(seating);
            }
        }

        return seatingList;
    }

    @Override
    public List<SeatingDTO> findBookedFlightsByFlightNumber(String flightNumber) {

    FlightEntity flight = getFlightOrThrow(flightNumber);

    List<PassengerFlight> passengers = passengerFlightRepository.findPassengerFlightByFlight(flight);
    if (passengers.isEmpty()) {
        throw new EntityNotFoundException("No passengers found for flight with ID: " + flightNumber);
    }

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

    return seats;
    }

    private String getSeatType(String seatNumber, int businessEndRow) {
        // Determine the seat type based on the row number
        int rowNumber = Integer.parseInt(seatNumber.substring(1));
        return rowNumber <= businessEndRow ? "business" : "economy";
    }
}
