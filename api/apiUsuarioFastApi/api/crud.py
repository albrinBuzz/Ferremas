from sqlalchemy.orm import Session
from models import Usuario, Rol, RolUsuario
from schemas import UsuarioCreate, UsuarioUpdate, RolCreate

# Función para crear un usuario
def crear_usuario(db: Session, usuario: UsuarioCreate):
    db_usuario = Usuario(
        rut_usuario=usuario.rut_usuario,
        nombreUsuario=usuario.nombreUsuario,
        contrasena=usuario.contrasena,
        correo=usuario.correo
    )
    db.add(db_usuario)
    db.commit()
    db.refresh(db_usuario)
    return db_usuario

# Función para obtener un usuario por su rut
def get_usuario(db: Session, rut_usuario: str):
    return db.query(Usuario).filter(Usuario.rut_usuario == rut_usuario).first()

# Función para obtener todos los usuarios
def get_usuarios(db: Session):
    try:
        usuarios = db.query(Usuario).all()
        print(f"Usuarios obtenidos de la base de datos: {usuarios}")
        return usuarios
    except Exception as e:
        print(f"Error al obtener usuarios: {e}")
        raise

# Función para actualizar un usuario
def actualizar_usuario(db: Session, rut_usuario: str, usuario_update: UsuarioUpdate):
    db_usuario = db.query(Usuario).filter(Usuario.rut_usuario == rut_usuario).first()
    if db_usuario:
        if usuario_update.nombreUsuario:
            db_usuario.nombreUsuario = usuario_update.nombreUsuario
        if usuario_update.contrasena:
            db_usuario.contrasena = usuario_update.contrasena
        if usuario_update.correo:
            db_usuario.correo = usuario_update.correo
        db.commit()
        db.refresh(db_usuario)
        return db_usuario
    return None

# Función para eliminar un usuario
def eliminar_usuario(db: Session, rut_usuario: str):
    db_usuario = db.query(Usuario).filter(Usuario.rut_usuario == rut_usuario).first()
    if db_usuario:
        db.delete(db_usuario)
        db.commit()
        return db_usuario
    return None

# Función para crear un rol
def crear_rol(db: Session, rol: RolCreate):
    db_rol = Rol(nombre=rol.nombre, descripcion=rol.descripcion)
    db.add(db_rol)
    db.commit()
    db.refresh(db_rol)
    return db_rol

# Función para obtener todos los roles
def get_roles(db: Session):
    return db.query(Rol).all()

# Función para asignar un rol a un usuario
def asignar_rol_a_usuario(db: Session, rut_usuario: str, id_rol: int):
    db_usuario = db.query(Usuario).filter(Usuario.rut_usuario == rut_usuario).first()
    db_rol = db.query(Rol).filter(Rol.id_rol == id_rol).first()
    if db_usuario and db_rol:
        db_rol_usuario = RolUsuario(rut_usuario=rut_usuario, id_rol=id_rol)
        db.add(db_rol_usuario)
        db.commit()
        return db_rol_usuario
    return None
