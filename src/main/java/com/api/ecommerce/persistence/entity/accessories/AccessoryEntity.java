package com.api.ecommerce.persistence.entity.accessories;

import com.api.ecommerce.persistence.entity.category.Category;
import com.api.ecommerce.persistence.entity.Product;
import com.api.ecommerce.web.dto.accessorydto.AccessoryCreateDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accessories")
@Getter
@Setter
@NoArgsConstructor
public class AccessoryEntity extends Product {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    protected Category category = new Category(2L, "accesories");

    public AccessoryEntity(AccessoryCreateDTO accessoryCreateDTO) {
        this.setName(accessoryCreateDTO.name());
        this.setDescription(accessoryCreateDTO.description());
        this.setPrice(accessoryCreateDTO.price());
        this.setStock(accessoryCreateDTO.Stock());
        this.setBrand(accessoryCreateDTO.brand());
    }
}
