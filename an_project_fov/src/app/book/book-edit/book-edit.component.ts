import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { Book } from 'src/app/model/book';
import { BookService } from '../../shared/book.service';

@Component({
  selector: 'app-book-edit',
  templateUrl: './book-edit.component.html',
  styleUrls: ['./book-edit.component.scss']
})
export class BookEditComponent implements OnInit {

  book : Book | undefined;
  editBook : Book | undefined;

  inputName : string | undefined = "";
  inputIsbn : string | undefined = "";
  inputCategory : string | undefined = "";
  inputDescription : string | undefined = "";
  inputEditorial : string | undefined = "";

  constructor(
    private bookService : BookService, 
    private route : ActivatedRoute, 
    private router : Router
  ) { }

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => this.bookService.viewBook(+params.get('id')!))
    ).subscribe(book => {
      this.book = book
      this.inputName = book.name
      this.inputIsbn = book.isbn
      this.inputCategory = book.category
      this.inputDescription = book.description
      this.inputEditorial = book.editorial
    });
  }
  
  onSubmit(){
    if(this.book && this.inputName && this.inputIsbn && this.inputCategory && this.inputCategory && this.inputEditorial && this.inputDescription){

      this.editBook = new Book(this.book.id, this.inputName, this.inputIsbn, this.inputCategory, this.inputDescription, this.inputEditorial);
      
      this.bookService.editBook(this.editBook).subscribe(() => {
        console.log(this.editBook);
        this.router.navigate(['book/view', this.book?.id]);
      });
    }
  }

  onCancel(){
    this.router.navigate(['book/view', this.book?.id]);
  }

}
