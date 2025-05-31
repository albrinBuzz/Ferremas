import { Categoria } from './categoria.model';

export interface Producto {
  idProducto?: number;
  nombre: string;
  descripcion: string;
  marca: string;
  precio: number;
  imagen: string;
  categoria: Categoria;
}
