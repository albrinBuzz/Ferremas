#!/bin/bash

# Este script es para ejecutar el proyecto en un entorno Unix/Linux

# Asegúrate de tener Maven y Java instalados
if ! command -v mvn &> /dev/null
then
    echo "Maven no está instalado. Por favor, instálalo."
    exit 1
fi

if ! command -v java &> /dev/null
then
    echo "Java no está instalado. Por favor, instálalo."
    exit 1
fi

# Crear la base de datos 'ferremas_db' en PostgreSQL (si no está creada)
# Configura el archivo de aplicación con las credenciales adecuadas antes de continuar

# Ejecutar la aplicación
echo "Ejecutando la aplicación..."
./mvnw spring-boot:run
