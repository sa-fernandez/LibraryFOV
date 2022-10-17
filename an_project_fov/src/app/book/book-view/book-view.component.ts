import { Component, OnInit } from '@angular/core';
import { Book } from 'src/app/model/book';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { Author } from 'src/app/model/author';
import { BookService } from '../../shared/book.service';
import { Realbook } from 'src/app/model/realbook';

@Component({
  selector: 'app-book-view',
  templateUrl: './book-view.component.html',
  styleUrls: ['./book-view.component.scss']
})
export class BookViewComponent implements OnInit {

  isDisabled : boolean = false;

  selectedAuthor : Author | undefined;
  inputName : string = "";

  book : Book | undefined;
  author : Author = new Author(0, "");

  authors : Author[] | undefined;
  allAuthors : Author[] | undefined;
  copies : Realbook[] | undefined;

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
      this.bookService.bookAuthors(book.id).subscribe(authors => this.authors = authors)
      this.bookService.listNotBorrowed(book.id).subscribe(copies => this.copies = copies)
    });

    this.bookService.listAuthors().subscribe(authors => this.allAuthors = authors);
  }

  deleteBook(){
    this.bookService.deleteBook(this.book!.id).subscribe(() => {
      this.router.navigate(['book/list'])
    });
  }

  deleteCopy(index : number){
    this.bookService.deleteCopy(this.copies![index].id).subscribe(() => {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['book/view', this.book!.id])
      });
    });
  }

}
