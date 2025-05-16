from fastapi import Depends, HTTPException, APIRouter
from sqlalchemy.orm import Session

import crud
from database import SessionLocal, engine, Base
from schemas import UsuarioCreate, UsuarioOut, UsuarioUpdate, RolCreate, RolOut

Base.metadata.create_all(bind=engine)

app = APIRouter(
    prefix="/usuarios",
    tags=["Usuarios"]
)


# Dependencia para obtener sesión de base de datos
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# Crear un usuario
@app.post("/", response_model=UsuarioOut, status_code=201)
def crear_usuario(usuario: UsuarioCreate, db: Session = Depends(get_db)):
    if crud.get_usuario(db, usuario.rut_usuario):
        raise HTTPException(status_code=400, detail="El usuario ya existe")
    return crud.crear_usuario(db, usuario)

# Obtener un usuario por RUT
@app.get("/usuarios/{rut}", response_model=UsuarioOut)
def obtener_usuario(rut: str, db: Session = Depends(get_db)):
    usuario = crud.get_usuario(db, rut)
    if not usuario:
        raise HTTPException(status_code=404, detail="Usuario no encontrado")
    return usuario

# Actualizar un usuario
@app.put("/usuarios/{rut}", response_model=UsuarioOut)
def actualizar_usuario(rut: str, usuario: UsuarioUpdate, db: Session = Depends(get_db)):
    updated = crud.actualizar_usuario(db, rut, usuario)
    if not updated:
        raise HTTPException(status_code=404, detail="Usuario no encontrado")
    return updated

# Obtener todos los usuarios
@app.get("/usuarios", response_model=list[UsuarioOut])
def listar_usuarios(db: Session = Depends(get_db)):
    try:
        usuarios = crud.get_usuarios(db)
        print(f"Usuarios obtenidos: {usuarios}")
        return usuarios
    except Exception as e:
        print(f"Error al obtener usuarios: {e}")
        raise HTTPException(status_code=500, detail="Error al obtener usuarios")


# Eliminar un usuario
@app.delete("/usuarios/{rut}", response_model=UsuarioOut)
def eliminar_usuario(rut: str, db: Session = Depends(get_db)):
    usuario = crud.eliminar_usuario(db, rut)
    if not usuario:
        raise HTTPException(status_code=404, detail="Usuario no encontrado")
    return usuario

# Crear un rol
@app.post("/roles/", response_model=RolOut, status_code=201)
def crear_rol(rol: RolCreate, db: Session = Depends(get_db)):
    return crud.crear_rol(db, rol)

# Obtener todos los roles
@app.get("/roles/", response_model=list[RolOut])
def obtener_roles(db: Session = Depends(get_db)):
    return crud.get_roles(db)

# Asignar un rol a un usuario
@app.post("/usuarios/{rut}/roles/{id_rol}")
def asignar_rol(rut: str, id_rol: int, db: Session = Depends(get_db)):
    rol_usuario = crud.asignar_rol_a_usuario(db, rut, id_rol)
    if not rol_usuario:
        raise HTTPException(status_code=404, detail="Usuario o rol no encontrado")
    return {"mensaje": "Rol asignado con éxito"}


