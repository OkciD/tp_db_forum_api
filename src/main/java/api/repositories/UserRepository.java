package api.repositories;

import api.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends AbstractRepository {

    final static private RowMapper<User> userMapper = (resultSet, nRows) ->
            new User(resultSet.getString("nickname"),
                    resultSet.getString("fullname"),
                    resultSet.getString("email"),
                    resultSet.getString("description")
            );

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

    public List<User> getUsersByNicknameOrEmail(String nickname, String email) {
        final String queryString =
                "SELECT \"user\".nickname, \"user\".fullname, \"user\".email, \"user\".description " +
                        "FROM \"user\" " +
                        "WHERE lower(\"user\".nickname) = lower(?) OR lower(\"user\".email) = lower(?)";
        return jdbcTemplate.query(queryString, userMapper, nickname, email);
    }

}
