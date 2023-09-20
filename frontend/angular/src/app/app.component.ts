import {Component, Output} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'angular';

  clickCount: number = 0;
  lastCreatedElement: string = ''

  constructor(private router:Router){

  }

}
