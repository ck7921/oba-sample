import {Component, OnDestroy} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserService} from "../services/user.service";
import {Subscription} from "rxjs";
import {LoadingSpinnerComponent} from "../shared/loading-spinner.component";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
    selector: 'app-consent',
    standalone: true,
    imports: [CommonModule, LoadingSpinnerComponent],
    templateUrl: './consent.component.html',
    styleUrls: ['./consent.component.css']
})
export class ConsentComponent implements OnDestroy {
    errorMessage: string;
    loading: boolean;

    componentSub: Subscription;

    constructor(private userService: UserService, private router: Router,
                private route : ActivatedRoute) {
    }

    onApprove(): void {
        this.loading = true;
        console.log("approve consent: " + this.route.snapshot.params['consentId'])
        this.componentSub = this.userService.consentApproval(this.route.snapshot.params['consentId'])
            .subscribe(res => {
                this.loading = false;
                if (res.success) {
                    this.router.navigate(['/']);
                } else {
                    this.errorMessage = res.errorMessage;
                }
            });
    }

    ngOnDestroy() {
        if (this.componentSub) this.componentSub.unsubscribe();
    }
}
