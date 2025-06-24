package com.api.ecommerce.web.controller;

import com.api.ecommerce.service.AccessoryService;
import com.api.ecommerce.web.dto.accessorydto.AccesoryUpdatedDTO;
import com.api.ecommerce.web.dto.accessorydto.AccessoryCreateDTO;
import com.api.ecommerce.web.dto.accessorydto.AccessoryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/accessories")
@Validated
public class AccesoryController {

    // Inyeccion de un servicio
    private final AccessoryService accessoryService;

    @Autowired
    public AccesoryController(AccessoryService accessoryService) {
        this.accessoryService = accessoryService;
    }


    // Para crear un nuevo accesorio
    @PostMapping("/create")
    public ResponseEntity<Void> createAccesory(@Valid @RequestBody AccessoryCreateDTO accessoryCreateDTO,
                                               UriComponentsBuilder uriComponentsBuilder
    ) {
        Long id = accessoryService.createAccesory(accessoryCreateDTO);

        // Para generar la URL donde puede consultarse el recurso creado
        URI url = uriComponentsBuilder.path("/api/accesories/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(url).build();
    }


    // Para obtener todos los accesorios que esten activos
    @GetMapping("/all")
    public ResponseEntity<List<AccessoryResponseDTO>> findAllIsActiveTrue() {
        List<AccessoryResponseDTO> accessories = accessoryService.findAllIsActiveTrue();
        return ResponseEntity.ok(accessories);
    }


    // Para obtener todos los accesorios incluidos los inactivos
    @GetMapping("/all-including-inactives")
    public ResponseEntity<List<AccessoryResponseDTO>> findAllIncludingInactive() {
        List<AccessoryResponseDTO> accessoriesIncludingInactives = accessoryService.findAllIncludingInactives();
        return ResponseEntity.ok(accessoriesIncludingInactives);
    }


    // Para buscar un accesorio que este activo por medio de su id
    @GetMapping("/{id}")
    public ResponseEntity<AccessoryResponseDTO> findByIdAndIsActiveTrue(@PathVariable Long id) {
        AccessoryResponseDTO accessoryFound = accessoryService.findByIdAndIsActiveTrue(id);
        if (accessoryFound == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accessoryFound);
    }


    // Para obtener un accesorio activo o inactivo por medio de su id
    @GetMapping("/find/{id}")
    public ResponseEntity<AccessoryResponseDTO> findById(@PathVariable Long id) {
        AccessoryResponseDTO accessoryFound = accessoryService.findByIdIncludingInactive(id);
        if (accessoryFound == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accessoryFound);
    }


    // Para buscar un accesorio por medio de palabras clave
    @GetMapping("/search")
    public ResponseEntity<List<AccessoryResponseDTO>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double price
    ) {
        if (name == null && brand == null && price == null) {
            return ResponseEntity.noContent().build();
        }

        List<AccessoryResponseDTO> accessoriesFound =
                accessoryService.search(name, brand, price);

        return ResponseEntity.ok(accessoriesFound);
    }


    // Para actualizar un vehiculo
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody AccesoryUpdatedDTO accesoryUpdatedDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Long idAccesoryUpdated = accessoryService.updatedAccesory(id, accesoryUpdatedDTO);

        if (idAccesoryUpdated == null) {
            return ResponseEntity.notFound().build();
        }

        URI url = uriComponentsBuilder.path("/api/vehicles/{id}")
                .buildAndExpand(idAccesoryUpdated)
                .toUri();

        return ResponseEntity.ok().location(url).build(); // Retorna 200 OK con la URL en el header
    }


    // Para realizar eliminacion logica de un accesorio
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteAccesory(@PathVariable Long id) {
        if (!accessoryService.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        accessoryService.softDeleteAccesoryById(id);
        return ResponseEntity.noContent().build();
    }



    // Para reactivar un vehiculo eliminado logicamente
    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restoreAccesory(@PathVariable Long id, UriComponentsBuilder uriComponentsBuilder) {
        Long idRestoreAccesory = accessoryService.restoreAccesoryById(id);

        if (idRestoreAccesory == null) {
            return ResponseEntity.notFound().build();
        }

        URI url = uriComponentsBuilder.path("/api/vehicles/{id}")
                .buildAndExpand(idRestoreAccesory)
                .toUri();

        return ResponseEntity.ok().location(url).build();
    }
}
