package api.repositories;

import api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends AbstractRepository {

    @Autowired
    protected UserRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public User createUser(String nickname, String fullName, String email, String description) {
        User newUser = new User(nickname, fullName, email, description);
        this.jdbcTemplate.update(
                "INSERT INTO \"user\" (nickname, fullname, email, description) VALUES (?, ?, ?, ?)",
                newUser.getNickname(),
                newUser.getFullname(),
                newUser.getEmail(),
                newUser.getAbout()
        );
        return newUser;
    }
}
