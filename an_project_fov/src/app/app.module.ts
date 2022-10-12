import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BookCreateComponent } from './book/book-create/book-create.component';
import { BookListComponent } from './book/book-list/book-list.component';
import { BookViewComponent } from './book/book-view/book-view.component';
import { BookEditComponent } from './book/book-edit/book-edit.component';
import { HomeComponent } from './home/home/home.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AuthorEditComponent } from './author/author-edit/author-edit.component'

@NgModule({
  declarations: [
    AppComponent,
    BookCreateComponent,
    BookListComponent,
    BookViewComponent,
    BookEditComponent,
    HomeComponent,
    AuthorEditComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
