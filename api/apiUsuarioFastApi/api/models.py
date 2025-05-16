from sqlalchemy import Column, String, Integer, ForeignKey
from sqlalchemy.orm import relationship
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()

# Modelo de Usuario
class Usuario(Base):
    __tablename__ = 'usuario'  # Nombre de la tabla en la base de datos

    rut_usuario = Column(String(15), primary_key=True)
    nombreUsuario = Column('nombreusuario',String(50))  # Cambiado de nombreusuario a nombreUsuario
    contrasena = Column(String(128))
    correo = Column(String(50), unique=True)

    # Relación con RolUsuario
    rolusuarios = relationship("RolUsuario", back_populates="usuario")

    def __repr__(self):
        return f"<Usuario(rut_usuario={self.rut_usuario}, nombreUsuario={self.nombreUsuario}, correo={self.correo})>"

# Modelo de Rol
class Rol(Base):
    __tablename__ = 'rol'  # Nombre de la tabla en la base de datos

    id_rol = Column(Integer, primary_key=True)
    nombre = Column(String(50))
    descripcion = Column(String(120))

    # Relación con RolUsuario
    rolusuarios = relationship("RolUsuario", back_populates="rol")

    def __repr__(self):
        return f"<Rol(id_rol={self.id_rol}, nombre={self.nombre})>"


# Modelo de RolUsuario
class RolUsuario(Base):
    __tablename__ = 'rolusuario'  # Nombre de la tabla en la base de datos

    id_rol = Column(Integer, ForeignKey('rol.id_rol'), primary_key=True)
    rut_usuario = Column(String(15), ForeignKey('usuario.rut_usuario'), primary_key=True)

    # Relaciones con Usuario y Rol
    usuario = relationship("Usuario", back_populates="rolusuarios")
    rol = relationship("Rol", back_populates="rolusuarios")

    def __repr__(self):
        return f"<RolUsuario(id_rol={self.id_rol}, rut_usuario={self.rut_usuario})>"
