

export class TransactionsDetailsData {
    description: string;
    timestampDisplay: string;

    // amount
    amountDisplay: string;
    amountType: string;
    amountCurrencySymbol: string;
    // balance
    balanceAmountDisplay: string;
    balanceAmountType: string;
    balanceCurrencySymbol: string;
}

export class TransactionsData {
    totalPageCount: number;
    currentPageNumber: number;

    accountBalanceDisplay: string;
    accountCurrencySymbol: string;
    accountAmountType: string;

    transactionDetails: TransactionsDetailsData[]
}
