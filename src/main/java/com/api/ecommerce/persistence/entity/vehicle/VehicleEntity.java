package com.api.ecommerce.persistence.entity.vehicle;

import com.api.ecommerce.persistence.entity.Product;
import com.api.ecommerce.persistence.entity.category.Category;
import com.api.ecommerce.web.dto.vehicledto.VehicleCreateDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
public class VehicleEntity extends Product {

    /*
     * Atributos de la entidad vehiculo que seran columnas
     * en la tabla vehiculos
     */

    @Column(nullable = false, length = 100)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer mileage;

    @Column(name = "engine_capacity", nullable = false)
    private Integer engineCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Transmission transmission;

    @Column(nullable = false)
    private Integer doors;

    @Column(nullable = false, length = 100)
    private String color;

    @Column(nullable = false, length = 100)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    protected Category category = new Category(1L, "vehicle");

    // Contructor para la creacion de un vehiculo
    public VehicleEntity(VehicleCreateDTO vehicleCreateDTO) {
        this.setName(vehicleCreateDTO.name().trim());
        this.setDescription(vehicleCreateDTO.description().trim());
        this.setPrice(vehicleCreateDTO.price());
        this.setStock(vehicleCreateDTO.stock());
        this.setBrand(vehicleCreateDTO.brand().trim());

        this.model = vehicleCreateDTO.model().trim();
        this.year = vehicleCreateDTO.year();
        this.mileage = vehicleCreateDTO.mileage();
        this.engineCapacity = vehicleCreateDTO.engineCapacity();
        this.fuelType = vehicleCreateDTO.fuelType();
        this.transmission = vehicleCreateDTO.transmission();
        this.doors = vehicleCreateDTO.doors();
        this.color = vehicleCreateDTO.color().trim();
        this.location = vehicleCreateDTO.location().trim();
    }
}
