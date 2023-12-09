package com.wolroys.userservice.service;

import com.wolroys.shopentity.dto.AuthDto;
import com.wolroys.shopentity.dto.UserCreateEditDto;
import com.wolroys.shopentity.dto.UserDto;
import com.wolroys.shopentity.entity.Role;
import com.wolroys.shopentity.entity.User;
import com.wolroys.shopentity.mapper.UserMapper;
import com.wolroys.userservice.jwt.JwtUtil;
import com.wolroys.userservice.repository.UserRepository;
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

import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final EmailServiceImpl emailService;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
                       @Lazy AuthenticationManager authenticationManager,
                       EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
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
                    entity.setActivationCode(UUID.randomUUID().toString());
                    String message = String.format(
                            "Hello, %s! \n" +
                                    "Welcome to spring-shop. Please, visit next link: http://localhost:8765/auth/activate/%s",
                            entity.getUsername(),
                            entity.getActivationCode()
                    );
                    emailService.sendSimpleEmail(entity.getEmail(), "Account activation", message);
                    return entity;
                })
                .map(userRepository::saveAndFlush)
                .orElseThrow();


        return jwtUtil.generateToken(loadUserByUsername(user.getUsername()), user.getUsername());
    }

    public String login(AuthDto authDto){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDto.getUsername(),
                        authDto.getPassword()));
        if (authentication.isAuthenticated()){
            UserDetails userDetails = loadUserByUsername(authDto.getUsername());
            User user = userRepository.findByUsername(authDto.getUsername()).get();
            return jwtUtil.generateToken(userDetails, user.getUsername());
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

    @Transactional
    public boolean activateUser(String code){
        Optional<User> user = userRepository.findByActivationCode(code);

        if (user.isEmpty())
            return false;

        user.get().setActivationCode(null);
        userRepository.saveAndFlush(user.get());

        return true;
    }

    public boolean isTaken(String email){
        return userRepository.existsByEmail(email);
    }

    public boolean isActive(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && user.get().getActivationCode() != null;
    }
}


