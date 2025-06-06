// producto.service.ts

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private apiUrl = 'http://localhost:8080/api/productos/crear';

  constructor(private http: HttpClient) {}

  crearProducto(producto: any, imagen: File) {
    const formData = new FormData();
    formData.append('producto', JSON.stringify(producto));
    formData.append('imagen', imagen);

    return this.http.post(this.apiUrl, formData);
  }
}
const producto = {
  nombre: 'Taladro Bosch',
  precio: 199.99,
  descripcion: 'Taladro de impacto',
  stock: 15
};

this.productoService.crearProducto(producto, this.archivoSeleccionado)
  .subscribe(res => console.log(res));
