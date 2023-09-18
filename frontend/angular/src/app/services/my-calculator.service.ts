import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MyCalculatorService {

  constructor() { }

  sum(valueOne:number, valueTwo:number) :number  {
    return +valueOne + +valueTwo
  }

  subtract(valueOne:number, valueTwo:number):number {
    return +valueOne - +valueTwo
  }

  multiple(valueOne:number, valueTwo:number):number {
    return +valueOne * +valueTwo
  }

  divide(valueOne:number, valueTwo:number):number{
    return +valueOne / +valueTwo
  }
}
