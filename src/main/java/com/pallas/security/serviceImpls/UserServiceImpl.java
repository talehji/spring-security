package com.pallas.security.serviceImpls;

import com.pallas.security.entities.User;
import com.pallas.security.models.Response;
import com.pallas.security.repositories.UserRepository;
import com.pallas.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : Pallas
 * @project : Security
 * @created on : 16-November-2022
 * @email : talehji@gmail.com
 */

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Response add(User user) {
        return null;
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    user.get().getUsername(),
                    user.get().getPassword(),
                    user.get().getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList())
            );
        }
        throw new UsernameNotFoundException("User not found");
    }
}
