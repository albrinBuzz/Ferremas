<img width="1901" height="1016" alt="TiendaMain" src="https://github.com/user-attachments/assets/f0828139-6a82-464c-a5a5-ea329442097f" /><img width="1901" height="1016" alt="TiendaMain" src="https://github.com/user-attachments/assets/cc4acab6-d6c5-4b61-ad3e-d77f9a1c6e39" /># FERREMAS - Sistema de Comercio Electrónico

## 📌 Contexto del Proyecto

FERREMAS es una reconocida distribuidora de productos de ferretería y construcción con presencia en Santiago y varias regiones de Chile. Fundada en los años 80, la empresa ha crecido junto con la industria de la construcción, ofreciendo una amplia gama de herramientas, materiales eléctricos, pinturas y artículos de seguridad. Ante los desafíos que trajo la pandemia del 2020, FERREMAS identificó una necesidad crítica: **la falta de una plataforma de ventas en línea**.

Este proyecto tiene como objetivo desarrollar una solución de comercio electrónico que permita a FERREMAS adaptarse a los nuevos hábitos de consumo, ofreciendo a sus clientes la posibilidad de comprar en línea y mejorar la eficiencia de los procesos internos mediante herramientas digitales para su personal.

---

## 🎯 Objetivos del Sistema

- Implementar una plataforma web funcional, moderna y segura.
- Permitir ventas en línea a través de un sistema de carrito de compras.
- Facilitar el trabajo de vendedores, bodegueros, contadores y administradores con módulos personalizados.
- Integrar funcionalidades para retiro en tienda.
- Brindar múltiples opciones de pago (débito, crédito, transferencia).

---

## Imagenes de la tienda

![tienda.png](/imagenes%2Ftienda.png)
![TiendaMain.png](/imagenes%2FTiendaMain.png)


## 🔧 Tecnologías Utilizadas

- **Front-end**:
    - JSF con PrimeFaces
    - HTML5, CSS3, Bootstrap
    - [![My Skills](https://skillicons.dev/icons?i=html,css,js,bootstrap&theme=dark)](https://skillicons.dev)

- **Back-end**:
    - Jakarta EE, Jakarta Faces
    - [![My Skills](https://skillicons.dev/icons?i=java&theme=dark)](https://skillicons.dev)

- **Base de Datos**:
    - PostgreSQL
    - [![My Skills](https://skillicons.dev/icons?i=postgres&theme=dark)](https://skillicons.dev)

- **IDE sugeridas**:
    - Eclipse, IntelliJ IDEA, NetBeans, PgAdmin
    - [![My Skills](https://skillicons.dev/icons?i=idea,eclipse&theme=dark)](https://skillicons.dev)

  - **Contenedores y Virtualización**:
  - Docker
  - [![Docker](https://skillicons.dev/icons?i=docker&theme=dark)](https://skillicons.dev)


---

## ⚙️ Requisitos de Ejecución

Para ejecutar correctamente el sistema de FERREMAS, se debe contar con el siguiente entorno de desarrollo y configuración:

### Entorno Recomendado

- **JDK**: Java 17+
- **Base de Datos**: PostgreSQL 13 o superior, ejecución en local
- **Editor/IDE**: Eclipse, IntelliJ IDEA o NetBeans
- **Sistema de Build**: Maven 3.8+

## 🛠️ Instalación

### Paso 1: Clonar el repositorio

Clona el repositorio de GitHub en tu máquina local. Si aún no tienes Git instalado, puedes obtenerlo desde [aquí](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git).

```bash
git clone https://github.com/albrinBuzz/Ferremas.git
cd Ferremas
```


## Ejecucion

Windos

```bash
  ./start.bat 
```

unix/MacOs

```bash
   ./start.sh
```

unix/MacOs/Windos

```bash
   docker build -t ferremas-commerce .
   docker run -p 8080:8080 ferremas-commerce
```

ruta
```bash
   http://localhost:8080/home/index.xhtml
```


### Errores Comunes

A continuación se detallan algunos errores frecuentes al ejecutar el sistema FERREMAS, especialmente relacionados con la conexión a la base de datos:

## Error tablas o tabla inexistente

ejemplo:
```java
Caused by: org.hibernate.tool.schema.spi.SchemaManagementException: Schema-validation: missing table [categoria]
```

causas:

1. la base de datos configurada, no tiene todas la tablas

✅ Solución:
1. ejecutar el archivo tablas.sql ubicando en [tablas.sql](dataBase%2Ftablas.sql) se deben insertas todas la tablas, como asi tambien
se deben insertar los archivos [procedimientos.sql](dataBase%2Fprocedimientos.sql) y [trigger.sql](dataBase%2Ftrigger.sql) y [poblado.sql](dataBase%2Fpoblado.sql)

## Error host no econtrado
ejemplo:
```java
Caused by: java.net.NoRouteToHostException: No existe ninguna ruta hasta el `host'
Caused by: org.postgresql.util.PSQLException: El intento de conexión falló.
org.hibernate.exception.JDBCConnectionException: unable to obtain isolated JDBC connection [El intento de conexión falló.] [n/a]
Caused by: org.hibernate.exception.JDBCConnectionException: Unable to open JDBC Connection for DDL execution [El intento de conexión falló.] [n/a]
```

causas:

1. El host especificado en el archivo application-dev.properties o application-prod.properties está incorrecto.
2. se esta usando dev y no prod en el archivo .properties, osea se esta conectado a un base de datos local inexistente


✅ Solución:




### Configuración de la Base de Datos

La aplicación FERREMAS permite conectarse a una base de datos en modo local o en la nube, dependiendo del perfil que esté activo. Esto se define en el archivo de configuración:

[application.properties](src%2Fmain%2Fresources%2Fapplication.properties)


## Archivo .propierties

```text
spring.profiles.active=dev    # ➤ Modo local (desarrollo)
spring.profiles.active=prod  # ➤ Modo nube (producción)

```

## Configurar la base de datos local
1. Crea una base de datos local en PostgreSQL:
```sql
CREATE DATABASE ferremas_db;
```
2. Verifica que el perfil activo esté en modo dev, en [application.properties](src%2Fmain%2Fresources%2Fapplication.properties)
```text
spring.profiles.active=dev    # ➤ Modo local (desarrollo)
```
3. Configura tus credenciales locales en: [application-dev.properties](src%2Fmain%2Fresources%2Fapplication-dev.properties)

```sql
spring.datasource.url=jdbc:postgresql://localhost:5432/ferremas_db
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
```

---
