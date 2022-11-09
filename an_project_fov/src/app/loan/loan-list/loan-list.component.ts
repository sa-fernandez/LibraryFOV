import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from 'src/app/shared/book.service';
import { Loan } from 'src/app/model/loan';
import { Book } from 'src/app/model/book';
import { Realbook } from 'src/app/model/realbook';
import { SecurityService } from 'src/app/shared/security.service';
import { Author } from 'src/app/model/author';

@Component({
  selector: 'app-loan-list',
  templateUrl: './loan-list.component.html',
  styleUrls: ['./loan-list.component.scss']
})
export class LoanListComponent implements OnInit {

  loans: Loan[] | undefined;

  public booksBorrowed: Book[] = [];

  realBook: Realbook | undefined;
  person: Author | undefined;

  constructor(
    private bookService: BookService,
    private securityService: SecurityService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.bookService.viewAuthor(this.securityService.userName()).subscribe(elem => {
      this.person = elem
      this.bookService.listLoansPerson(this.person.id).subscribe(loans => {
        this.loans = loans
        this.loans?.forEach(elem =>
          this.bookService.viewRealBook(elem.id).subscribe(real => {
            this.realBook = real
            this.bookService.copyBook(this.realBook.id).subscribe(book => {
              this.booksBorrowed.push(book)
            })
          }));
      });
    })
  }

}
