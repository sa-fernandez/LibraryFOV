import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Author } from 'src/app/model/author';
import { Book } from 'src/app/model/book';
import { Dtolink } from 'src/app/model/dtolink';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private httpOptions = {
    headers : new HttpHeaders({
      "Content-Type" : "application/json"
    })
  }
  
  constructor(private http : HttpClient) { }

  list() : Observable<Book[]>{
    return this.http.get<Book[]>(`http://localhost:8080/book/list`)
  }

  viewBook(id : number): Observable<Book>{
    return this.http.get<Book>(`http://localhost:8080/book/view/${id}`);
  }

  bookAuthors(id : number) : Observable<Author[]>{
    return this.http.get<Author[]>(`http://localhost:8080/book/authors/${id}`);
  }

  deleteBook(id : number){
    return this.http.delete(`http://localhost:8080/book/delete/${id}`);
  }

  createBook(book : Book){
    return this.http.post<Book>(`http://localhost:8080/book/create-book`, book, this.httpOptions);
  }

  createAuthor(author : Author){
    return this.http.post<Author>(`http://localhost:8080/book/create-author`, author, this.httpOptions);
  }

  linkAuthorBook(idAuthor : number, idBook : number){
    return this.http.put(`http://localhost:8080/book/link`, new Dtolink(idAuthor, idBook), this.httpOptions); 
  }

  listAuthors() : Observable<Author[]>{
    return this.http.get<Author[]>(`http://localhost:8080/book/list-authors`);
  }

  editBook(book : Book){
    return this.http.put(`http://localhost:8080/book/edit-book`, book, this.httpOptions);
  }

  editAuthor(author : Author){
    return this.http.put(`http://localhost:8080/book/edit-author`, author, this.httpOptions);
  }

  deleteAuthor(id : number){
    return this.http.delete(`http://localhost:8080/book/delete-author/${id}`);
  }

  unlinkAuthorBook(idAuthor : number, idBook : number){
    return this.http.put(`http://localhost:8080/book/unlink`, new Dtolink(idAuthor, idBook), this.httpOptions); 
  }

}
