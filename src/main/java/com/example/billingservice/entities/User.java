package com.example.billingservice.entities;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permissions> permissions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserCar> userCars = new HashSet<>();

    public Set<Car> getCars() {
        return userCars.stream()
                       .map(UserCar::getCar)
                       .collect(Collectors.toSet());
    }

    // Constructors, getters, and setters
    public User() {
    }

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permissions permission) {
        this.permissions.add(permission);
    }

    public void removePermission(Permissions permissions) {
        this.permissions.remove(permissions);
    }

    public Set<UserCar> getUserCars() {
        return userCars;
    }

    public void setUserCars(Set<UserCar> userCars) {
        this.userCars = userCars;
    }

    public void addCar(Car car) {
        UserCar userCar = new UserCar(this, car);
        this.userCars.add(userCar);
        car.getUserCars().add(userCar);
    }

    public void removeCar(Car car) {
        for (UserCar userCar : userCars) {
            if (userCar.getUser().equals(this) && userCar.getCar().equals(car)) {
                userCars.remove(userCar);
                car.getUserCars().remove(userCar);
                userCar.setUser(null);
                userCar.setCar(null);
                break;
            }
        }
    }
}
