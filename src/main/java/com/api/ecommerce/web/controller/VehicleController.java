package com.api.ecommerce.web.controller;

import com.api.ecommerce.service.VehicleService;
import com.api.ecommerce.web.dto.vehicledto.VehicleCreateDTO;
import com.api.ecommerce.web.dto.vehicledto.VehicleResponseDTO;
import com.api.ecommerce.web.dto.vehicledto.VehicleUpdatedDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@Validated
public class VehicleController {

    // Inyectar un servicio por medio de constructor
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }


    // Para crear un nuevo vehiculo
    @PostMapping("/create")
    public ResponseEntity<Void> createVehicle(
            @Valid @RequestBody VehicleCreateDTO vehicleCreateDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long id = vehicleService.createVehicle(vehicleCreateDTO);

        // Para generar la URL donde puede consultarse el recurso creado
        URI url = uriComponentsBuilder.path("/api/vehicles/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(url).build();
    }


    // Para obtener todos los vehiculos activos
    @GetMapping("/all")
    public ResponseEntity<List<VehicleResponseDTO>> findAllIsActiveTrue() {
        List<VehicleResponseDTO> vehicles = vehicleService.findAllIsActiveTrue();
        return ResponseEntity.ok(vehicles);
    }


    // Para obtener todos los vehiculos incluidos los inactivos
    @GetMapping("/all-including-inactive")
    public ResponseEntity<List<VehicleResponseDTO>> findAllIncludingInactive() {
        List<VehicleResponseDTO> vehicles = vehicleService.findAllIncludingInactive();
        return ResponseEntity.ok(vehicles);
    }


    // Para buscar un vehiculo activo por medio de su id
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> findByIdAndIsActiveTrue(@PathVariable Long id) {
        VehicleResponseDTO vehicleFound = vehicleService.findByIdAndIsActiveTrue(id);
        if (vehicleFound == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicleFound);
    }


    // Para buscar un vehiculo que este activo o inactivo por medio de su id
    @GetMapping("/find/{id}")
    public ResponseEntity<VehicleResponseDTO> findById(@PathVariable Long id) {
        VehicleResponseDTO vehicleFound = vehicleService.findByIdIncludingInactive(id);
        if (vehicleFound == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicleFound);
    }


    // Para buscar un vehiculo por palabras clave
    @GetMapping("/search")
    public ResponseEntity<List<VehicleResponseDTO>> search(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) Integer doors,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String location
    ) {
        if (brand == null && model == null && year == null && price == null &&
                doors == null && color == null && location == null) {
            return ResponseEntity.noContent().build();
        }

        List<VehicleResponseDTO> vehiclesFound = vehicleService.search(brand, model, year, price,
                doors, color, location);
        return ResponseEntity.ok(vehiclesFound);
    }


    // Para actualizar un vehiculo
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleUpdatedDTO vehicleUpdatedDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long idVehicleUpdated = vehicleService.updateVehicle(id, vehicleUpdatedDTO);

        if (idVehicleUpdated == null) {
            return ResponseEntity.notFound().build();
        }

        URI url = uriComponentsBuilder.path("/api/vehicles/{id}")
                .buildAndExpand(idVehicleUpdated)
                .toUri();

        return ResponseEntity.ok().location(url).build(); // Retorna 200 OK con la URL en el header
    }


    // Para realizar eliminacion logica de un vehiculo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteVehicle(@PathVariable Long id) {
        if (!vehicleService.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        vehicleService.softDeleteVehicleById(id);
        return ResponseEntity.noContent().build();
    }


    // Para reactivar un vehiculo eliminado logicamente
    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restoreVehicle(@PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) {
        Long idRestoreVehicle = vehicleService.restoreVehicleById(id);

        if (idRestoreVehicle == null) {
            return ResponseEntity.notFound().build();
        }

        URI url = uriComponentsBuilder.path("/api/vehicles/{id}")
                .buildAndExpand(idRestoreVehicle)
                .toUri();

        return ResponseEntity.ok().location(url).build();
    }
}
