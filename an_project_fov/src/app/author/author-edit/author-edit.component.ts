import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { Author } from 'src/app/model/author';
import { Book } from 'src/app/model/book';
import { BookService } from 'src/app/shared/book.service';

@Component({
  selector: 'app-author-edit',
  templateUrl: './author-edit.component.html',
  styleUrls: ['./author-edit.component.scss']
})
export class AuthorEditComponent implements OnInit {

  book : Book | undefined;
  authors : Author[] | undefined;

  constructor(
    private bookService : BookService, 
    private route : ActivatedRoute, 
    private router : Router
  ) { }

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => this.bookService.viewBook(+params.get('id')!))
    ).subscribe(book => {
      this.book = book
      this.bookService.bookAuthors(book.id).subscribe(authors => this.authors = authors);
    });
  }

  updateAuthor(index : number){
    this.bookService.editAuthor(this.authors![index]).subscribe(() => {
      this.router.navigate(['book/edit', this.book!.id])
    });
  }

  unlinkAuthor(index : number){
    this.bookService.unlinkAuthorBook(this.authors![index].id, this.book!.id).subscribe(() => {
      this.router.navigate(['book/edit', this.book!.id])
    });
  }

}
