package com.api.ecommerce.web.controller;

import java.net.URI;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import com.api.ecommerce.service.VehicleService;
import org.springframework.web.bind.annotation.*;
import com.api.ecommerce.web.dto.VehicleCreateDTO;
import com.api.ecommerce.web.dto.VehicleUpdatedDTO;
import com.api.ecommerce.web.dto.VehicleResponseDTO;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/vehicles")
@Validated
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createVehicle(@Valid @RequestBody VehicleCreateDTO vehicleCreateDTO,
                                              UriComponentsBuilder uriComponentsBuilder) {

        Long id = vehicleService.createVehicle(vehicleCreateDTO);

        // Para generar la URL donde puede consultarse el recurso creado
        URI url = uriComponentsBuilder.path("/api/vehicles/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(url).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleResponseDTO>> getAll() {
        List<VehicleResponseDTO> vehicles = vehicleService.findAll();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> findById(@PathVariable Long id) {
        VehicleResponseDTO vehicleFound = vehicleService.findById(id);
        if (vehicleFound == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vehicleFound);
    }

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
}
