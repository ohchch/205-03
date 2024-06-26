package com.example.billingservice.services;

import com.example.billingservice.dto.CarDTO;
import com.example.billingservice.entities.Car;
import com.example.billingservice.exceptions.ResourceNotFoundException;
import com.example.billingservice.repositories.CarRepository;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
    private UserService userService;

    @Value("${spring.servlet.multipart.location}")
    private String uploadDir;

    private final String UPLOAD_DIR = "\\uploads\\img\\";

    @Override
    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CarDTO getCarById(Long id) throws ResourceNotFoundException {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        return convertToDTO(car);
    }

    @Override
    public CarDTO addCar(CarDTO carDTO, MultipartFile image, Long userId) throws IOException {
        logger.info("Adding car: {}", carDTO.getName());
        Car car = convertToEntity(carDTO);
        car = carRepository.save(car); // 先保存车辆信息以获取数据库生成的 ID

        if (!image.isEmpty()) {
            saveImage(car, image);
            carRepository.save(car); // 更新车辆信息，包括图片路径
        }
        userService.addCarToUser(userId, car.getId()); // 关联用户和车辆
        return convertToDTO(car);
    }

    @Override
    public CarDTO updateCar(CarDTO carDTO, MultipartFile image) throws ResourceNotFoundException, IOException {
        Car car = carRepository.findById(carDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));

        car.setName(carDTO.getName());
        car.setBrand(carDTO.getBrand());
        car.setModel(carDTO.getModel());
        car.setRegistration(carDTO.getRegistration());
        car.setPrice(carDTO.getPrice());

        if (!image.isEmpty()) {
            saveImage(car, image);
        }

        car = carRepository.save(car); // 更新车辆信息，包括图片路径
        return convertToDTO(car);
    }

    @Override
    public void deleteCarById(Long id) throws ResourceNotFoundException {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        
        deleteImageFile(car);
        carRepository.delete(car);
    }

    private void saveImage(Car car, MultipartFile image) throws IOException {
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename()));
        String fileExtension = FilenameUtils.getExtension(originalFilename);
        String fileName = car.getId() + "." + fileExtension;
        String imagePath = UPLOAD_DIR + fileName; // 存储相对路径

        File file = new File(UPLOAD_DIR + fileName); // 使用完整路径

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs(); // 创建上传目录
        }

        image.transferTo(file); // 保存文件到上传目录

        car.setImagePath(imagePath.replace("\\", "/")); // 替换路径中的反斜杠
        logger.info("Saved image file to: {}", file.getAbsolutePath()); // 输出文件保存路径信息
    }

    private void deleteImageFile(Car car) {
        if (car.getImagePath() != null) {
            String fullPath = uploadDir + "/" + car.getImagePath().replace("/", File.separator); // 构建完整路径
            File imageFile = new File(fullPath);
            if (imageFile.exists()) {
                if (imageFile.delete()) {
                    logger.info("Deleted image file: {}", imageFile.getAbsolutePath());
                } else {
                    logger.error("Failed to delete image file: {}", imageFile.getAbsolutePath());
                }
            } else {
                logger.warn("Image file not found: {}", imageFile.getAbsolutePath());
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
        carDTO.setImagePath(car.getImagePath()); // 设置图片路径
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
}
