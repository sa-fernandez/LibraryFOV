import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Author } from 'src/app/model/author';
import { Loan } from 'src/app/model/loan';
import { BookService } from 'src/app/shared/book.service';
import { SecurityService } from 'src/app/shared/security.service';

@Component({
  selector: 'app-return-book',
  templateUrl: './return-book.component.html',
  styleUrls: ['./return-book.component.scss']
})
export class ReturnBookComponent implements OnInit {

  selectedPerson : Author | undefined;
  event : string | undefined = "";

  person : Author = new Author(0, "");

  allPeople : Author[] | undefined;

  loans : Loan[] | undefined;

  constructor(
    private bookService : BookService, 
    private securityService : SecurityService,
    private route : ActivatedRoute, 
    private router : Router
  ) { }

  ngOnInit(): void {
    this.bookService.listAuthors().subscribe(people => this.allPeople = people)
    this.securityService.loadProfile().then(user => console.log(user.username))
  }

  onSubmit(){
    this.person = new Author(0, "");
    if(this.selectedPerson){
      this.person = this.selectedPerson;
    }else{
      //ALERTA
    }
    this.bookService.listLoansPerson(this.person.id).subscribe(loans => this.loans = loans);
  }


}
