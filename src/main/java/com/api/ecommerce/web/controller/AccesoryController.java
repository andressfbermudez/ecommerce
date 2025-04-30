package com.api.ecommerce.web.controller;

import com.api.ecommerce.service.AccessoryService;
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

    private final AccessoryService accessoryService;

    @Autowired
    public AccesoryController(AccessoryService accessoryService) {
        this.accessoryService = accessoryService;
    }

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

    @GetMapping("/all")
    public ResponseEntity<List<AccessoryResponseDTO>> findAllIsActiveTrue() {
        List<AccessoryResponseDTO> accessories = accessoryService.findAllIsActiveTrue();
        return ResponseEntity.ok(accessories);
    }

    @GetMapping("/all-including-inactives")
    public ResponseEntity<List<AccessoryResponseDTO>> findAllIncludingInactive() {
        List<AccessoryResponseDTO> accessoriesIncludingInactives = accessoryService.findAllIncludingInactives();
        return ResponseEntity.ok(accessoriesIncludingInactives);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessoryResponseDTO> findByIdAndIsActiveTrue(@PathVariable Long id) {
        AccessoryResponseDTO accessoryFound = accessoryService.findByIdAndIsActiveTrue(id);
        if (accessoryFound == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accessoryFound);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<AccessoryResponseDTO> findById(@PathVariable Long id) {
        AccessoryResponseDTO accessoryFound = accessoryService.findByIdIncludingInactive(id);
        if (accessoryFound == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accessoryFound);
    }

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
}
