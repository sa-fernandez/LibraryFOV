import { BookEditComponent } from './book/book-edit/book-edit.component';
import { BookCreateComponent } from './book/book-create/book-create.component';
import { BookViewComponent } from './book/book-view/book-view.component';
import { BookListComponent } from './book/book-list/book-list.component';
import { HomeComponent } from './home/home/home.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthorEditComponent } from './author/author-edit/author-edit.component';
import { LoanCreateComponent } from './loan/loan-create/loan-create.component';
import { ReturnBookComponent } from './loan/return-book/return-book.component';
import { LoanViewComponent } from './loan/loan-view/loan-view.component';
import { CreateCopyComponent } from './realbook/create-copy/create-copy.component';
import { AuthGuard } from './guard/auth.guard';

const routes: Routes = [
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
  { path: '', pathMatch: 'full', redirectTo: '/home' },
  { path: 'book/list', component: BookListComponent, canActivate: [AuthGuard] },
  { path: 'book/view/:id', component: BookViewComponent },
  { path: 'book/create', component: BookCreateComponent, canActivate: [AuthGuard] },
  { path: 'book/edit/:id', component: BookEditComponent },
  { path: 'book/edit-authors', component: AuthorEditComponent },
  { path: 'book/create-loan/:id', component: LoanCreateComponent },
  { path: 'book/return-book', component: ReturnBookComponent, canActivate: [AuthGuard] },
  { path: 'book/view-loan/:id', component: LoanViewComponent },
  { path: 'book/create-copy/:id', component: CreateCopyComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
