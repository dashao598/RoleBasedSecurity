package de.unileipzig.bis.rbs.testApp.controllers;

import de.unileipzig.bis.rbs.testApp.model.Author;
import de.unileipzig.bis.rbs.testApp.model.Book;
import de.unileipzig.bis.rbs.testApp.model.Role;
import de.unileipzig.bis.rbs.testApp.service.AuthorRepository;
import de.unileipzig.bis.rbs.testApp.service.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The book controller to manage books in this application.
 *
 * @author Lukas Werner
 */
@Controller
@RequestMapping("/manage/book")
public class BookController extends AbstractController {

    /**
     * The book repository to persist changes
     */
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;
    /**
     * Get all books
     *
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(method = RequestMethod.GET)
    public String books(Model model) {
        Iterable<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "book/all-books";
    };

    /**
     * Get book by id
     *
     * @param bookid the id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/{bookid}", method = RequestMethod.GET)
    public String book(@PathVariable String bookid, Model model) {
        Book book = bookRepository.findOne(Long.valueOf(bookid));
        model.addAttribute("book", book);
        return "book/book";
    }

    /**
     * Create new book (show mask)
     *
     * @return the book creation mask
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        Iterable<Author> allAuthors = authorRepository.findAll();
        model.addAttribute("authors", allAuthors);
        return "book/create";
    }

    /**
     * Create new book (action)
     *
     * @param isbn the bookname
     * @param title the password
     * @param authorId the name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String doCreate(@RequestParam(value = "isbn") String isbn,
                           @RequestParam(value = "title") String title,
                           @RequestParam(value = "author_id") Long authorId) {

        Author author = authorRepository.findOne(Long.valueOf(authorId));
        bookRepository.save(new Book(isbn, title, author));
        return "redirect:/manage/book";
    }

    /**
     * Edit existing book (show mask)
     *
     * @param bookid the book id
     * @param model the ui model
     * @return the view
     */
    @RequestMapping(value = "/edit/{bookid}", method = RequestMethod.GET)
    public String edit(@PathVariable String bookid, Model model) {
        Book book = bookRepository.findOne(Long.valueOf(bookid));
        model.addAttribute("book", book);
        Iterable<Author> allAuthors = authorRepository.findAll();
        model.addAttribute("authors", allAuthors);
        return "book/edit";
    }

    /**
     * Edit existing book (action)
     *
     * @param bookid the book id
     * @param isbn the new bookname
     * @param title the new password
     * @param authorId the new name
     * @return the view (redirect)
     */
    @RequestMapping(value = "/edit/{bookid}", method = RequestMethod.POST)
    public String doEdit(@PathVariable String bookid,
                         @RequestParam(value = "isbn") String isbn,
                         @RequestParam(value = "title") String title,
                         @RequestParam(value = "author_id") Long authorId) {
        Book book = bookRepository.findOne(Long.valueOf(bookid));
        Author author = authorRepository.findOne(Long.valueOf(authorId));
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setAuthor(author);
        bookRepository.save(book);
        return "redirect:/manage/book/" + bookid;
    }

    /**
     * Delete existing book
     *
     * @param bookid the book id
     * @return the view (redirect)
     */
    @RequestMapping(value = "/delete/{bookid}", method = RequestMethod.GET)
    public String delete(@PathVariable String bookid) {
        bookRepository.delete(Long.valueOf(bookid));
        return "redirect:/manage/book";
    }

}