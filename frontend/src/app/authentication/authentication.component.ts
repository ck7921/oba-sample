import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserService} from "../services/user.service";
import {FormsModule} from "@angular/forms";
import {Subscription} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {LoadingSpinnerComponent} from "../shared/loading-spinner.component";

@Component({
    selector: 'app-authentication',
    standalone: true,
    imports: [CommonModule, FormsModule, LoadingSpinnerComponent],
    templateUrl: './authentication.component.html',
    styleUrls: ['./authentication.component.css']
})
export class AuthenticationComponent implements OnInit, OnDestroy {
    errorMessage: string;
    loading: boolean;

    authenticationSub: Subscription;
    userName: string;
    password: string;

    constructor(private userService: UserService, private router: Router,
                private route : ActivatedRoute) {
    }

    ngOnInit(): void {
        if(this.route.snapshot.params['action']==='logout') {
            this.logout();
        }
    }

    onLoginClick() {
        this.loading = true;
        this.authenticationSub = this.userService.authenticateUser(this.userName, this.password)
            .subscribe(res => {
                this.loading = false;
                if (!res.success) {
                    this.errorMessage = res.errorMessage;
                } else {
                    // navigate to consent page
                    this.router.navigate(['/consent', res.consentId]);
                }
            })
        this.password = null;

    }

    isAuthenticated(): boolean {
        return this.userService.isAuthenticated();
    }

    logout(): void {
        if (this.authenticationSub) this.authenticationSub.unsubscribe();
        this.loading = true;
        this.authenticationSub = this.userService.logout()
            .subscribe(result => {
                this.loading = false;
                this.router.navigate(['/']);
            });
        this.userName = null;
    }

    ngOnDestroy() {
        if (this.authenticationSub) this.authenticationSub.unsubscribe();
    }

}
