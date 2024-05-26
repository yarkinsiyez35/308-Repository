package com.su.FlightScheduler.Entity;
import com.su.FlightScheduler.DTO.PilotDTOs.PilotWithLanguagesDTO;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pilots")
public class PilotEntity  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //this needs to be added for the sql to automatically generate id
    @Column(name = "pilot_id")
    private int pilotId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "allowed_range", nullable = false)
    private int allowedRange;

    @Column(name = "nationality", nullable = false)
    private String nationality;

    @Column(name = "seniority", nullable = false)
    private String seniority;

    @OneToMany(mappedBy = "pilotLanguagePK.pilotId", cascade = CascadeType.REMOVE)
    private List<PilotLanguageEntity> languages;

    @OneToMany(mappedBy = "pilotAssignmentPK.pilotId")
    private List<PilotAssignmentEntity> assignments;


    @PreRemove
    private void preRemove()    //THIS IS NOT TESTED
    {
        if (assignments != null)    //pilot is assigned to flights
        {
            for (PilotAssignmentEntity pilotAssignmentEntity : assignments) //for each flight assignment
            {
                //set the pilotId to null
                PilotAssignmentPK pilotAssignmentPK = new PilotAssignmentPK(null, pilotAssignmentEntity.getPilotAssignmentPK().getFlightNumber());
                //update the PilotAssignmentEntity
                pilotAssignmentEntity.setPilotAssignmentPK(pilotAssignmentPK);
            }
        }
    }
    public PilotEntity() {}

    public PilotEntity(int pilotId, String email, String password, String firstName, String surname, int age, String gender, int allowedRange, String nationality, String seniority) {
        this.pilotId = pilotId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.allowedRange = allowedRange;
        this.nationality = nationality;
        this.seniority = seniority;
    }

    public PilotEntity(String email, String password, String firstName, String surname, int age, String gender, int allowedRange, String nationality, String seniority) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.allowedRange = allowedRange;
        this.nationality = nationality;
        this.seniority = seniority;
    }

    public PilotEntity(PilotEntity pilotEntity)
    {
        this.pilotId = pilotEntity.pilotId;
        this.email = pilotEntity.email;
        this.password = pilotEntity.password;
        this.firstName = pilotEntity.firstName;
        this.surname = pilotEntity.surname;
        this.age = pilotEntity.age;
        this.gender = pilotEntity.gender;
        this.allowedRange = pilotEntity.allowedRange;
        this.nationality = pilotEntity.nationality;
        this.seniority = pilotEntity.seniority;
    }

    public PilotEntity(String email, String password, String firstName, String surname, int age, String gender, int allowedRange, String nationality, String seniority, List<PilotLanguageEntity> languages, List<PilotAssignmentEntity> assignments) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.allowedRange = allowedRange;
        this.nationality = nationality;
        this.seniority = seniority;
        this.languages = languages;
        this.assignments = assignments;
    }

    public PilotEntity(PilotWithLanguagesDTO pilotWithLanguagesDTO) //this constructor will be used to save a pilotEntity
    {
        this.pilotId = pilotWithLanguagesDTO.getPilotId();
        this.email = pilotWithLanguagesDTO.getEmail();
        this.password = pilotWithLanguagesDTO.getPassword();
        this.firstName = pilotWithLanguagesDTO.getFirstName();
        this.surname = pilotWithLanguagesDTO.getSurname();
        this.age = pilotWithLanguagesDTO.getAge();
        this.gender = pilotWithLanguagesDTO.getGender();
        this.allowedRange = pilotWithLanguagesDTO.getAllowedRange();
        this.nationality = pilotWithLanguagesDTO.getNationality();
        this.seniority = pilotWithLanguagesDTO.getSeniority();
        List<PilotLanguageEntity> languageEntities = new ArrayList<>();
        for ( String language : pilotWithLanguagesDTO.getLanguages())
        {
            PilotLanguagePK pilotLanguagePK = new PilotLanguagePK(this.pilotId, language);
            PilotLanguageEntity pilotLanguageEntity = new PilotLanguageEntity(pilotLanguagePK);
            languageEntities.add(pilotLanguageEntity);
        }
        this.languages = languageEntities;
    }

    public PilotEntity(PilotWithLanguagesDTO pilotWithLanguagesDTO, boolean idIsPresent)    //set id if id is present
    {
        if (idIsPresent)
        {
            this.pilotId = pilotWithLanguagesDTO.getPilotId();
        }
        this.email = pilotWithLanguagesDTO.getEmail();
        this.password = pilotWithLanguagesDTO.getPassword();
        this.firstName = pilotWithLanguagesDTO.getFirstName();
        this.surname = pilotWithLanguagesDTO.getSurname();
        this.age = pilotWithLanguagesDTO.getAge();
        this.gender = pilotWithLanguagesDTO.getGender();
        this.allowedRange = pilotWithLanguagesDTO.getAllowedRange();
        this.nationality = pilotWithLanguagesDTO.getNationality();
        this.seniority = pilotWithLanguagesDTO.getSeniority();
        List<PilotLanguageEntity> languageEntities = new ArrayList<>();
        for ( String language : pilotWithLanguagesDTO.getLanguages())
        {
            PilotLanguagePK pilotLanguagePK = new PilotLanguagePK(this.pilotId, language);
            PilotLanguageEntity pilotLanguageEntity = new PilotLanguageEntity(pilotLanguagePK);
            languageEntities.add(pilotLanguageEntity);
        }
        this.languages = languageEntities;
    }

    public int getPilotId() {
        return pilotId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public int getAllowedRange() {
        return allowedRange;
    }

    public String getNationality() {
        return nationality;
    }

    public String getSeniority() {
        return seniority;
    }

    public List<PilotLanguageEntity> getLanguages() {
        return languages;
    }

    public List<PilotAssignmentEntity> getAssignments() {
        return assignments;
    }

    public void setPilotId(int pilotId) {
        this.pilotId = pilotId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAllowedRange(int allowedRange) {
        this.allowedRange = allowedRange;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setSeniority(String seniority) {
        this.seniority = seniority;
    }

    public void setLanguages(List<PilotLanguageEntity> languages) {
        this.languages = languages;
    }

    public void setAssignments(List<PilotAssignmentEntity> assignments) {
        this.assignments = assignments;
    }
}
