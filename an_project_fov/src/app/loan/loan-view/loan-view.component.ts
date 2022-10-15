import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Author } from 'src/app/model/author';
import { Book } from 'src/app/model/book';
import { switchMap } from 'rxjs';
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

  minDate : Date = new Date();
  maxDate : Date = new Date();

  isDisabled : boolean = false;

  selectedAuthor : Author | undefined;
  inputName : string = "";
  event : string | undefined = "";

  book : Book | undefined;
  realbook : Realbook | undefined;
  loan : Loan = new Loan(0, "", "");
  author : Author = new Author(0, "");

  authors : Author[] | undefined;

  constructor(
    private bookService : BookService, 
    private route : ActivatedRoute, 
    private router : Router
  ) { }

  ngOnInit(): void {
    this.maxDate.setMonth(this.maxDate.getMonth() + 1);
    this.route.paramMap.pipe(
      switchMap(params => this.bookService.viewLoan(+params.get('id')!))
    ).subscribe(loan => {
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

  addEvent(event: MatDatepickerInputEvent<Date>) {
    this.event = event.value?.toDateString()
  }

  returnLoan(){
    this.bookService.deleteLoan(this.loan?.id!).subscribe(() => {
      this.router.navigate(['book/list'])
    })
  }

  editDate(){
    if(this.event){
      this.loan.finalDate = this.event;
    }else{
      //ALERTA
    }
    this.bookService.editLoan(this.loan).subscribe(() => {
      this.router.navigate(['book/view-loan', this.loan?.id])
    }
    )
  }

}
