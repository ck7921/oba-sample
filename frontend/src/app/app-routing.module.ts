import {RouterModule, Routes} from "@angular/router";
import {BalancesComponent} from "./balances/balances.component";
import {NgModule} from "@angular/core";
import {AuthenticationComponent} from "./authentication/authentication.component";
import {ConsentComponent} from "./consent/consent.component";

const routes: Routes = [
  { path: 'balances', component: BalancesComponent },
  { path: 'authentication/:action', component: AuthenticationComponent },
  { path: 'consent/:consentId', component: ConsentComponent }
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
