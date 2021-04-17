package main.service;

import main.model.Role;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user){
        User userFromDBByName = userRepository.findByUsername(user.getUsername());
        User userFromDBByEmail = userRepository.findByEmail(user.getEmail());
        if(userFromDBByName != null || userFromDBByEmail != null){
            return false;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepository.save(user);

        if(!user.getEmail().isEmpty() && user.getEmail() != null){
            String message = String.format("" +
                    "Hello, %s!\n" +
                    "Welcome to BookShelf. Please visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode());

            mailSender.send(user.getEmail(), "Activation code", message);
        }
        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);

        if(user == null){
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);

        return true;
    }

    public boolean changeData(User user, String username, String password){
        boolean nameChanged = false;
        boolean passwordChanged = false;

        if(username != null && !username.isEmpty()){
            user.setUsername(username);
            nameChanged = true;
        }
        if(password != null && !password.isEmpty()){
            user.setPassword(password);
            passwordChanged = true;
        }

        if(nameChanged || passwordChanged){
            return true;
        } else {
            return false;
        }
    }
}
