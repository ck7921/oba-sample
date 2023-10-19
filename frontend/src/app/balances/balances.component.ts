import {Component, OnDestroy, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {BalancesData} from "../services/models/balances.model";
import {AccountService} from "../services/account.service";
import {Subscription} from "rxjs";
import {RouterLink} from "@angular/router";
import {LoadingSpinnerComponent} from "../shared/loading-spinner.component";

@Component({
  selector: 'app-balances',
  standalone: true,
  imports: [CommonModule, RouterLink, LoadingSpinnerComponent],
  templateUrl: './balances.component.html',
  styleUrls: ['./balances.component.css']
})
export class BalancesComponent implements OnInit, OnDestroy {
  balancesDataSubscription: Subscription;
  balancesData: BalancesData;
  loadingBalanceData: boolean = false;

  constructor(private accountService: AccountService) { }

  ngOnInit() : void {
    this.refreshBalances();
  }

  refreshBalances() {
    if(this.balancesDataSubscription) this.balancesDataSubscription.unsubscribe();
    this.loadingBalanceData = true;
    this.balancesDataSubscription = this.accountService
      .getAccountBalances()
      .subscribe(data => {
        this.balancesData = data;
        this.loadingBalanceData = false;
    });
  }

  onRefresh() {
    this.refreshBalances();
  }

  ngOnDestroy() {
    if(this.balancesData!==null) this.balancesDataSubscription.unsubscribe();
  }

}
