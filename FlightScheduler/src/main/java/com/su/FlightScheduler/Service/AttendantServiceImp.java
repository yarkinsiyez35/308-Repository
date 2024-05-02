package com.su.FlightScheduler.Service;

import com.su.FlightScheduler.Entity.CabinCrewEntity;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class AttendantServiceImp {

    @PersistenceContext
    private EntityManager em;

    // Add a new attendant
    public void addAttendant(CabinCrewEntity attendant) {
        em.persist(attendant);
    }

    // Find an attendant by ID
    public CabinCrewEntity findAttendantById(int id) {
        return em.find(CabinCrewEntity.class, id);
    }

    // Update an attendant's details
    public boolean updateAttendant(int id, CabinCrewEntity updatedAttendant) {
        CabinCrewEntity attendant = findAttendantById(id);
        if (attendant != null) {
            attendant.setEmail(updatedAttendant.getEmail());
            attendant.setPassword(updatedAttendant.getPassword());
            attendant.setFirstName(updatedAttendant.getFirstName());
            attendant.setSurname(updatedAttendant.getSurname());
            attendant.setAge(updatedAttendant.getAge());
            attendant.setGender(updatedAttendant.getGender());
            attendant.setNationality(updatedAttendant.getNationality());
            attendant.setSeniority(updatedAttendant.getSeniority());
            attendant.setLanguages(updatedAttendant.getLanguages());
            attendant.setRecipes(updatedAttendant.getRecipes());
            em.merge(attendant);
            return true;
        }
        return false;
    }

    // Delete an attendant
    public boolean deleteAttendant(int id) {
        CabinCrewEntity attendant = findAttendantById(id);
        if (attendant != null) {
            em.remove(attendant);
            return true;
        }
        return false;
    }

    // List all attendants
    public List<CabinCrewEntity> getAllAttendants() {
        TypedQuery<CabinCrewEntity> query = em.createQuery("SELECT a FROM CabinCrewEntity a", CabinCrewEntity.class);
        return query.getResultList();
    }
}
