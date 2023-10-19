import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject, Observable, tap} from "rxjs";
import {AuthResult} from "./models/auth-result.model";
import {ActionResult} from "./models/action-result.model";


@Injectable({
    providedIn: 'root'
})
export class UserService {
    user = new BehaviorSubject<AuthResult>(null);
    consentApproved: boolean;

    constructor(private client: HttpClient) {
    }

    authenticateUser(userName: string, password: string): Observable<AuthResult> {
        return this.client.post<AuthResult>("http://localhost:8080/authenticate", {
            userName: userName, password: password
        })
            .pipe(
                tap(resData => {
                    this.user.next(resData);
                })
            );
    }

    consentApproval(consentId: string): Observable<AuthResult> {
        return this.client.get<ActionResult>(`http://localhost:8080/approveConsent/${consentId}`)
            .pipe(
                tap(res => {
                    this.consentApproved = res.success;
                })
            );
    }

    logout(): Observable<ActionResult> {
        return this.client.get<ActionResult>("http://localhost:8080/goodbye")
            .pipe(
                tap(resData => {
                    this.user.next(null);
                    this.consentApproved = false;
                })
            );
    }

    isAuthenticated(): boolean {
        return this.consentApproved
            && this.user.getValue() != null
            && this.user.getValue().success;
    }

    showAuthState(): void {
        console.log("auth state in usr srvc: " + this.isAuthenticated())
    }
}
