from sqlalchemy.orm import Session
from models import Categoria  # Asegúrate de importar el modelo de Categoria
from schemas import CategoriaCreate, CategoriaUpdate

# Crear una categoría
def crear_categoria(db: Session, categoria: CategoriaCreate):
    db_categoria = Categoria(
        nombre=categoria.nombre,
        descripcion=categoria.descripcion
    )
    db.add(db_categoria)
    db.commit()
    db.refresh(db_categoria)
    return db_categoria

# Obtener todas las categorías
def get_categorias(db: Session, skip: int = 0, limit: int = 100):
    return db.query(Categoria).offset(skip).limit(limit).all()

# Obtener una categoría por ID
def get_categoria(db: Session, id_categoria: int):
    return db.query(Categoria).filter(Categoria.id_categoria == id_categoria).first()

# Actualizar una categoría
def actualizar_categoria(db: Session, id_categoria: int, categoria_update: CategoriaUpdate):
    db_categoria = db.query(Categoria).filter(Categoria.id_categoria == id_categoria).first()
    if db_categoria:
        if categoria_update.nombre:
            db_categoria.nombre = categoria_update.nombre
        if categoria_update.descripcion:
            db_categoria.descripcion = categoria_update.descripcion
        db.commit()
        db.refresh(db_categoria)
        return db_categoria
    return None

# Eliminar una categoría
def eliminar_categoria(db: Session, id_categoria: int):
    db_categoria = db.query(Categoria).filter(Categoria.id_categoria == id_categoria).first()
    if db_categoria:
        db.delete(db_categoria)
        db.commit()
        return db_categoria
    return None
