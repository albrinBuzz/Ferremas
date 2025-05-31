from sqlalchemy import Column, String, Integer, ForeignKey,Text
from sqlalchemy.orm import relationship
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()



class Producto(Base):
    __tablename__ = "producto"

    id_producto = Column(Integer, primary_key=True, index=True)
    nombre = Column(String(255), nullable=False)
    descripcion = Column(Text, nullable=False)
    precio = Column(Integer, nullable=False)
    marca = Column(String(255), nullable=False)
    imagen = Column(String(255), nullable=True)
    id_categoria = Column(Integer, ForeignKey("categoria.id_categoria"), nullable=False)

    # Relación con la tabla Categoria
    categoria = relationship("Categoria", back_populates="productos")

class Categoria(Base):
    __tablename__ = "categoria"

    id_categoria = Column(Integer, primary_key=True, index=True)
    nombre = Column(String(50), nullable=False)
    descripcion = Column(Text, nullable=True)

    productos = relationship("Producto", back_populates="categoria")

