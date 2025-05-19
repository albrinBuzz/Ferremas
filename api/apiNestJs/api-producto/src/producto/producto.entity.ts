// src/producto/producto.entity.ts
import { Entity, PrimaryGeneratedColumn, Column, ManyToOne, JoinColumn } from 'typeorm';
import { Categoria } from '../categoria/categoria.entity';

@Entity()
export class Producto {
  @PrimaryGeneratedColumn({ name: 'id_producto' })
  id: number;

  @Column()
  nombre: string;

  @Column('text')
  descripcion: string;

  @Column()
  precio: number;

  @Column()
  marca: string;

  @Column()
  imagen: string;

  @ManyToOne(() => Categoria, (categoria) => categoria.productos)
  @JoinColumn({ name: 'id_categoria' })
  categoria: Categoria;

}
