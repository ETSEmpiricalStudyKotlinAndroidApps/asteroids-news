package main.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@Entity
@Table(name = "users_books")
public class UsersBook implements Serializable {

    @EmbeddedId
    private UsersBookKey key;

    @Column(name = "book_id", updatable = false, insertable = false)
    private long bookId;
    @Column(name = "user_id", updatable = false, insertable = false)
    private long userId;

    @Enumerated(EnumType.STRING)
    private ListsOfBooks list;

    @Column(name = "date_added")
    private Date dateAdded;

    public UsersBook() {
    }

    public UsersBook(UsersBookKey key, ListsOfBooks list) {
        this.key = key;
        this.list = list;
        this.dateAdded = new Date(System.currentTimeMillis());
    }
}
