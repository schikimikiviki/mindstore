import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddText } from './add-text';

describe('AddText', () => {
  let component: AddText;
  let fixture: ComponentFixture<AddText>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddText]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddText);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
