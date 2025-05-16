from pydantic import BaseModel
from typing import List, Optional


class ProductoBase(BaseModel):
    nombre: str
    descripcion: str
    precio: int
    marca: str
    imagen: Optional[str] = None
    id_categoria: int

class ProductoCreate(ProductoBase):
    pass

class ProductoUpdate(ProductoBase):
    nombre: Optional[str] = None
    descripcion: Optional[str] = None
    precio: Optional[int] = None
    marca: Optional[str] = None
    imagen: Optional[str] = None
    id_categoria: Optional[int] = None

class ProductoOut(ProductoBase):
    id_producto: int

    class Config:
        orm_mode = True




class CategoriaBase(BaseModel):
    nombre: str
    descripcion: Optional[str] = None

class CategoriaCreate(CategoriaBase):
    pass

class CategoriaUpdate(CategoriaBase):
    nombre: Optional[str] = None
    descripcion: Optional[str] = None

class CategoriaOut(CategoriaBase):
    id_categoria: int

    class Config:
        orm_mode = True



