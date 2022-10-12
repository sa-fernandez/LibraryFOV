import { Component, OnInit } from '@angular/core';
import { Book } from 'src/app/model/book';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { Author } from 'src/app/model/author';
import { BookService } from '../../shared/book.service';

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

    this.bookService.listAuthors().subscribe(authors => this.allAuthors = authors);
  }

  dropboxClick(){
    if(this.selectedAuthor){
      this.isDisabled = true;
      this.inputName = '';
    }else{
      this.isDisabled = false;
    }
  }

  deleteBook(){
    this.bookService.deleteBook(this.book!.id).subscribe(() => {
      this.router.navigate(['book/list'])
    });
  }

  saveAuthor(){
    this.bookService.createAuthor(this.author).subscribe(author => {
      this.author = author
      this.linkAuthor()
    });
  }

  linkAuthor(){
    this.bookService.linkAuthorBook(this.author.id, this.book!.id).subscribe(() => {
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigate(['book/view', this.book!.id])
      });
    })
  }

  verifyAuthor(){
    let flag = false;
    this.authors?.forEach(element => {
      if(element.id === this.author.id){
        flag = true;
      }
    });
    return flag;
  }

  onSubmit(){
    if(this.inputName){
      this.author.name = this.inputName;
      this.saveAuthor();
    }else if(this.selectedAuthor){
      this.author = this.selectedAuthor;
      if(!this.verifyAuthor()){
        this.linkAuthor();
      }
    }else{
      //ALERTA
    }
  }

}
