package com.api.ecommerce.service;

import com.api.ecommerce.persistence.entity.accessories.AccessoryEntity;
import com.api.ecommerce.persistence.repository.AccessoryRepository;
import com.api.ecommerce.web.dto.accessorydto.AccessoryCreateDTO;
import com.api.ecommerce.web.dto.accessorydto.AccessoryResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccessoryService {

    // Inyeccion de un respositorio
    private final AccessoryRepository accessoryRepository;

    @Autowired
    public AccessoryService(AccessoryRepository accessoryRepository) {
        this.accessoryRepository = accessoryRepository;
    }


    // Para crear un nuevo accesorio
    public Long createAccesory(AccessoryCreateDTO accessoryCreateDTO) {
        AccessoryEntity newAccesory = new AccessoryEntity(accessoryCreateDTO);
        return accessoryRepository.save(newAccesory).getId();
    }


    // Para obtener todos los accesorios que esten activos
    public List<AccessoryResponseDTO> findAllIsActiveTrue() {
        return accessoryRepository.findByIsActiveTrue().stream()
                .map(AccessoryResponseDTO::convertToAccessoryResponseDTO)
                .toList();
    }


    // Para obtener todos los accesorios incluidos los inactivos
    public List<AccessoryResponseDTO> findAllIncludingInactives() {
        return accessoryRepository.findAll()
                .stream()
                .map(AccessoryResponseDTO::convertToAccessoryResponseDTO)
                .toList();
    }


    // Para buscar un accesorio que este activo por medio de su id
    public AccessoryResponseDTO findByIdAndIsActiveTrue(Long id) {
        if (accessoryRepository.existsByIdAndIsActiveTrue(id)) {
            Optional<AccessoryEntity> optionalAccesory = accessoryRepository.findByIdAndIsActiveTrue(id);
            return AccessoryResponseDTO.convertToAccessoryResponseDTO(optionalAccesory.get());
        }
        return null;
    }


    // Para obtener un accesorio activo o inactivo por medio de su id
    public AccessoryResponseDTO findByIdIncludingInactive(Long id) {
        if (accessoryRepository.existsById(id)) {
            Optional<AccessoryEntity> optionalAccessory = accessoryRepository.findById(id);
            return AccessoryResponseDTO.convertToAccessoryResponseDTO(optionalAccessory.get());
        }
        return null;
    }


    // Para buscar un accesorio por medio de palabras clave
    public List<AccessoryResponseDTO> search(String name, String brand, Double price) {
        return accessoryRepository.search(name, brand, price)
                .stream()
                .map(AccessoryResponseDTO::convertToAccessoryResponseDTO)
                .toList();
    }
}
