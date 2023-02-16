package com.bookstore.dto;

import com.bookstore.model.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    @NotNull
    @NotEmpty
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleType name;

}
