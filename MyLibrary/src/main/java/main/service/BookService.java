package main.service;

import main.model.Book;
import main.model.ListsOfBooks;
import main.model.User;
import main.model.UsersBook;
import main.model.UsersBookKey;
import main.repository.BookRepository;
import main.repository.UserRepository;
import main.repository.UsersBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UsersBookRepository usersBookRepository;

    public boolean addToList(Book book, User user, String targetList){
        ListsOfBooks list = getList(targetList);
        if(!usersBookRepository.existsById(new UsersBookKey(book, user))) {
            UsersBook usersBook = new UsersBook(new UsersBookKey(book, user), list);
            usersBookRepository.save(usersBook);
            return true;
        }
        return false;
    }

    public void changeList(Book book, User user, String newList) {
        UsersBook usersBook = usersBookRepository.findByKey(new UsersBookKey(book, user));
        usersBook.setList(getList(newList));
        usersBookRepository.save(usersBook);
    }

    public ListsOfBooks getList(String list){
        switch (list){
            case "finished":
                return ListsOfBooks.FINISHED;
            case "wantToRead":
                return ListsOfBooks.WANT_TO_READ;
            case "isReading":
                return ListsOfBooks.IS_READING;
            default:
                return ListsOfBooks.UNDEFINED;
        }
    }
}
