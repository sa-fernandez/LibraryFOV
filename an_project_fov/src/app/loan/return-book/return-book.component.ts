import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { Author } from 'src/app/model/author';
import { Book } from 'src/app/model/book';
import { Loan } from 'src/app/model/loan';
import { Realbook } from 'src/app/model/realbook';
import { BookService } from 'src/app/shared/book.service';

@Component({
  selector: 'app-return-book',
  templateUrl: './return-book.component.html',
  styleUrls: ['./return-book.component.scss']
})
export class ReturnBookComponent implements OnInit {

  isDisabled : boolean = false;

  selectedPerson : Author | undefined;
  inputName : string = "";
  event : string | undefined = "";

  book : Book | undefined;
  realBook : Realbook | undefined;
  person : Author = new Author(0, "");

  authors : Author[] | undefined;
  allPeople : Author[] | undefined;

  loans : Loan[] | undefined;

  constructor(
    private bookService : BookService, 
    private route : ActivatedRoute, 
    private router : Router
  ) { }

  ngOnInit(): void {
    this.bookService.listAuthors().subscribe(people => this.allPeople = people)
  }

  savePerson(){
    this.bookService.createAuthor(this.person).subscribe(author => {
      this.person = author
      this.linkPerson()
    });
  }

  linkPerson(){
    this.bookService.linkAuthorBook(this.person.id, this.book!.id).subscribe(() => {
      if(this.event){
        this.bookService.createLoan(this.realBook!.id, this.person.id, this.event).subscribe(() => {
          this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
            this.router.navigate(['book/view', this.book!.id])
          });
        })
      }
    })
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
    this.person = new Author(0, "");
    if(this.inputName){
      this.person.name = this.inputName;
    }else if(this.selectedPerson){
      this.person = this.selectedPerson;
    }else{
      //ALERTA
    }
    this.bookService.listLoansPerson(this.person.id).subscribe(loans => 
      this.loans = loans

    );
  }


}
