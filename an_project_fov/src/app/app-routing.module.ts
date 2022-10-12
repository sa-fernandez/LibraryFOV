import { BookEditComponent } from './book/book-edit/book-edit.component';
import { BookCreateComponent } from './book/book-create/book-create.component';
import { BookViewComponent } from './book/book-view/book-view.component';
import { BookListComponent } from './book/book-list/book-list.component';
import { HomeComponent } from './home/home/home.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthorEditComponent } from './author/author-edit/author-edit.component';

const routes: Routes = [
  { path: 'home', component: HomeComponent },
  { path: '', pathMatch: 'full', redirectTo: '/home' },
  { path: 'book/list', component: BookListComponent },
  { path: 'book/view/:id', component: BookViewComponent },
  { path: 'book/create', component: BookCreateComponent },
  { path: 'book/edit/:id', component: BookEditComponent },
  { path: 'book/edit-authors', component: AuthorEditComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
