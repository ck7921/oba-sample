import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BalancesData} from "./models/balances.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private client: HttpClient) { }

  getAccountBalances() : Observable<BalancesData> {
    return this.client.get<BalancesData>("http://localhost:8080/api/balances");
  }

}
