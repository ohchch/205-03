package com.example.billingservice.stores;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key

    private String name; // Store Name
    private String phoneNumber; // Store Phone Number
    private String email; // Store Email

    private String unitNumber; // Unit Number, Floor, Building Name
    private String streetName; // Street Name
    private String residentialArea; // Residential Area or Suburb Name
    private String postalCode; // Postal Code
    private String city; // City
    private String state; // State
    private String country = "Malaysia"; // Country

    // Default constructor
    public Store() {}

    // Parameterized constructor
    public Store(String name, String phoneNumber, String email, String unitNumber, String streetName, String residentialArea, String postalCode, String city, String state) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.unitNumber = unitNumber;
        this.streetName = streetName;
        this.residentialArea = residentialArea;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getResidentialArea() {
        return residentialArea;
    }

    public void setResidentialArea(String residentialArea) {
        this.residentialArea = residentialArea;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // equals method override
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        return Objects.equals(id, store.id) &&
                Objects.equals(name, store.name) &&
                Objects.equals(phoneNumber, store.phoneNumber) &&
                Objects.equals(email, store.email) &&
                Objects.equals(unitNumber, store.unitNumber) &&
                Objects.equals(streetName, store.streetName) &&
                Objects.equals(residentialArea, store.residentialArea) &&
                Objects.equals(postalCode, store.postalCode) &&
                Objects.equals(city, store.city) &&
                Objects.equals(state, store.state) &&
                Objects.equals(country, store.country);
    }

    // hashCode method override
    @Override
    public int hashCode() {
        return Objects.hash(id, name, phoneNumber, email, unitNumber, streetName, residentialArea, postalCode, city, state, country);
    }

    // toString method override
    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", unitNumber='" + unitNumber + '\'' +
                ", streetName='" + streetName + '\'' +
                ", residentialArea='" + residentialArea + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
