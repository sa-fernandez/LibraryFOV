import { Component, Input, OnChanges } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from 'src/app/shared/book.service';
import { Loan } from 'src/app/model/loan';
import { Book } from 'src/app/model/book';
import { Realbook } from 'src/app/model/realbook';

@Component({
  selector: 'app-loan-list',
  templateUrl: './loan-list.component.html',
  styleUrls: ['./loan-list.component.scss']
})
export class LoanListComponent implements OnChanges {

  @Input()
  loansReceived : Loan[] | undefined;

  public booksBorrowed : Book[] = [];

  realBook : Realbook | undefined;

  constructor(
    private bookService : BookService, 
    private route : ActivatedRoute, 
    private router : Router
  ) { }

  ngOnChanges(): void {

    this.booksBorrowed = [];

    this.loansReceived?.forEach(elem =>
      this.bookService.viewRealBook(elem.id).subscribe(real =>{
        this.realBook = real
        this.bookService.copyBook(this.realBook.id).subscribe(book => {
          this.booksBorrowed.push(book)
        })
      }));
  }

}
