package main.repository;

import main.model.Book;
import main.model.ListsOfBooks;
import main.model.UsersBook;
import main.model.UsersBookKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsersBookRepository extends CrudRepository<UsersBook, UsersBookKey> {
    List<UsersBook> findAll();

    UsersBook findByKey(UsersBookKey key);

//    @Query("DELETE FROM UsersBook " +
//            "WHERE userId = :userId " +
//            "AND bookId = :bookId")
//    void delete(long bookId, long userId);

    @Query("SELECT book FROM Book as book " +
            "JOIN UsersBook as usersBook ON usersBook.bookId = book.id " +
            "WHERE usersBook.list = :list " +
            "AND usersBook.userId = :userId " +
            "AND book.title = (CASE WHEN :title != '' THEN :title ELSE book.title END) " +
            "AND book.author = (CASE WHEN :author != '' THEN :author ELSE book.author END)")
    List<Book> findByTitleOrAuthorAndList(String title, String author, long userId, ListsOfBooks list);
}
