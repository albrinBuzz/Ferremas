import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
    imports: [
    RouterModule,
    FormsModule,
    CommonModule // 👈 AÑADE ESTO AQUÍ TAMBIÉN
  ],
    standalone: true, // importante
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
   // Estado simulado de login
 loginUsername = '';
  loginPassword = '';
  loginError = false;

  cartItemCount=4;

  constructor(public authService: AuthService) {}

  login(event: Event) {
    event.preventDefault();
    const success = this.authService.login(this.loginUsername, this.loginPassword);
    this.loginError = !success;
    if (success) {
      this.loginUsername = '';
      this.loginPassword = '';
    }
  }

  logout() {
    this.authService.logout();
  }

  get isLoggedIn(): boolean {
    return this.authService.isUserLoggedIn();
  }

  get userName(): string | null {
    return this.authService.getUserName();
  }

  hasRole(role: string): boolean  {
    if( this.authService.hasRole(role)){
      return true;
    }else{
      return false;
    }
    //return this.authService.hasRole(role);
  }
}
