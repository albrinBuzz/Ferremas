import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductoService } from '../../services/producto.service';
import { Producto } from '../../models/producto.model';
import { Categoria } from '../../models/categoria.model';
import { HttpClient } from '@angular/common/http';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-producto-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './producto-form.component.html'
})

export class ProductoFormComponent implements OnInit {
  producto: Producto = {
    nombre: '',
    descripcion: '',
    marca: '',
    precio: 0,
    imagen: '',
    categoria: { idCategoria: 0, nombre: '', descripcion: '' }
  };

  categorias: Categoria[] = [];
  isEdit = false;

  constructor(
    private productoService: ProductoService,
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit() {
    this.cargarCategorias();

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
       const idNum = +id;
      console.log('🛠️ Editando producto con ID:', idNum); // Puedes dejar esto para probar

      this.productoService.getById(idNum).subscribe(response => {
        this.producto = response.data;
      });
      
    }
  }

  cargarCategorias() {
    this.http.get<any>('http://localhost:8080/api/categorias').subscribe(response => {
      this.categorias = response.data;
    });
  }

  guardarProducto() {
    if (this.isEdit) {
      this.productoService.update(this.producto.idProducto!, this.producto).subscribe(() => {
        this.router.navigate(['/productos']);
      });
    } else {
      this.productoService.create(this.producto).subscribe(() => {
        this.router.navigate(['/productos']);
      });
    }
  }
}
