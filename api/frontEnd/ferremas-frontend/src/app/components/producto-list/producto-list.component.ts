import { Component, OnInit } from '@angular/core';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { Router } from '@angular/router';

import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-producto-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './producto-list.component.html'
})

export class ProductoListComponent implements OnInit {
  productos: Producto[] = [];

  constructor(private productoService: ProductoService, private router: Router) {}

  ngOnInit() {
    this.cargarProductos();
  }

  cargarProductos() {
    this.productoService.getAll().subscribe(response => {
      this.productos = response.data;
    });
  }

  editarProducto(id: number) {
    this.router.navigate(['/productos/editar', id]);
  }

  eliminarProducto(id: number) {
    if (confirm('¿Seguro que desea eliminar este producto?')) {
      this.productoService.delete(id).subscribe(() => this.cargarProductos());
    }
  }
}
