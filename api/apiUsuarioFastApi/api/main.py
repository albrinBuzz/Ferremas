from fastapi import FastAPI
import  apiRest as usuarios
from fastapi.middleware.cors import CORSMiddleware
from productosController import router as productos_router  # Asegúrate de importar el archivo de rutas de productos
from categoriasController import router as categorias_router  # Asegúrate de importar el archivo de rutas de categorías

app = FastAPI(
    title="API de Gestión de Usuarios",
    version="1.0.0",
    description=(
        "Una API"
        "desarrollada con FastAPI, SQLAlchemy y PostgreSQL. "
        "Permite crear, actualizar, eliminar y consultar usuarios,"
    )
)



app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Permite todas las solicitudes de cualquier origen
    allow_credentials=True,
    allow_methods=["*"],  # Permite todos los métodos
    allow_headers=["*"],  # Permite todas las cabeceras
)

#Traeremos lo de las rutas(routers):
app.include_router(usuarios.app)
app.include_router(productos_router)
app.include_router(categorias_router)