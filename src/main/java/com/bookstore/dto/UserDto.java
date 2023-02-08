package com.bookstore.dto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @NotEmpty
    private Integer id;

    @NotNull
    @NotEmpty
    private String userName;

    @NotNull
    @NotEmpty
    private Integer roleId;

    @NotNull
    @NotEmpty
    private String roleName;

    private String email;

    private String phone;

    private String address;

    @NotNull
    @NotEmpty
    private String login;

    @NotNull
    @NotEmpty
    private String password;
}
