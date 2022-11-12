import { Component, OnInit } from '@angular/core';
import { Book } from 'src/app/model/book';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { Author } from 'src/app/model/author';
import { BookService } from '../../shared/book.service';
import { Realbook } from 'src/app/model/realbook';
import { SecurityService } from 'src/app/shared/security.service';

@Component({
  selector: 'app-book-view',
  templateUrl: './book-view.component.html',
  styleUrls: ['./book-view.component.scss']
})
export class BookViewComponent implements OnInit {

  librarian: boolean = false;

  selectedAuthor: Author | undefined;
  inputName: string = "";

  book: Book | undefined;
  author: Author = new Author(0, "");

  authors: Author[] | undefined;
  allAuthors: Author[] | undefined;
  copies: Realbook[] | undefined;

  activated: number = 0;
  requestLaunch = 0;
  requestType = -1; //1-loanCreate, 2-copyCreate, 3-editBook
  copyId: number = -1;

  constructor(
    private bookService: BookService,
    private securityService: SecurityService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.librarian = this.securityService.isLibrarian();
    this.route.paramMap.pipe(
      switchMap(params => this.bookService.viewBook(+params.get('id')!))
    ).subscribe(book => {
      this.book = book
      this.bookService.bookAuthors(book.id).subscribe(authors => this.authors = authors)
      this.bookService.listNotBorrowed(book.id).subscribe(copies => this.copies = copies)
    });

    this.bookService.listAuthors().subscribe(authors => this.allAuthors = authors);
  }

  deleteBook() {
    this.bookService.deleteBook(this.book!.id).subscribe(() => {
      this.router.navigate(['book/list'])
    });
  }

  deleteCopy(index: number) {
    this.bookService.deleteCopy(this.copies![index].id).subscribe(() => {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['book/view', this.book!.id])
      });
    });
  }

  toggleCopiesActivated() {

    let toggle = document.getElementById('toggle-copies') as HTMLElement;
    let layout = document.getElementById('copies-layout') as HTMLElement;

    if (this.activated) {
      layout.style.maxHeight = "0px";
      toggle.style.rotate = "0deg";
      this.activated = 0;
    }

    else {
      layout.style.maxHeight = "250px";
      toggle.style.rotate = "-180deg";
      this.activated = 1;
    }


  }

  launchWindow(option: number, i?: number) {
    let mainSection = document.getElementById('book-view') as HTMLElement;
    mainSection.classList.toggle('emergent-activated')
    this.requestLaunch = 1;

    if (option === 1) {
      this.requestType = 1;

      if (typeof i !== 'undefined') {
        this.copyId = this.copies![i].id;
      }
    }

    else if (option === 2) {
      this.requestType = 2;

    }

    else if (option === 3) {
      this.requestType = 3;

    }

  }

  closeWindow() {
    let mainSection = document.getElementById('book-view') as HTMLElement;
    mainSection.classList.toggle('emergent-activated')
    this.requestLaunch = 0;
    this.requestType = -1;
  }



  toogleOptionActivated() {
    let buttonsLayout = document.getElementById('buttons-layout') as HTMLElement;
    buttonsLayout.classList.toggle('layout-activated');
  }
}
