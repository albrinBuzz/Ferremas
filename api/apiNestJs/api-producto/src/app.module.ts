import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm'; // Importa TypeOrmModule
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { CategoriaModule } from './categoria/categoria.module';
import { ProductoModule } from './producto/producto.module';
import { Categoria } from './categoria/categoria.entity'; // Asegúrate de importar las entidades
import { Producto } from './producto/producto.entity';

@Module({
  imports: [
    // Configura TypeOrmModule con tus parámetros de conexión
    TypeOrmModule.forRoot({
      type: 'postgres', // Motor de base de datos (puede ser 'mysql', 'sqlite', etc.)
      host: 'aws-0-sa-east-1.pooler.supabase.com', // Dirección del servidor de base de datos
      port: 5432, // Puerto del servidor de base de datos
      username: 'postgres.pbhhgprldwuuvcjwsjmr', // Usuario de PostgreSQL
      password: '3wX%r29V#D09', // Contraseña de PostgreSQL
      database: 'postgres', // Nombre de la base de datos
      entities: [Categoria, Producto], // Asegúrate de agregar todas las entidades que vas a utilizar
      synchronize: false, // Cambia a `false` en producción para evitar que se borren tablas accidentalmente
    }),
    CategoriaModule, // Módulos importados
    ProductoModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
