package mx.conacyt.reports;

import java.io.Serializable;
import java.util.Set;

import mx.conacyt.reports.annotations.SheetColumn;

/**
 * A DTO representing a user, with his authorities.
 */
class UserDto implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @SheetColumn("ID")
    private String id;

    @SheetColumn
    private String login;

    private String firstName;

    private String lastName;

    private String imageUrl;

    @SheetColumn
    private String langKey;

    private String createdBy;

    @SheetColumn
    private String createdDate;

    @SheetColumn
    private String lastModifiedBy;

    @SheetColumn
    private String lastModifiedDate;

    @SheetColumn
    private Set<String> authorities;

    @SheetColumn
    private Genre genre;

    @SheetColumn
    private String lastLogin;

    @SheetColumn
    private Integer followers;

    public UserDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLangKey() {
        return langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    enum Genre {
        MALE, FEMALE
    }

    @Override
    public String toString() {
        return "UserDTO{" + "login='" + login + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
                + ", imageUrl='" + imageUrl + '\'' + ", langKey='" + langKey + '\'' + ", createdBy=" + createdBy
                + ", createdDate=" + createdDate + ", lastModifiedBy='" + lastModifiedBy + '\'' + ", lastModifiedDate="
                + lastModifiedDate + ", authorities=" + authorities + "}";
    }
}