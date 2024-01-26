package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.domain.User;
import com.lukasz.auctionhouse.repositories.RoleRepository;
import com.lukasz.auctionhouse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if(userOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        User user = userOptional.get();

        if(!user.getEnabled()){
            throw new UsernameNotFoundException(username);
        }

        return convertToUserDetails(user);
    }

    private UserDetails convertToUserDetails(User user) {
        var grantedAuthorities = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().toString()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

    @Override
    public Optional<User> validUserNameAndPassword(String username, String password) {
        return getByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }


    @Override
    public Boolean hasUserWithUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.isPresent();
    }

    @Override
    public Boolean hasUserWithEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.isPresent();
    }

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByName("ROLE_USER"))));
        return userRepository.save(user);
    }
}
