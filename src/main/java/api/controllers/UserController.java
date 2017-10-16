package api.controllers;

import api.models.User;
import api.repositories.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/user")
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/{nickname}/create")
    public ResponseEntity createUser(@PathVariable String nickname, @RequestBody User userData) {
        try {
            User newUser = userRepository.createUser(nickname, userData.getFullname(), userData.getEmail(),
                    userData.getAbout());
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (DuplicateKeyException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    userRepository.getUsersByNicknameOrEmail(nickname, userData.getEmail())
            );
        }
    }
}
