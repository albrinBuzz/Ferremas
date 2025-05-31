// API Base URL
const apiUrl = "http://127.0.0.1:8000";

// Función para obtener los roles
async function getRoles() {
    const response = await fetch(`${apiUrl}/roles/`);
    const roles = await response.json();

    const roleSelect = document.getElementById("selectRol");
    roleSelect.innerHTML = ""; // Limpiar el select antes de agregar nuevos roles
    roles.forEach(role => {
        const option = document.createElement("option");
        option.value = role.id_rol;
        option.textContent = role.nombre;
        roleSelect.appendChild(option);
    });
}

// Función para obtener los usuarios
async function getUsuarios() {
    const response = await fetch(`${apiUrl}/usuarios/`);
    const usuarios = await response.json();

    const usuarioSelect = document.getElementById("selectUsuario");
    usuarioSelect.innerHTML = ""; // Limpiar el select antes de agregar nuevos usuarios
    usuarios.forEach(usuario => {
        const option = document.createElement("option");
        option.value = usuario.rut_usuario;
        option.textContent = `${usuario.nombreUsuario} (${usuario.rut_usuario})`;
        usuarioSelect.appendChild(option);
    });
}

// Función para crear un nuevo usuario
document.getElementById("createUserForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const rut_usuario = document.getElementById("rut_usuario").value;
    const nombreUsuario = document.getElementById("nombreUsuario").value;
    const contrasena = document.getElementById("contrasena").value;
    const correo = document.getElementById("correo").value;

    const usuarioData = {
        rut_usuario,
        nombreUsuario,
        contrasena,
        correo
    };

    const response = await fetch(`${apiUrl}/usuarios/`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(usuarioData)
    });

    if (response.ok) {
        alert("Usuario creado con éxito");
        getUsuarios(); // Recargar la lista de usuarios
    } else {
        alert("Error al crear el usuario");
    }
});

// Función para asignar un rol a un usuario
document.getElementById("assignRoleBtn").addEventListener("click", async () => {
    const rut_usuario = document.getElementById("selectUsuario").value;
    const id_rol = document.getElementById("selectRol").value;

    if (!rut_usuario || !id_rol) {
        alert("Por favor, seleccione un usuario y un rol.");
        return;
    }

    const response = await fetch(`${apiUrl}/usuarios/${rut_usuario}/roles/${id_rol}`, {
        method: "POST"
    });

    if (response.ok) {
        alert("Rol asignado con éxito");
    } else {
        alert("Error al asignar el rol");
    }
});

// Inicializar los datos al cargar la página
window.onload = async () => {
    await getRoles();
    await getUsuarios();
};
