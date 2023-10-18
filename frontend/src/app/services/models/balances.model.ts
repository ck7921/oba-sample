

class AccountBalanceData {
  public accountId: string;
  public accountName: string;
  public balanceDisplay: string;
  public currencySymbol: string;
}

export class BalancesData {
  public balanceTotalDisplay: string;
  public currencySymbol: string;

  public accountBalances: AccountBalanceData[];

  constructor() {}

}
