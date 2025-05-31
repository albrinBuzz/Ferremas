import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';


import { AppComponent } from './app.component';

import { ProductoListComponent } from './components/producto-list/producto-list.component';
import { ProductoFormComponent } from './components/producto-form/producto-form.component';




@NgModule({
  declarations: [
    AppComponent,
    ProductoListComponent,
    ProductoFormComponent
  ],
  imports: [
    BrowserModule,

    FormsModule,
    HttpClientModule,
        RouterModule.forRoot(routes) // Aquí cargas las rutas
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
