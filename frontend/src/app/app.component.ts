import {Component, OnInit} from '@angular/core';
import {RouterLink, RouterOutlet} from "@angular/router";
import {UserService} from "./services/user.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [
    RouterOutlet,
    RouterLink,
    NgIf
  ],
  standalone: true
})
export class AppComponent implements OnInit{

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
  }

  isAuthenticated(): boolean {
    return this.userService.isAuthenticated();
  }

}
