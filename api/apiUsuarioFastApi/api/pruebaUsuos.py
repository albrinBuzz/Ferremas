from sqlalchemy import create_engine, text
from sqlalchemy.orm import sessionmaker
from database import Base
# Configuración de la base de datos
DATABASE_URL = "postgresql://postgres.pbhhgprldwuuvcjwsjmr:3wX%r29V#D09@aws-0-sa-east-1.pooler.supabase.com:5432/postgres"

# Crea el motor
engine = create_engine(DATABASE_URL)

# Crea la sesión
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# Intenta realizar una consulta simple
def probar_conexion():
    db = SessionLocal()

    try:
        # Aquí intentamos obtener el primer registro de la tabla "cliente"
        result = db.execute(text('select * from usuario'))  # Usa text() para envolver la consulta
        for row in result:
            print(row)
        print("Conexión exitosa")
    except Exception as e:
        print(f"Error al conectar: {e}")
    finally:
        db.close()

# Llamada a la función de prueba
probar_conexion()
