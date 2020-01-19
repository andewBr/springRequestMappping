package book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/{userId}/books")
class BookRestController {

    private final BookRepository bookRepository;

    private final AccountRepository accountRepository;

    @Autowired
    BookRestController(BookRepository bookRepository,
                       AccountRepository accountRepository) {
        this.bookRepository = bookRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "/removeBook/{bookId}", method = RequestMethod.DELETE)
    ResponseEntity<?> remove(@PathVariable Long bookId) {
        Book result = this.bookRepository.getOne(bookId);
        this.bookRepository.delete(result);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("")
                .buildAndExpand(result.getId()).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@PathVariable String userId, @RequestBody Book input) {
        this.validateUser(userId);
        return this.accountRepository
                .findByUsername(userId)
                .map(account -> {
                    bookRepository.save(new Book(account, input.name,
                            input.author, "description"));

                    return new ResponseEntity<>(null, HttpStatus.CREATED);
                }).get();

    }

    @RequestMapping(value = "/{bookId}", method = RequestMethod.GET)
    Optional<Book> readBookmark(@PathVariable String userId, @PathVariable Long bookId) {
        this.validateUser(userId);
        return this.bookRepository.findById(bookId);
    }

    @RequestMapping(method = RequestMethod.GET)
    Collection<Book> readBookmarks(@PathVariable String userId) {
        this.validateUser(userId);
        return this.bookRepository.findByAccountUsername(userId);
    }

    private void validateUser(String userId) {
        this.accountRepository.findByUsername(userId).orElseThrow(
                () -> new UserNotFoundException(userId));
    }
}