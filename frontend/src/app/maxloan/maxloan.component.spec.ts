import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MaxloanComponent } from './maxloan.component';

describe('MaxloanComponent', () => {
  let component: MaxloanComponent;
  let fixture: ComponentFixture<MaxloanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MaxloanComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MaxloanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
