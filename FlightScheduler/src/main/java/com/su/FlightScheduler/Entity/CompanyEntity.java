package com.su.FlightScheduler.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "companies")
public class CompanyEntity implements Serializable {

    @Id
    @Column(name = "company_name")
    private String companyName;

    @Column(name = "additional_info", columnDefinition = "TEXT")
    private String additionalInfo;

    public CompanyEntity() {
    }

    public CompanyEntity(String companyName, String additionalInfo) {
        this.companyName = companyName;
        this.additionalInfo = additionalInfo;
    }

    // getters and setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}