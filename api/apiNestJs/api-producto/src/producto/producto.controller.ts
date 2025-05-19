// src/producto/producto.controller.ts
import { Controller, Get, Post, Body, Param, Put, Delete } from '@nestjs/common';
import { ProductoService } from './producto.service';
import { Producto } from './producto.entity';

@Controller('productos')
export class ProductoController {
  constructor(private readonly service: ProductoService) {}

  @Get()
  getAll(): Promise<Producto[]> {
    return this.service.findAll();
  }

 @Get(':id')
  getOne(@Param('id') id: number): Promise<Producto | null> {  // Aquí cambiamos el tipo
    return this.service.findOne(id);
  }

  @Post()
  create(@Body() data: Partial<Producto>): Promise<Producto> {
    return this.service.create(data);
  }

  @Put(':id')
  update(@Param('id') id: number, @Body() data: Partial<Producto>) {
    return this.service.update(id, data);
  }

  @Delete(':id')
  remove(@Param('id') id: number) {
    return this.service.remove(id);
  }
}
