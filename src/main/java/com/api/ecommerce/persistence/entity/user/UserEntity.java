package com.api.ecommerce.persistence.entity.user;

import lombok.Getter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import com.api.ecommerce.persistence.entity.Auditable;
import com.api.ecommerce.web.dto.userdto.UserRegisterDTO;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean locked = false;

    @Column(nullable = false)
    private Boolean disabled = false;

    public UserEntity(UserRegisterDTO userRegisterDTO) {
        this.username = userRegisterDTO.username();
        this.email = userRegisterDTO.email();
        this.password = userRegisterDTO.password();
        this.role = userRegisterDTO.role();
    }
}
