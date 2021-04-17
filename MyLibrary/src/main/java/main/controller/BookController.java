package main.controller;

import main.model.Book;
import main.model.ListsOfBooks;
import main.model.Note;
import main.model.User;
import main.repository.BookRepository;
import main.repository.NoteRepository;
import main.repository.UserRepository;
import main.repository.UsersBookRepository;
import main.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class BookController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UsersBookRepository usersBookRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private BookService bookService;



    @GetMapping("/")
    public String greeting(Map<String, Object> model){
        return "greeting";
    }

    @GetMapping("/books")
    public String getBooks(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            Model model){

        List<Book> booksWhichUserWantToRead = usersBookRepository.findByTitleOrAuthorAndList(title, author, user.getId(), ListsOfBooks.WANT_TO_READ);
        List<Book> booksWhichUserIsReading = usersBookRepository.findByTitleOrAuthorAndList(title, author, user.getId(), ListsOfBooks.IS_READING);
        List<Book> booksWhichUserFinished = usersBookRepository.findByTitleOrAuthorAndList(title, author, user.getId(), ListsOfBooks.FINISHED);

        model.addAttribute("user", user);
        model.addAttribute("wantToRead", booksWhichUserWantToRead);
        model.addAttribute("isReading", booksWhichUserIsReading);
        model.addAttribute("finished", booksWhichUserFinished);
        return "books";
    }

    @GetMapping("/books/{id}")
    public String getBook(
            @RequestParam int id,
            Model model){
        Book book = bookRepository.findById(id);
        model.addAttribute("book", book);
        model.addAttribute("notes", book.getNotes());
        return "book";
    }

    @PostMapping("/books")
    public String changeList(
            @AuthenticationPrincipal User user,
            @RequestParam int id,
            @RequestParam String newList){
        Book book = bookRepository.findById(id);
        bookService.changeList(book, user, newList);
        return "redirect:/books";
    }

    @PostMapping("/books/{id}")
    public String sendNote(
            @RequestParam int id,
            @RequestParam String text){
        Note note = new Note(text, bookRepository.findById(id));
        noteRepository.save(note);
        return "redirect:/books/{id}";
    }
}
