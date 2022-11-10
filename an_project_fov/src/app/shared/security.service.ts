import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class SecurityService {

  constructor(private http: HttpClient, public keycloakService: KeycloakService) { }

  logout() {
    this.keycloakService.clearToken();
    this.keycloakService.logout("http://localhost:4200/");
  }

  userData() {
    console.log(this.keycloakService.isUserInRole("ROLE_USER"));
    console.log(this.keycloakService.getUserRoles());
    console.log(this.keycloakService.loadUserProfile().then(console.log));
  }

  loadProfile(): Promise <any>{
    return new Promise <any>(resolve => this.keycloakService.loadUserProfile().then(data=>resolve(data)))
  }

  isLibrarian(): boolean{
    return this.keycloakService.isUserInRole("ROLE_LIBRARIAN") || this.keycloakService.isUserInRole("ROLE_ADMIN");
  }

  isUser(): boolean{
    return this.keycloakService.isUserInRole("ROLE_USER") || this.keycloakService.isUserInRole("ROLE_ADMIN");
  }

  userName(): string{
    return this.keycloakService.getUsername();
  }

  userN() : Promise<any>{
    return new Promise<any>(async (resolve, reject) => {
      if(await this.keycloakService.isLoggedIn()){
        this.keycloakService.loadUserProfile().then(data=>resolve(data)).catch(error => console.log(error))
      }else{
        console.log('user not logged in')
      }
    })
  }

}