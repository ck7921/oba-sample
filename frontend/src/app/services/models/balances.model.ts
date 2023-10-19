

class AccountBalanceData {
  public accountId: string;
  public accountName: string;
  public balanceDisplay: string;
  public currencySymbol: string;
  public amountType: string;
}

class CurrencyTotalData {
  public balanceDisplay: string;
  public currencySymbol: string;
  public amountType: string;
}

export class BalancesData {
  public currencyTotals: CurrencyTotalData[];
  public accountBalances: AccountBalanceData[];

  constructor() {}

}
