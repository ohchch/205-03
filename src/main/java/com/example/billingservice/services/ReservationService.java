package com.example.billingservice.services;

import com.example.billingservice.entities.Car;
import com.example.billingservice.entities.Reservation;
import com.example.billingservice.entities.User;
import com.example.billingservice.repositories.CarRepository;
import com.example.billingservice.repositories.ReservationRepository;
import com.example.billingservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * 创建预约
     *
     * @param carId 车辆ID
     * @param userId 用户ID
     * @throws Exception 如果车辆已经全部预约满
     */
    @Transactional
    public void makeReservation(Long carId, Long userId) throws Exception {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + carId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));

        List<Reservation> reservations = reservationRepository.findByCar(car);

        if (reservations.size() >= 5) {
            throw new Exception("Car is fully reserved.");
        }

        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setUser(user);
        reservation.setReservationTime(LocalDateTime.now());

        reservationRepository.save(reservation);
    }


    public List<Reservation> getReservationsForCar(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + carId));
        return reservationRepository.findByCar(car);
    }

    @Transactional
    public void deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reservation Id: " + reservationId));

        reservationRepository.delete(reservation);
    }
}
