package com.example.billingservice.services;

import com.example.billingservice.dto.ActivateDTO;
import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.entities.Activate;
import com.example.billingservice.entities.Bidding;
import com.example.billingservice.entities.Car;
import com.example.billingservice.entities.CarActivate;
import com.example.billingservice.entities.Reservation;
import com.example.billingservice.entities.User;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.repositories.ActivateRepository;
import com.example.billingservice.repositories.BiddingRepository;
import com.example.billingservice.repositories.CarActivateRepository;
import com.example.billingservice.repositories.CarRepository;
import com.example.billingservice.repositories.ReservationRepository;
import com.example.billingservice.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarActivateRepository carActivateRepository;

    @Autowired
    private ActivateRepository activateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BiddingRepository biddingRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    private final String UPLOAD_DIR = "\\uploads\\img\\";

    @Override
    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDTO> getActivatedCars() {
        return carRepository.findByCarActivate_Activate_Status("Activated").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CarDTO> getDeactivatedCars() {
        return carRepository.findByCarActivate_Activate_Status("Deactivated").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    

    @Override
    public CarDTO getCarById(Long id) throws ResourceNotFoundException {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        return convertToDTO(car);
    }

    @Override
    @Transactional
    public CarDTO addCar(CarDTO carDTO, MultipartFile image, Long userId) throws IOException {
        Car car = convertToEntity(carDTO);

        car = carRepository.save(car);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.addCar(car);
        userRepository.save(user);

        saveCarActivate(carDTO, car);

        if (!image.isEmpty()) {
            saveImage(car, image);
        }

        return convertToDTO(car);
    }

    @Override
    @Transactional
    public CarDTO updateCar(CarDTO carDTO, MultipartFile image) throws ResourceNotFoundException, IOException {
        Car car = carRepository.findById(carDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        car.setName(carDTO.getName());
        car.setBrand(carDTO.getBrand());
        car.setModel(carDTO.getModel());
        car.setRegistration(carDTO.getRegistration());
        car.setPrice(carDTO.getPrice());

        saveCarActivate(carDTO, car);

        if (!image.isEmpty()) {
            saveImage(car, image);
        }

        car = carRepository.save(car);
        return convertToDTO(car);
    }

    @Override
    @Transactional
    public void deleteCarById(Long id) throws ResourceNotFoundException {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        deleteImageFile(car);

        carActivateRepository.deleteByCarId(car.getId());

        carRepository.delete(car);
    }



    

    private void saveCarActivate(CarDTO carDTO, Car car) throws ResourceNotFoundException {
        Activate activate = activateRepository.findByStatus("Activated")
                .orElseThrow(() -> new ResourceNotFoundException("Activate status 'Activated' not found"));
    
        CarActivate carActivate = new CarActivate();
        carActivate.setCar(car);
        carActivate.setActivate(activate);
    
        carActivateRepository.save(carActivate);
    }
    

    private void saveImage(Car car, MultipartFile image) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        String fileExtension = FilenameUtils.getExtension(originalFilename);
        String fileName = car.getId() + "." + fileExtension;
        String imagePath = UPLOAD_DIR + fileName;

        File file = new File(uploadDir + imagePath);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        image.transferTo(file);

        car.setImagePath(imagePath.replace("\\", "/"));
        logger.info("Saved image file to: " + file.getAbsolutePath());
    }

    private void deleteImageFile(Car car) {
        String imagePath = car.getImagePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(uploadDir + imagePath);
            if (file.delete()) {
                logger.info("Deleted image file: " + file.getAbsolutePath());
            } else {
                logger.warn("Failed to delete image file: " + file.getAbsolutePath());
            }
        }
    }

    private CarDTO convertToDTO(Car car) {
        CarDTO carDTO = new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setName(car.getName());
        carDTO.setBrand(car.getBrand());
        carDTO.setModel(car.getModel());
        carDTO.setRegistration(car.getRegistration());
        carDTO.setPrice(car.getPrice());
        carDTO.setImagePath(car.getImagePath());

        if (car.getCarActivate() != null && car.getCarActivate().getActivate() != null) {
            Activate activate = car.getCarActivate().getActivate();
            ActivateDTO activateDTO = new ActivateDTO();
            activateDTO.setId(activate.getId());
            activateDTO.setStatus(activate.getStatus());
            carDTO.setCarActivate(activateDTO);
            carDTO.setActive("Activated".equals(activate.getStatus()));
        }

        Bidding highestBid = biddingRepository.findTopByCarOrderByBiddingPriceDesc(car);
        if (highestBid != null) {
            carDTO.setHighestBiddingPrice(highestBid.getBiddingPrice());
            carDTO.setHighestBidderEmail(highestBid.getUser().getEmail());
        } else {
            carDTO.setHighestBiddingPrice(0.0);
            carDTO.setHighestBidderEmail("No bids yet");
        }

        List<Reservation> reservations = reservationRepository.findByCar(car);
        System.out.println("Reservations for car " + car.getId() + ": " + reservations);
        carDTO.setReservations(reservations);

        return carDTO;
    }


    private Car convertToEntity(CarDTO carDTO) {
        Car car = new Car();
        car.setId(carDTO.getId());
        car.setName(carDTO.getName());
        car.setBrand(carDTO.getBrand());
        car.setModel(carDTO.getModel());
        car.setRegistration(carDTO.getRegistration());
        car.setPrice(carDTO.getPrice());
        return car;
    }

    @Transactional
    public void toggleActivate(Long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new EntityNotFoundException("Car not found"));
        CarActivate carActivate = car.getCarActivate();

        if (carActivate == null) {
            throw new EntityNotFoundException("CarActivate not found for car with id: " + carId);
        }

        Activate currentActivate = carActivate.getActivate();

        Activate newActivate;
        if ("Activated".equals(currentActivate.getStatus())) {
            newActivate = activateRepository.findByStatus("Deactivated")
                    .orElseThrow(() -> new EntityNotFoundException("Deactivated status not found"));
        } else {
            newActivate = activateRepository.findByStatus("Activated")
                    .orElseThrow(() -> new EntityNotFoundException("Activated status not found"));
        }

        carActivate.setActivate(newActivate);
        carActivateRepository.save(carActivate);
    }

    
    @Override
    @Transactional
    public void updateActivateStatus(Car car, Integer activateId) {
        Activate activate = activateRepository.findById(activateId)
                .orElseThrow(() -> new ResourceNotFoundException("Activate not found"));
    
        CarActivate carActivate = car.getCarActivate();
        carActivate.setActivate(activate);
        carActivateRepository.save(carActivate);
    }
    
}
