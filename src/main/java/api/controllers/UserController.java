package api.controllers;

import api.models.User;
import api.repositories.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @GetMapping(path = "{nickname}/profile")
    public ResponseEntity getUserByNickname(@PathVariable String nickname) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.getUserByNickname(nickname));
        } catch (EmptyResultDataAccessException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
    }

    @PostMapping(path = "{nickname}/profile")
    public ResponseEntity updateUserData(@PathVariable String nickname, @RequestBody User userData) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.updateUserData(
                    nickname,
                    userData.getFullname(),
                    userData.getEmail(),
                    userData.getAbout()
            ));
        } catch (DuplicateKeyException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        } catch (EmptyResultDataAccessException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
    }
}
