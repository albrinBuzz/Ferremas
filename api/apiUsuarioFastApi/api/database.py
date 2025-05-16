from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base

DATABASE_URL = "postgresql://postgres.pbhhgprldwuuvcjwsjmr:3wX%r29V#D09@aws-0-sa-east-1.pooler.supabase.com:5432/postgres"
#DATABASE_URL = "postgresql://postgres.pbhhgprldwuuvcjwsjmr:3wX%r29V#D09@aws-0-sa-east-1.pooler.supabase.com:5432/ferremasDB"
#DATABASE_URL = "postgresql://ferremas:ferremas@localhost:5432/ferremasDB"
#DATABASE_URL = "postgresql://usuario:password@localhost:5432/tu_basedatos"

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(bind=engine, autoflush=False, autocommit=False)
Base = declarative_base()
