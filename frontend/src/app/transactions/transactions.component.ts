import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoadingSpinnerComponent} from "../shared/loading-spinner.component";
import {TransactionsData, TransactionsDetailsData} from "../services/models/transactions.model";
import {TransactionService} from "../services/transaction.service";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
    selector: 'app-transactions',
    standalone: true,
    imports: [CommonModule, LoadingSpinnerComponent],
    templateUrl: './transactions.component.html',
    styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent implements OnInit, OnDestroy {
    loadingTransactionData: boolean = false;
    transactionsData: TransactionsData;
    transactionsDetailsData: TransactionsDetailsData[];

    accountId: string;
    currentPage: number = -1;
    totalPageCount: number = 0;
    sortDirection: string = "desc"

    transactionsSub: Subscription;

    constructor(private transactionService: TransactionService, private route : ActivatedRoute) {
    }

    ngOnInit(): void {
        this.accountId = this.route.snapshot.params['accountId'];
        this.loadNextPage();
    }

    toggleSortDirection(): void {
        this.sortDirection = this.sortDirection === "desc" ? "asc" : "desc";
        this.currentPage = -1;
        this.totalPageCount = 0;
        this.transactionsData = null;
        this.transactionsDetailsData = [];
        this.loadNextPage();
    }


    loadNextPage(): void {
        this.loadingTransactionData = true;
        this.transactionsSub = this.transactionService.getAccountTransactions(this.accountId,
            this.currentPage + 1, this.sortDirection)
            .subscribe(res => {
                this.loadingTransactionData = false;
                // pagination
                this.currentPage = res.currentPageNumber;
                this.totalPageCount = res.totalPageCount;
                // local data
                if(this.currentPage == 0) {
                    this.transactionsData = res;
                    this.transactionsDetailsData = res.transactionDetails;
                } else {
                    this.transactionsDetailsData.push(...res.transactionDetails);
                }

            })
    }

    getAmountTypeColor(s: string ) : string {
        return s === "DEBIT" ? "red" : "black";
    }

    hasMorePages(): boolean {
        return this.totalPageCount > 0 && this.currentPage + 1 < this.totalPageCount;
    }

    hasData() : boolean {
        return this.transactionsData != null;
    }

    ngOnDestroy(): void {
        if(this.transactionsSub) this.transactionsSub.unsubscribe();
    }

}
