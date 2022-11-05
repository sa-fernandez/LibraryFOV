import { Component, OnInit } from '@angular/core';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs';
import { Book } from 'src/app/model/book';
import { Realbook } from 'src/app/model/realbook';
import { BookService } from 'src/app/shared/book.service';

@Component({
  selector: 'app-create-copy',
  templateUrl: './create-copy.component.html',
  styleUrls: ['./create-copy.component.scss']
})
export class CreateCopyComponent implements OnInit {

  inputStatus : string = "";
  event : string | undefined = "";
  book : Book | undefined;

  realbook : Realbook = new Realbook(0, "","");
  now : Date = new Date();

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
    });
  }

  onSubmit(){
    if(!this.event){
      this.event = this.now.toLocaleDateString();
    }
    if(this.inputStatus){
      this.realbook.status = this.inputStatus;
      this.realbook.timestamp = this.event;
      this.bookService.createRealBook(this.realbook).subscribe(realbook => {
        this.bookService.linkCopy(this.book!.id, realbook.id).subscribe(() => {
          this.router.navigate(['book/view', this.book?.id])
        })
      });
    }
  }

  addEvent(event: MatDatepickerInputEvent<Date>) {
    this.event = event.value?.toLocaleDateString();
  }

}
