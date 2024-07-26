package ru.kata.spring.boot_security.demo.services;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.util.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public List<User> findAll() {  // Все пользователи из бд
        return userRepository.findAll();
    }

    public User findOne(Long id) {  // Пользователь по id из бд
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void save(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void update(User user) {
        userRepository.findById(user.getId())
                .map(existingUser -> {
                    user.setEmail(user.getEmail());
                    userRepository.save(user);
                    return existingUser;
                })
                .orElseThrow(UserNotFoundException::new);
    }

    public void delete(long id) {
        userRepository.findById(id)
                .map(existingUser -> {
                    userRepository.delete(existingUser);
                    return id;
                })
                .orElseThrow(UserNotFoundException::new);
    }

}
