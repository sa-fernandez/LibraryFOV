import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateCopyComponent } from './create-copy.component';

describe('CreateCopyComponent', () => {
  let component: CreateCopyComponent;
  let fixture: ComponentFixture<CreateCopyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateCopyComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateCopyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
