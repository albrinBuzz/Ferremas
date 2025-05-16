from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
import crudProductos as crud
from database import SessionLocal
from schemas import ProductoCreate, ProductoOut, ProductoUpdate

# Dependencia para obtener sesión de base de datos
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Creación de la API de productos
router = APIRouter(
    prefix="/productos",
    tags=["Productos"]
)

# Crear un producto
@router.post("/", response_model=ProductoOut, status_code=201)
def crear_producto_view(producto: ProductoCreate, db: Session = Depends(get_db)):
    return crud.crear_producto(db, producto)

# Obtener todos los productos
@router.get("/", response_model=list[ProductoOut])
def listar_productos(db: Session = Depends(get_db), skip: int = 0, limit: int = 100):
    return crud.get_productos(db, skip=skip, limit=limit)

# Obtener un producto por ID
@router.get("/{id_producto}", response_model=ProductoOut)
def obtener_producto(id_producto: int, db: Session = Depends(get_db)):
    db_producto = crud.get_producto(db, id_producto)
    if not db_producto:
        raise HTTPException(status_code=404, detail="Producto no encontrado")
    return db_producto

# Actualizar un producto
@router.put("/{id_producto}", response_model=ProductoOut)
def actualizar_producto_view(id_producto: int, producto: ProductoUpdate, db: Session = Depends(get_db)):
    db_producto = crud.actualizar_producto(db, id_producto, producto)
    if not db_producto:
        raise HTTPException(status_code=404, detail="Producto no encontrado")
    return db_producto

# Eliminar un producto
@router.delete("/{id_producto}", response_model=ProductoOut)
def eliminar_producto_view(id_producto: int, db: Session = Depends(get_db)):
    db_producto = crud.eliminar_producto(db, id_producto)
    if not db_producto:
        raise HTTPException(status_code=404, detail="Producto no encontrado")
    return db_producto