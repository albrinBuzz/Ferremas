// src/main.ts
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // Configuración de Swagger
  const options = new DocumentBuilder()
    .setTitle('API de Productos')
    .setDescription('API para controlaar los productos, de ferremas')
    .setVersion('1.0')
    .addTag('productos')  // Etiqueta que puedes utilizar para agrupar las rutas relacionadas
    .build();
  const document = SwaggerModule.createDocument(app, options);
  SwaggerModule.setup('api', app, document);  // Ruta donde estará disponible la documentación Swagger

  await app.listen(3000);
}
bootstrap();
