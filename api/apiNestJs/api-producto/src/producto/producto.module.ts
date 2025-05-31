// src/producto/producto.module.ts
import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { ProductoController } from './producto.controller';
import { ProductoService } from './producto.service';
import { Producto } from './producto.entity';  // Importa la entidad Producto

@Module({
  imports: [
    TypeOrmModule.forFeature([Producto]),  // Aquí se registra el repositorio para Producto
  ],
  controllers: [ProductoController],
  providers: [ProductoService],
})
export class ProductoModule {}
