package com.wolroys.userservice.service;

import com.wolroys.shopentity.dto.UserCreateEditDto;
import com.wolroys.shopentity.dto.UserDto;
import com.wolroys.shopentity.mapper.UserMapper;
import com.wolroys.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> findAll(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapToDto)
                .toList();
    }

    public Optional<UserDto> findById(Long id){
        return userRepository.findById(id)
                .map(userMapper::mapToDto);
    }

    @Transactional
    public UserDto create(UserCreateEditDto userDto){
        return Optional.of(userDto)
                .map(userMapper::mapToEntity)
                .map(userRepository::saveAndFlush)
                .map(userMapper::mapToDto)
                .orElseThrow();
    }

    @Transactional
    public Optional<UserDto> update(Long id, UserCreateEditDto userEdit){
        return userRepository.findById(id)
                .map(entity -> userMapper.mapUpdate(userEdit, entity))
                .map(userRepository::saveAndFlush)
                .map(userMapper::mapToDto);
    }

    @Transactional
    public boolean delete(Long id){
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    userRepository.flush();
                    return true;
                }).orElse(false);
    }

}


