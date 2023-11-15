package com.wolroys.userservice.service;

import com.wolroys.shopentity.dto.UserCreateEditDto;
import com.wolroys.shopentity.dto.UserDto;
import com.wolroys.shopentity.entity.Role;
import com.wolroys.shopentity.entity.User;
import com.wolroys.shopentity.mapper.UserMapper;
import com.wolroys.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

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

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User '%s' wasn't found", username)));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                Collections.singleton(user.getRole()));
    }
}


