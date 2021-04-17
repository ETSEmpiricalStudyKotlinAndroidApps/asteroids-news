package main.repository;

import main.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitleOrAuthor(String title, String author);
    Book findById(long id);

}
