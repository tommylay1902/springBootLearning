import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-my-first-component',
  templateUrl: './my-first.component.html',
  styleUrls: ['./my-first.component.scss']
})
export class MyFirstComponent {
  inputValue:string = 'Hello';
  displayValue: string = '';
  displayMessage: boolean = false;
  doneLoading:boolean = false
  messages: string [] = [];
  
  onClickHandler(): void{
    this.messages.unshift(this.inputValue);
  }



}
