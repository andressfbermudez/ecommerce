package com.api.ecommerce.web.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.api.ecommerce.service.VehicleService;
import org.springframework.web.bind.annotation.*;
import com.api.ecommerce.web.dao.VehicleCreateDAO;
import com.api.ecommerce.web.dao.VehicleResponseDAO;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/create")
    public ResponseEntity createVehicle(@RequestBody VehicleCreateDAO vehicleCreateDAO) {
        vehicleService.createVehicle(vehicleCreateDAO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleResponseDAO>> getAll() {
        List<VehicleResponseDAO> vehicles = vehicleService.findAll();
        return ResponseEntity.ok(vehicles);
    }
}
