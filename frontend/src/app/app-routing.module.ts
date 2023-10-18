import {RouterModule, Routes} from "@angular/router";
import {BalancesComponent} from "./balances/balances.component";
import {NgModule} from "@angular/core";

const routes: Routes = [
  { path: 'balances', component: BalancesComponent }
]

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
