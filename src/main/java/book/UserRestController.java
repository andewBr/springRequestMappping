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
@RequestMapping("/user")
class UserRestController {

    private final AccountRepository accountRepository;

    @Autowired
    UserRestController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    ResponseEntity<?> add(@RequestBody Account account) {
        Account result = this.accountRepository.save(account);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("")
                .buildAndExpand(result.getId()).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);

    }

    @RequestMapping(value = "/allUsers", method = RequestMethod.GET)
    Collection<Account> readUsers() {
        return this.accountRepository.findAll();
    }

    @RequestMapping(value = "/removeUser/{userId}", method = RequestMethod.DELETE)
    ResponseEntity<?> remove(@PathVariable String userId) {
        Optional<Account> result = this.accountRepository.findByUsername(userId);
        this.accountRepository.delete(result.get());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(ServletUriComponentsBuilder
                .fromCurrentRequest().path("")
                .buildAndExpand(result.get().getId()).toUri());
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.OK);
    }
}