import { Component, OnInit } from '@angular/core';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { Author } from 'src/app/model/author';
import { Book } from 'src/app/model/book';
import { Realbook } from 'src/app/model/realbook';
import { BookService } from 'src/app/shared/book.service';

@Component({
  selector: 'app-loan-create',
  templateUrl: './loan-create.component.html',
  styleUrls: ['./loan-create.component.scss']
})
export class LoanCreateComponent implements OnInit {

  minDate : Date = new Date();
  maxDate : Date = new Date();

  isDisabled : boolean = false;

  selectedPerson : Author | undefined;
  inputName : string = "";
  event : string | undefined = "";

  book : Book | undefined;
  realBook : Realbook | undefined;
  person : Author = new Author(0, "");

  authors : Author[] | undefined;
  allPeople : Author[] | undefined;

  constructor(
    private bookService : BookService, 
    private route : ActivatedRoute, 
    private router : Router
  ) { }

  ngOnInit(): void {
    this.maxDate.setMonth(this.maxDate.getMonth() + 1);
    this.route.paramMap.pipe(
      switchMap(params => this.bookService.viewCopy(+params.get('id')!))
    ).subscribe(realBook => {
      this.realBook = realBook
      this.bookService.copyBook(realBook.id).subscribe(book => {
        this.book = book
        this.bookService.bookAuthors(book.id).subscribe(authors => this.authors = authors)
      })
    });

    this.bookService.listAuthors().subscribe(people => this.allPeople = people)
  }

  savePerson(){
    this.bookService.createAuthor(this.person).subscribe(author => {
      this.person = author
      this.linkPerson()
    });
  }

  linkPerson(){
    if(this.event){
      this.bookService.createLoan(this.realBook!.id, this.person.id, this.event).subscribe(() => {
        this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
          this.router.navigate(['book/view', this.book!.id])
        });
      })
    }
  }

  dropboxClick(){
    if(this.selectedPerson){
      this.isDisabled = true;
      this.inputName = '';
    }else{
      this.isDisabled = false;
    }
  }

  verifyAuthor(){
    let flag = false;
    this.authors?.forEach(element => {
      if(element.id === this.person.id){
        flag = true;
      }
    });
    return flag;
  }

  onSubmit(){
    if(this.inputName){
      this.person.name = this.inputName;
      this.savePerson();
    }else if(this.selectedPerson){
      this.person = this.selectedPerson;
      if(!this.verifyAuthor()){
        this.linkPerson();
      }
    }else{
      //ALERTA
    }
  }

  addEvent(event: MatDatepickerInputEvent<Date>) {
    this.event = event.value?.toLocaleDateString()
  }

}
