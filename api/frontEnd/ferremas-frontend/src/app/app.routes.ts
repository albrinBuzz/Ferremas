import { Routes } from '@angular/router';
import { ProductoListComponent } from './components/producto-list/producto-list.component';
import { ProductoFormComponent } from './components/producto-form/producto-form.component';
import { LoginComponent } from './components/login/login.component';


export const routes: Routes = [
  { path: 'productos', component: ProductoListComponent },
   { path: 'login', component: LoginComponent },
  { path: 'productos/nuevo', component: ProductoFormComponent },
  { path: 'productos/editar/:id', component: ProductoFormComponent },
  { path: '', redirectTo: '/productos', pathMatch: 'full' },
  { path: '**', redirectTo: '/productos' }
];
