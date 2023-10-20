import {RouterModule, Routes} from "@angular/router";
import {BalancesComponent} from "./balances/balances.component";
import {NgModule} from "@angular/core";
import {AuthenticationComponent} from "./authentication/authentication.component";
import {ConsentComponent} from "./consent/consent.component";
import {TransactionsComponent} from "./transactions/transactions.component";
import {WelcomeComponent} from "./welcome/welcome.component";

const routes: Routes = [
  { path: '', component: WelcomeComponent },
  { path: 'balances', component: BalancesComponent },
  { path: 'authentication/:action', component: AuthenticationComponent },
  { path: 'consent/:consentId', component: ConsentComponent },
  { path: 'transactions/:accountId', component: TransactionsComponent }
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
