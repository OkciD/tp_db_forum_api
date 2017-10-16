package api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String nickname;
    private String fullname;
    private String email;
    private String about;

    @JsonCreator
    public User(@JsonProperty("nickname") String nickname,
                @JsonProperty("fullname") String fullname,
                @JsonProperty("email") String email,
                @JsonProperty("about") String about) {
        this.nickname = nickname;
        this.fullname = fullname;
        this.email = email;
        this.about = about;
    }

    public String getNickname() {
        return nickname;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getAbout() {
        return about;
    }

}
