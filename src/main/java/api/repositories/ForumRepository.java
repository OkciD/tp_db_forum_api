package api.repositories;

import api.models.Forum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ForumRepository extends AbstractRepository {
    final static private RowMapper<Forum> FORUM_ROW_MAPPER = (resultSet, nRows) ->
            new Forum(resultSet.getString("title"),
                    resultSet.getString("slug"),
                    resultSet.getString("user"),
                    resultSet.getLong("threads"),
                    resultSet.getLong("posts")
            );

    @Autowired
    protected ForumRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Forum createForum(String title, String slug, String moderator) {
        Forum newForum = new Forum(title, slug, moderator, 0L, 0L);
        jdbcTemplate.update(
                "INSERT INTO forum (title, slug, noderator) " +
                        "VALUES (?, ?, " +
                        "(SELECT \"user\".nickname FROM \"user\" WHERE lower(\"user\".nickname) = lower(?)))",
                newForum.getTitle(),
                newForum.getSlug(),
                newForum.getUser()
        );
        return newForum;
    }

    public Forum getForumBySlug(String slug) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM forum WHERE forum.slug = ?",
                FORUM_ROW_MAPPER,
                slug
        );
    }
}
