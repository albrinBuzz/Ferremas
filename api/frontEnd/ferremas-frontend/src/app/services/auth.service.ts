import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';

const TOKEN_KEY = 'jwtToken';

const USERNAME_KEY = 'nombreUsuario';
const RUT_KEY = 'rutUsuario';
const ROLES_KEY = 'roles';

export interface AuthResponse {
  token: string;
  nombreUsuario: string;
  rutUsuario: string;
  roles: string[];
}


// src/app/models/auth-response.ts
/*export interface AuthResponse {
  token: string;
  nombreUsuario: string;
  rutUsuario: string;
  roles: string[];
}*/


export interface JwtPayload {
  sub: string; // correo
  roles: string[];
  rutUsuario: string;
  nombreusuario: string;
  firstLogin: boolean;
  exp: number;
  iat: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private apiUrl = 'http://localhost:8080/auth/login';
  private jwtHelper = new JwtHelperService();

  constructor(private http: HttpClient) {}


  private isLoggedIn = false;
  private userName = '';
  private roles: string[] = [];


  login(username: string, password: string): Observable<boolean> {
  return this.http.post<AuthResponse>(this.apiUrl, { username, password }).pipe(
    map(res => {
      //localStorage.setItem(TOKEN_KEY, res.token);
      this.saveSession(res);
      console.log('Login exitoso, token recibido:', res.token);
      this.isLoggedIn=true;
      return true;
    }),
    catchError(err => {
      console.error('Error en login:', err);

      // Puedes capturar detalles del error para mostrar
      if (err.error && err.error.message) {
        console.error('Mensaje del servidor:', err.error.message);
      }

      return of(false);
    })
  );
}

  private saveSession(res: AuthResponse): void {
    localStorage.setItem(TOKEN_KEY, res.token);
    localStorage.setItem(USERNAME_KEY, res.nombreUsuario);
    localStorage.setItem(RUT_KEY, res.rutUsuario);
    localStorage.setItem(ROLES_KEY, JSON.stringify(res.roles));

    //localStorage.setItem(ROLES_KEY, JSON.stringify(res.roles));
  }

  /**
   * Limpia el almacenamiento local.
   */
  logout(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USERNAME_KEY);
    localStorage.removeItem(RUT_KEY);
    localStorage.removeItem(ROLES_KEY);
  }


private saveToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
  }

  /*logout(): void {
    localStorage.removeItem(TOKEN_KEY);
  }*/

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return token != null && !this.jwtHelper.isTokenExpired(token);
  }

  public getUser(): JwtPayload | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      return this.jwtHelper.decodeToken(token) as JwtPayload;
    } catch {
      return null;
    }
  }

  getRutUsuario(): string | null {
    return this.getUser()?.rutUsuario || null;
  }


  /*login(username: string, password: string): boolean {
    // Simulación de login
    if (username === 'admin' && password === '1234') {
      this.isLoggedIn = true;
      this.userName = 'admin';
      this.roles = ['ROLE_ADMINISTRADOR', 'ROLE_CLIENTE']; // simula roles
      return true;
    }

    if (username === 'cliente' && password === '1234') {
      this.isLoggedIn = true;
      this.userName = 'cliente';
      this.roles = ['ROLE_CLIENTE'];
      return true;
    }

    return false;
  }*/

  /*/logout(): void {
    this.isLoggedIn = false;
    this.userName = '';
    this.roles = [];
  }*/

  public isUserLoggedIn(): boolean {
    return this.isLoggedIn;
  }

  public getUserName(): string | null {
    return this.getUser()?.nombreusuario|| null;
  }

  getUserRoles(): string[] | null {
      const rolesString = localStorage.getItem(ROLES_KEY);
      if (rolesString) {
        console.log("roles: ",rolesString)
        return JSON.parse(rolesString);
      }
      return [];
    //return this.getUser()?.roles || null;
  }

   /*getUserRoles(): string[] {
    const user = this.getUser();
    if (!user || !user.roles) return [];
    return user.roles.map(r => r.authority); // extrae "ROLE_X"
  }*/


  hasRole(role: string): boolean | undefined {
    //this.roles=localStorage.getItem(ROLES_KEY);
        console.log("tiene el rol?",role)
    return this.getUserRoles()?.includes(role);
  }
}
