from pydantic import BaseModel
from typing import List, Optional

# Esquema para la creación de un usuario
class UsuarioCreate(BaseModel):
    rut_usuario: str
    nombreUsuario: str
    contrasena: str
    correo: str

# Esquema de salida de usuario
class UsuarioOut(BaseModel):
    rut_usuario: str
    nombreUsuario: str
    correo: str
    roles: List[str] = []  # Lista de roles del usuario

    class Config:
        from_attributes = True  # Cambiar de 'orm_mode' a 'from_attributes'


from pydantic import BaseModel
from typing import List

# Esquema para la respuesta del Usuario
class UsuarioResponse(BaseModel):
    rut_usuario: str
    nombreUsuario: str  # Debe coincidir con el nombre del campo en tu base de datos y el modelo
    correo: str

    class Config:
        # Esto indica que FastAPI debe utilizar el nombre exacto de las columnas tal como se encuentran en el modelo de SQLAlchemy
        orm_mode = True


# Esquema para la actualización de un usuario
class UsuarioUpdate(BaseModel):
    nombreUsuario: Optional[str]
    contrasena: Optional[str]
    correo: Optional[str]

# Esquema para la creación de un rol
class RolCreate(BaseModel):
    nombre: str
    descripcion: Optional[str]

# Esquema de salida de rol
class RolOut(BaseModel):
    id_rol: int
    nombre: str
    descripcion: Optional[str]

    class Config:
        from_attributes = True  # Cambiar de 'orm_mode' a 'from_attributes'
