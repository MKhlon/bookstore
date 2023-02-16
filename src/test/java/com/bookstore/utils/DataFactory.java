package com.bookstore.utils;

import com.bookstore.dto.ProductDto;
import com.bookstore.dto.UserDto;
import com.bookstore.model.enums.RoleType;


public class DataFactory {

    public static ProductDto getProduct() {
        return ProductDto.builder()
                .name("Test Product")
                .author("Test author")
                .description("This is a test description product")
                .price(19.99F)
                .imagePath("http://www.test.image.jpg")
                .build();
    }

    public static UserDto getUser() {
        return UserDto.builder()
                .userName("Test User Name")
                .roleId(1)
                .roleName(RoleType.ADMIN.name())
                .email("test.email@gmail.com")
                .address("Washington DC")
                .phone("+44111222333")
                .login("test login value")
                .password("test password value")
                .build();
    }
}