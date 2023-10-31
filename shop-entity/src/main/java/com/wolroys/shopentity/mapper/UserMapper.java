package com.wolroys.shopentity.mapper;

import com.wolroys.shopentity.dto.ProductCreateEditDto;
import com.wolroys.shopentity.dto.ProductDto;
import com.wolroys.shopentity.dto.UserCreateEditDto;
import com.wolroys.shopentity.dto.UserDto;
import com.wolroys.shopentity.entity.Product;
import com.wolroys.shopentity.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserDto mapToDto(User user){
        return user == null ? null : modelMapper.map(user, UserDto.class);
    }

    public User mapToEntity(UserCreateEditDto userDto){
        return userDto == null ? null : modelMapper.map(userDto, User.class);
    }

    public User mapUpdate(UserCreateEditDto source, User destinationClass){
        modelMapper.map(source, destinationClass);
        return destinationClass;
    }
}
