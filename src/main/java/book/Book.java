package book;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Book {

    public String name;
    public String author;
    public String description;
    public String uri;
    @JsonIgnore
    @ManyToOne
    private Account account;
    @Id
    @GeneratedValue
    private Long id;

    Book() { // jpa only
    }

    public Book(Account account, String name, String author, String description) {
        this.name = name;
        this.author = author;
        this.account = account;
        this.description = description;
        this.uri = "http://localhost:8080/" + account.username + "/books/" + getId() + "";
    }

    public Account getAccount() {
        return account;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }
}