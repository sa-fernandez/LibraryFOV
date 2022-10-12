import { Book } from './../../model/book';
import { Component, OnInit } from '@angular/core';
import { BookService } from '../../shared/book.service';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {

  selection : boolean[] = [];
  books : Book[] | undefined;
  
  constructor(private bookService: BookService) { }

  ngOnInit(): void {
    this.bookService.list().subscribe(books => this.books = books);
  }
}
