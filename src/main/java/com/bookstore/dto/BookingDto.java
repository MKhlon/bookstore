package com.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    @NotNull
    private Integer id;

    @NotNull
    public Integer productId;

    @NotEmpty
    @NotNull
    private String productName;

    @NotEmpty
    @NotNull
    private Integer userId;

    @NotEmpty
    @NotNull
    private String userName;

    @NotEmpty
    @NotNull
    private String deliveryAddress;

    @NotEmpty
    @NotNull
    private LocalDate date;

    @NotEmpty
    @NotNull
    @JsonFormat(pattern = "HH:mm:ss.SSSSSSSSS")
    private LocalTime time;

    @NotEmpty
    @NotNull
    private Integer statusId;

    @NotEmpty
    @NotNull
    private String statusName;

    @NotEmpty
    @NotNull
    private Integer quantity;
}
