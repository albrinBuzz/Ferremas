from sqlalchemy.orm import Session
from models import Producto  # Asegúrate de importar el modelo de Producto
from schemas import ProductoCreate, ProductoUpdate

# Crear un producto
def crear_producto(db: Session, producto: ProductoCreate):
    db_producto = Producto(
        nombre=producto.nombre,
        descripcion=producto.descripcion,
        precio=producto.precio,
        marca=producto.marca,
        imagen=producto.imagen,
        id_categoria=producto.id_categoria
    )
    db.add(db_producto)
    db.commit()
    db.refresh(db_producto)
    return db_producto

# Obtener todos los productos
def get_productos(db: Session):
    return db.query(Producto).all()

# Obtener un producto por ID
def get_producto(db: Session, id_producto: int):
    return db.query(Producto).filter(Producto.id_producto == id_producto).first()

# Actualizar un producto
def actualizar_producto(db: Session, id_producto: int, producto_update: ProductoUpdate):
    db_producto = db.query(Producto).filter(Producto.id_producto == id_producto).first()
    if db_producto:
        if producto_update.nombre:
            db_producto.nombre = producto_update.nombre
        if producto_update.descripcion:
            db_producto.descripcion = producto_update.descripcion
        if producto_update.precio:
            db_producto.precio = producto_update.precio
        if producto_update.marca:
            db_producto.marca = producto_update.marca
        if producto_update.imagen:
            db_producto.imagen = producto_update.imagen
        if producto_update.id_categoria:
            db_producto.id_categoria = producto_update.id_categoria

        db.commit()
        db.refresh(db_producto)
        return db_producto
    return None

# Eliminar un producto
def eliminar_producto(db: Session, id_producto: int):
    db_producto = db.query(Producto).filter(Producto.id_producto == id_producto).first()
    if db_producto:
        db.delete(db_producto)
        db.commit()
        return db_producto
    return None