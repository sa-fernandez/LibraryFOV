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
import {MatNativeDateModule} from '@angular/material/core';
import { AuthorEditComponent } from './author/author-edit/author-edit.component';
import { LoanCreateComponent } from './loan/loan-create/loan-create.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ReturnBookComponent } from './loan/return-book/return-book.component';
import { LoanListComponent } from './loan/loan-list/loan-list.component';
import { LoanViewComponent } from './loan/loan-view/loan-view.component';
import { CreateCopyComponent } from './realbook/create-copy/create-copy.component';

@NgModule({
  declarations: [
    AppComponent,
    BookCreateComponent,
    BookListComponent,
    BookViewComponent,
    BookEditComponent,
    HomeComponent,
    AuthorEditComponent,
    LoanCreateComponent,
    ReturnBookComponent,
    LoanListComponent,
    LoanViewComponent,
    CreateCopyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
