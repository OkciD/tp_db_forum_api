package api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Forum {
    private String title;
    private String slug;
    private String user;
    private Long threads;
    private Long posts;

    @JsonCreator
    public Forum(
            @JsonProperty("title") String title,
            @JsonProperty("slug") String slug,
            @JsonProperty("user") String user,
            @JsonProperty("threads") Long threads,
            @JsonProperty("posts") Long posts) {
        this.title = title;
        this.slug = slug;
        this.user = user;
        this.threads = threads;
        this.posts = posts;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getUser() {
        return user;
    }

    public Long getThreads() {
        return threads;
    }

    public Long getPosts() {
        return posts;
    }
}
