package api.controllers;

import api.models.Forum;
import api.repositories.ForumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequestMapping(path = "api/forum")
public class ForumController {
    private ForumRepository forumRepository;

    @Autowired
    public ForumController(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }


    @PostMapping(path = "/create")
    public ResponseEntity createForum(@RequestBody Forum forumData) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(forumRepository.createForum(
                    forumData.getTitle(),
                    forumData.getSlug(),
                    forumData.getUser()
                    ));
        } catch (DuplicateKeyException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    forumRepository.getForumBySlug(forumData.getSlug())
            );
        } catch (DataAccessException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
    }
}
