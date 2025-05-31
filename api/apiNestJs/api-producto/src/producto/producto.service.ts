// src/producto/producto.service.ts
import { Injectable } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Producto } from './producto.entity';
import { Repository } from 'typeorm';

@Injectable()
export class ProductoService {
  constructor(
    @InjectRepository(Producto)
    private readonly repo: Repository<Producto>,
  ) {}

  findAll() {
    return this.repo.find({ relations: ['categoria'] });
  }

  findOne(id: number) {
    return this.repo.findOne({ where: { id }, relations: ['categoria'] });
  }

  create(data: Partial<Producto>) {
    const producto = this.repo.create(data);
    return this.repo.save(producto);
  }

  async update(id: number, data: Partial<Producto>) {
    await this.repo.update(id, data);
    return this.repo.findOne({ where: { id } });
  }

  async remove(id: number) {
    await this.repo.delete(id);
    return { deleted: true };
  }
}
