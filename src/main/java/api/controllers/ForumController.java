package api.controllers;

import api.models.Forum;
import api.repositories.ForumRepository;
import api.responses.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

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
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Message(Message.CANT_FIND_USER, forumData.getUser())
            );
        }
    }

    @GetMapping(path = "/{slug}/details")
    public ResponseEntity getForumBySlug(@PathVariable String slug) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(forumRepository.getForumBySlug(slug));
        } catch (EmptyResultDataAccessException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Message(Message.CANT_FIND_FORUM, slug)
            );
        }
    }
}
