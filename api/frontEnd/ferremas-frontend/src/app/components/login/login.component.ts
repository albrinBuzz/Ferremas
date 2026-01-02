import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
    imports: [FormsModule, ReactiveFormsModule
      ,CommonModule
    ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

 username = '';
  password = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}



    // login.component.ts
    login(): void {
      this.authService.login(this.username, this.password).subscribe(success => {
        if (success) {
          // redirige o actualiza la vista
          /*console.log('Token:', this.authService.getToken());
          console.log('Nombre:', this.authService.getUserName());
          console.log('Roles:', this.authService.getUserRoles());
          const user = this.authService.getUser();
          console.log('Usuario logeado:', user);*/
          //this.router.navigate(['/']);
        } else {
          console.log('Erro al iniciar sesion');
          this.error = 'Credenciales inválidas';
        }
      });
    }





}
