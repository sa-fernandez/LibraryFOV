import { Component, Input, OnInit } from '@angular/core';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { Author } from 'src/app/model/author';
import { Book } from 'src/app/model/book';
import { Realbook } from 'src/app/model/realbook';
import { BookService } from 'src/app/shared/book.service';
import { SecurityService } from 'src/app/shared/security.service';

@Component({
  selector: 'app-loan-create',
  templateUrl: './loan-create.component.html',
  styleUrls: ['./loan-create.component.scss']
})
export class LoanCreateComponent implements OnInit {

  minDate : Date = new Date();
  maxDate : Date = new Date();
  now : Date = new Date();

  isDisabled : boolean = false;

  selectedPerson : Author | undefined;
  inputName : string = "";
  event : string | undefined = "";

  book : Book | undefined;
  realBook : Realbook | undefined;
  person : Author | undefined;

  authors : Author[] | undefined;
  allPeople : Author[] | undefined;

  @Input()
  copyId: number | undefined;

  constructor(
    private bookService : BookService, 
    private securityService : SecurityService,
    private route : ActivatedRoute, 
    private router : Router
  ) { }

  ngOnInit(): void {
    this.maxDate.setMonth(this.maxDate.getMonth() + 1);

    this.bookService.viewCopy(this.copyId!).subscribe((realBook => {
      this.realBook = realBook;
        this.bookService.copyBook(realBook.id).subscribe(book => {
          this.book = book;
          this.bookService.bookAuthors(book.id).subscribe(authors => this.authors = authors);
        });
      })
    );
    
    this.bookService.listAuthors().subscribe(people => this.allPeople = people)
    this.bookService.viewAuthor(this.securityService.userName()).subscribe(elem => {
      this.person = elem
    })
  }

  savePerson(){
    this.bookService.createAuthor(this.person!).subscribe(author => {
      this.person = author
      this.linkPerson()
    });
  }

  linkPerson(){
    if(!this.event){
      this.event = this.now.toLocaleDateString();
    }
    this.bookService.createLoan(this.realBook!.id, this.person!.id, this.event).subscribe(() => {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['book/view', this.book!.id])
      });
    })
  }

  verifyAuthor(){
    let flag = false;
    this.authors?.forEach(element => {
      if(element.id === this.person!.id){
        flag = true;
      }
    });
    return flag;
  }

  onSubmit(){
    if(!this.person){
      this.person = new Author(0, this.securityService.userName());
      this.savePerson();
    }else{
      if(!this.verifyAuthor()){
        this.linkPerson();
      }
    }
  }

  addEvent(event: MatDatepickerInputEvent<Date>) {
    this.event = event.value?.toLocaleDateString()
  }

}
