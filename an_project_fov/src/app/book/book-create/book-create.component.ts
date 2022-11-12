import { Component, OnInit } from '@angular/core';
import { Book } from 'src/app/model/book';
import { Router } from '@angular/router';
import { BookService } from '../../shared/book.service';

@Component({
  selector: 'app-book-create',
  templateUrl: './book-create.component.html',
  styleUrls: ['./book-create.component.scss']
})
export class BookCreateComponent implements OnInit {

  book: Book = new Book(0, "", "", "", "", "");

  constructor(
    private bookService: BookService,
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  onSubmit() {
    if (this.book.name && this.book.isbn && this.book.category && this.book.editorial && this.book.description) {
      this.bookService.createBook(this.book).subscribe(() => {
        this.router.navigate(['book/list']);
      });
    }
  }

}
