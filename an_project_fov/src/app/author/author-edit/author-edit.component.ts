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

  book: Book | undefined;
  authors: Author[] | undefined;
  selectedAuthor: Author | undefined;
  inputName: string = "";
  isDisabled: boolean = false;

  author: Author = new Author(0, "");
  allAuthors: Author[] | undefined;

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.route.paramMap.pipe(
      switchMap(params => this.bookService.viewBook(+params.get('id')!))
    ).subscribe(book => {
      this.book = book
      this.bookService.bookAuthors(book.id).subscribe(authors => this.authors = authors);
    });

    this.bookService.listAuthors().subscribe(authors => this.allAuthors = authors);
  }

  updateAuthor(index: number) {
    this.bookService.editAuthor(this.authors![index]).subscribe(() => {
      this.router.navigate(['book/edit', this.book!.id])
    });
  }

  unlinkAuthor(index: number) {
    this.bookService.unlinkAuthorBook(this.authors![index].id, this.book!.id).subscribe(() => {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['book/edit', this.book!.id])
      });
    });
  }

  dropboxClick() {
    if (this.selectedAuthor) {
      this.isDisabled = true;
      this.inputName = '';
    } else {
      this.isDisabled = false;
    }
  }

  onSubmit() {
    if (this.inputName) {
      this.author.name = this.inputName;
      this.saveAuthor();
    } else if (this.selectedAuthor) {
      this.author = this.selectedAuthor;
      if (!this.verifyAuthor()) {
        this.linkAuthor();
      }
    } else {
      //ALERTA
    }
  }

  saveAuthor() {
    this.bookService.createAuthor(this.author).subscribe(author => {
      this.author = author
      this.linkAuthor()
    });
  }

  verifyAuthor() {
    let flag = false;
    this.authors?.forEach(element => {
      if (element.id === this.author.id) {
        flag = true;
      }
    });
    return flag;
  }

  linkAuthor() {
    this.bookService.linkAuthorBook(this.author.id, this.book!.id).subscribe(() => {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['book/edit', this.book!.id])
      });
    })
  }

  changeVisibility() {
    let addContainer = document.getElementById("add-author-icon") as HTMLElement;
    let formContainer = document.getElementById("add-author-form") as HTMLElement;


    if (addContainer.classList.contains('author-visibility')) {
      addContainer.classList.toggle('author-visibility')
    }

    if (formContainer.classList.contains('form-visibility')) {
      formContainer.classList.toggle('form-visibility')
    }
  }
}
