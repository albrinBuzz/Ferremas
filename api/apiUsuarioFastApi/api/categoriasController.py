from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
import crudCategorias as crud
from database import SessionLocal
from schemas import CategoriaCreate, CategoriaOut, CategoriaUpdate

# Dependencia para obtener sesión de base de datos
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Creación de la API de categorías
router = APIRouter(
    prefix="/categorias",
    tags=["Categorías"]
)

# Crear una categoría
@router.post("/", response_model=CategoriaOut, status_code=201)
def crear_categoria_view(categoria: CategoriaCreate, db: Session = Depends(get_db)):
    return crud.crear_categoria(db, categoria)

# Obtener todas las categorías
@router.get("/", response_model=list[CategoriaOut])
def listar_categorias(db: Session = Depends(get_db), skip: int = 0, limit: int = 100):
    return crud.get_categorias(db, skip=skip, limit=limit)

# Obtener una categoría por ID
@router.get("/{id_categoria}", response_model=CategoriaOut)
def obtener_categoria(id_categoria: int, db: Session = Depends(get_db)):
    db_categoria = crud.get_categoria(db, id_categoria)
    if not db_categoria:
        raise HTTPException(status_code=404, detail="Categoría no encontrada")
    return db_categoria

# Actualizar una categoría
@router.put("/{id_categoria}", response_model=CategoriaOut)
def actualizar_categoria_view(id_categoria: int, categoria: CategoriaUpdate, db: Session = Depends(get_db)):
    db_categoria = crud.actualizar_categoria(db, id_categoria, categoria)
    if not db_categoria:
        raise HTTPException(status_code=404, detail="Categoría no encontrada")
    return db_categoria

# Eliminar una categoría
@router.delete("/{id_categoria}", response_model=CategoriaOut)
def eliminar_categoria_view(id_categoria: int, db: Session = Depends(get_db)):
    db_categoria = crud.eliminar_categoria(db, id_categoria)
    if not db_categoria:
        raise HTTPException(status_code=404, detail="Categoría no encontrada")
    return db_categoria
