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
                    resultSet.getString("moderator"),
                    resultSet.getLong("n_threads"),
                    resultSet.getLong("n_posts")
            );

    @Autowired
    protected ForumRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Forum createForum(String title, String slug, String moderator) {
        return jdbcTemplate.queryForObject(
                "INSERT INTO forum (title, slug, moderator) " +
                        "VALUES (?, ?, " +
                        "(SELECT \"user\".nickname FROM \"user\" WHERE lower(\"user\".nickname) = lower(?)))" +
                        "RETURNING *",
                FORUM_ROW_MAPPER,
                title,
                slug,
                moderator
        );
    }

    public Forum getForumBySlug(String slug) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM forum WHERE lower(forum.slug) = lower(?)",
                FORUM_ROW_MAPPER,
                slug
        );
    }
}
