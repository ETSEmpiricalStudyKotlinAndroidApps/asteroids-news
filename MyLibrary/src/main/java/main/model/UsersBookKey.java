package main.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import main.model.Book;
import main.model.User;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@Embeddable
public class UsersBookKey implements Serializable {

    @OneToOne
    private Book book;
    @OneToOne
    private User user;

    public UsersBookKey() {
    }

    public UsersBookKey(Book book, User user) {
        this.book = book;
        this.user = user;
    }
}
