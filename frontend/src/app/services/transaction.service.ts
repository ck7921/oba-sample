import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {TransactionsData} from "./models/transactions.model";

@Injectable({
    providedIn: 'root'
})
export class TransactionService {
    constructor(private client: HttpClient) {
    }

    getAccountTransactions(accountId: string, page: number, sortDirection: string): Observable<TransactionsData> {
        return this.client.get<TransactionsData>(`http://localhost:8080/api/transactions/${accountId}`,
            {
                params: {
                    page: page,
                    sortDirection: sortDirection
                }
            });
    }
}
