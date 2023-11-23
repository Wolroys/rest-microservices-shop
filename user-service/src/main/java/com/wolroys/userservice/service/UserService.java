package com.wolroys.userservice.service;

import com.wolroys.shopentity.dto.AuthDto;
import com.wolroys.shopentity.dto.UserCreateEditDto;
import com.wolroys.shopentity.dto.UserDto;
import com.wolroys.shopentity.entity.Role;
import com.wolroys.shopentity.entity.User;
import com.wolroys.shopentity.mapper.UserMapper;
import com.wolroys.userservice.jwt.JwtUtil;
import com.wolroys.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

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
                .map(entity -> {
                    entity.setPassword(passwordEncoder.encode(entity.getPassword()));
                    return entity;
                })
                .map(userRepository::saveAndFlush)
                .map(userMapper::mapToDto)
                .orElseThrow();
    }

    @Transactional
    public String register(UserCreateEditDto userDto){
        User user = Optional.of(userDto)
                .map(userMapper::mapToEntity)
                .map(entity -> {
                    entity.setPassword(passwordEncoder.encode(entity.getPassword()));
                    entity.setRole(Role.USER);
                    return entity;
                })
                .map(userRepository::saveAndFlush)
                .orElseThrow();

        return jwtUtil.generateToken(loadUserByUsername(user.getUsername()), String.valueOf(user.getId()));
    }

    public String login(AuthDto authDto){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(),
                        authDto.getPassword()));
        if (authentication.isAuthenticated()){
            UserDetails userDetails = loadUserByUsername(authDto.getUsername());
            User user = userRepository.findByUsername(authDto.getUsername()).get();
            return jwtUtil.generateToken(userDetails, String.valueOf(user.getId()));
        } else
            throw new RuntimeException("invalid access");
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
                mapRolesToAuthorities(user.getRole()));
    }

    public boolean isExist(String username){
        return userRepository.existsByUsername(username);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Role role){
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }
}


