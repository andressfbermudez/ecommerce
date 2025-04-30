package com.api.ecommerce.web.dto.accessorydto;

import com.api.ecommerce.persistence.entity.accessories.AccessoryEntity;
import java.time.LocalDateTime;

public record AccessoryResponseDTO(
        Long id,
        String name,
        String description,
        Double price,
        Integer Stock,
        String brand,
        String category,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {

    public static AccessoryResponseDTO convertToAccessoryResponseDTO(AccessoryEntity accessoryEntity) {
        return new AccessoryResponseDTO(
                accessoryEntity.getId(), accessoryEntity.getName(), accessoryEntity.getDescription(),
                accessoryEntity.getPrice(), accessoryEntity.getStock(), accessoryEntity.getBrand(),
                accessoryEntity.getCategory().getName(), accessoryEntity.getCreatedDate(),
                accessoryEntity.getLastModifiedDate()
        );
    }
}
