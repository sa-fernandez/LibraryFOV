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
  editBook : Book = new Book(0, "", "");

  inputName : string | undefined = "";
  inputIsbn : string | undefined = "";
  inputAuthors : string | undefined = "";

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
    });
  }
  
  onSubmit(){
    if(this.book && this.inputName && this.inputIsbn){
      this.editBook.id = this.book.id;
      this.editBook.name = this.inputName;
      this.editBook.isbn = this.inputIsbn;
      this.bookService.editBook(this.editBook).subscribe(() => {
        this.router.navigate(['book/view', this.book?.id]);
      });
    }
  }

  onCancel(){
    this.router.navigate(['book/view', this.book?.id]);
  }

}
