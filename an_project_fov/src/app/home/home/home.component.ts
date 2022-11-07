import { Component, OnInit } from '@angular/core';
import { Author } from 'src/app/model/author';
import { BookService } from 'src/app/shared/book.service';
import { SecurityService } from 'src/app/shared/security.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  uname : string | undefined;

  constructor(
    private bookService : BookService, 
    private securityService : SecurityService
  ) { }

  ngOnInit(): void {
    this.securityService.loadProfile().then(elem => {
      this.uname = elem.username
    });
  }

}
