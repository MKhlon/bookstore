package com.bookstore.converter;

import static org.springframework.beans.BeanUtils.copyProperties;

public class Converter {

    protected void convert(Object source, Object target) {
        copyProperties(source, target);
    }
}
