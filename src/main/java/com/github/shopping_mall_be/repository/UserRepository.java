package com.github.shopping_mall_be.repository;

import com.github.shopping_mall_be.dto.UserDto;

import java.util.List;

public interface UserRepository {

    UserDto save(UserDto userDto);
    public List<UserDto> findAll();
    UserDto findByEmail(String email);
    public void deleteByEmail(String email);
}
