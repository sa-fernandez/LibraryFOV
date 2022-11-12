import { Dtoloan } from './../model/dtoloan';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Author } from 'src/app/model/author';
import { Book } from 'src/app/model/book';
import { Dtolink } from 'src/app/model/dtolink';
import { Realbook } from '../model/realbook';
import { Loan } from '../model/loan';
import { Dtocopy } from '../model/dtocopy';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private httpOptions = {
    headers: new HttpHeaders({
      "Content-Type": "application/json"
    })
  }

  constructor(private http: HttpClient) { }

  list(): Observable<Book[]> {
    return this.http.get<Book[]>(`http://localhost:8080/book/list`);
  }

  viewBook(id: number): Observable<Book> {
    return this.http.get<Book>(`http://localhost:8080/book/view/${id}`);
  }

  bookAuthors(id: number): Observable<Author[]> {
    return this.http.get<Author[]>(`http://localhost:8080/book/authors/${id}`);
  }

  deleteBook(id: number) {
    return this.http.delete(`http://localhost:8080/book/delete/${id}`);
  }

  createBook(book: Book): Observable<Book> {
    return this.http.post<Book>(`http://localhost:8080/book/create-book`, book, this.httpOptions);
  }

  createAuthor(author: Author): Observable<Author> {
    return this.http.post<Author>(`http://localhost:8080/book/create-author`, author, this.httpOptions);
  }

  linkAuthorBook(idAuthor: number, idBook: number) {
    return this.http.put(`http://localhost:8080/book/link`, new Dtolink(idAuthor, idBook), this.httpOptions);
  }

  listAuthors(): Observable<Author[]> {
    return this.http.get<Author[]>(`http://localhost:8080/book/list-authors`);
  }

  editBook(book: Book) {
    return this.http.put(`http://localhost:8080/book/edit-book`, book, this.httpOptions);
  }

  viewAuthor(name: string): Observable<Author> {
    return this.http.get<Author>(`http://localhost:8080/book/view-author/${name}`);
  }

  editAuthor(author: Author) {
    return this.http.put(`http://localhost:8080/book/edit-author`, author, this.httpOptions);
  }

  deleteAuthor(id: number) {
    return this.http.delete(`http://localhost:8080/book/delete-author/${id}`);
  }

  unlinkAuthorBook(idAuthor: number, idBook: number) {
    return this.http.put(`http://localhost:8080/book/unlink`, new Dtolink(idAuthor, idBook), this.httpOptions);
  }

  createLoan(idCopy: number, idPerson: number, finalDate: string) {
    return this.http.post<Loan>(`http://localhost:8080/book/loan`, new Dtoloan(idCopy, idPerson, finalDate), this.httpOptions);
  }

  listCopies(id: number): Observable<Realbook[]> {
    return this.http.get<Realbook[]>(`http://localhost:8080/book/list-copies/${id}`);
  }

  viewCopy(id: number): Observable<Realbook> {
    return this.http.get<Realbook>(`http://localhost:8080/book/view-copy/${id}`);
  }

  copyBook(id: number): Observable<Book> {
    return this.http.get<Book>(`http://localhost:8080/book/book-copy/${id}`);
  }

  listLoans(): Observable<Loan[]> {
    return this.http.get<Loan[]>(`http://localhost:8080/book/list-loans`);
  }

  listNotBorrowed(id: number): Observable<Realbook[]> {
    return this.http.get<Realbook[]>(`http://localhost:8080/book/not-borrowed/${id}`);
  }

  listLoansPerson(id: number): Observable<Loan[]> {
    return this.http.get<Loan[]>(`http://localhost:8080/book/list-person-loans/${id}`);
  }

  listBooksBorrowed(id: number): Observable<Book[]> {
    return this.http.get<Book[]>(`http://localhost:8080/book/list-borrowed-books/${id}`);
  }

  viewRealBook(id: number): Observable<Realbook> {
    return this.http.get<Realbook>(`http://localhost:8080/book/view-realbook/${id}`);
  }

  viewLoan(id: number): Observable<Loan> {
    return this.http.get<Loan>(`http://localhost:8080/book/view-loan/${id}`);
  }

  deleteLoan(id: number) {
    return this.http.delete(`http://localhost:8080/book/delete-loan/${id}`);
  }

  editLoan(loan: Loan) {
    return this.http.patch(`http://localhost:8080/book/edit-loan`, loan, this.httpOptions);
  }

  createRealBook(realbook: Realbook): Observable<Realbook> {
    return this.http.post<Realbook>(`http://localhost:8080/book/create-copy`, realbook, this.httpOptions);
  }

  linkCopy(idBook: number, idCopy: number) {
    return this.http.put(`http://localhost:8080/book/link-copy`, new Dtocopy(idBook, idCopy), this.httpOptions);
  }

  deleteCopy(id: number) {
    return this.http.delete(`http://localhost:8080/book/delete-copy/${id}`);
  }

}
