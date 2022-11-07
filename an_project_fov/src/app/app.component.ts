import { Component, OnInit } from '@angular/core';
import { SecurityService } from './shared/security.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  title = 'an_project_fov';
  librarian : boolean = false;
  user : boolean = false;
  
  constructor(
    private securityService : SecurityService
  ){ }
  
  logOut() {
    this.securityService.logout();
  }

  ngOnInit(): void {
    this.librarian = this.securityService.isLibrarian();
    this.user = this.securityService.isUser();
    this.securityService.userData();
  }

}
