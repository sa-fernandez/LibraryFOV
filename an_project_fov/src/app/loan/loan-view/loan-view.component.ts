import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Author } from 'src/app/model/author';
import { Book } from 'src/app/model/book';
import { Realbook } from 'src/app/model/realbook';
import { BookService } from 'src/app/shared/book.service';
import { Loan } from 'src/app/model/loan';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';

@Component({
  selector: 'app-loan-view',
  templateUrl: './loan-view.component.html',
  styleUrls: ['./loan-view.component.scss']
})

export class LoanViewComponent implements OnInit {

  @Input()
  loanID: number | undefined;

  minDate: Date = new Date();
  maxDate: Date = new Date();
  now: Date = new Date();

  isDisabled: boolean = false;

  selectedAuthor: Author | undefined;
  inputName: string = "";
  event: string | undefined = "";

  book: Book | undefined;
  realbook: Realbook | undefined;
  loan: Loan = new Loan(0, "", "");
  author: Author = new Author(0, "");

  authors: Author[] | undefined;

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.maxDate.setMonth(this.maxDate.getMonth() + 1);
    this.bookService.viewLoan(this.loanID!).subscribe((loan) => {
      this.loan = loan
      this.bookService.viewRealBook(loan.id).subscribe(realbook => {
        this.realbook = realbook
        this.bookService.copyBook(this.realbook.id).subscribe(book => {
          this.book = book
          this.bookService.bookAuthors(book.id).subscribe(authors => this.authors = authors)
        })
      })
    });
  }

  formatDate(date: string) {
    let split = date.split("/");
    return split[1] + "/" + split[0] + "/" + split[2];
  }

  addEvent(event: MatDatepickerInputEvent<Date>) {
    if (event.value) {
      this.event = this.formatDate(event.value.toLocaleDateString());
    }
  }

  returnLoan() {
    this.bookService.deleteLoan(this.loan?.id!).subscribe(() => {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['book/loan-list'])
      });
    });
  }

  editDate() {
    if (!this.event) {
      this.event = this.now.toLocaleDateString();
    }
    this.loan.finalDate = this.event;
    this.bookService.editLoan(this.loan).subscribe();
  }

}
