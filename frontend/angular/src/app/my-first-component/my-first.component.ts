import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MyCalculatorService} from "../services/my-calculator.service";

@Component({
  selector: 'app-my-first-component',
  templateUrl: './my-first.component.html',
  styleUrls: ['./my-first.component.scss']
})
export class MyFirstComponent {
  valueOne: number = 0;
  valueTwo: number = 0;
  result: number = 0;

  constructor(private readonly calc:MyCalculatorService) {
  }

  sum() {
    this.result = this.calc.sum(this.valueOne, this.valueTwo)
  }

  subtract() {
    this.result = this.calc.subtract(this.valueOne, this.valueTwo)
  }

  multiple() {
    this.result = this.calc.multiple(this.valueOne, this.valueTwo)
  }

  divide() {
    this.result = this.calc.divide(this.valueOne, this.valueTwo)

  }
}
