import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { ProductoListComponent } from './components/producto-list/producto-list.component';
import { HeaderComponent } from './components/header/header.component';
import { ProductoFormComponent } from './components/producto-form/producto-form.component';
import { FooterComponent } from './components/footer/footer.component';




@NgModule({
  declarations: [
    AppComponent,
       HeaderComponent,
        FooterComponent,
    ProductoListComponent,
    ProductoFormComponent
  ],
  imports: [
    BrowserModule,

    FormsModule,
    HttpClientModule,
       // RouterModule.forRoot(routes) // Aquí cargas las rutas
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
