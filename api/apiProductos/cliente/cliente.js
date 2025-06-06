const formData = new FormData();
formData.append('producto', JSON.stringify({
  nombre: 'Taladro Bosch',
  precio: 199.99,
  descripcion: 'Taladro de impacto',
  stock: 15
}));
formData.append('imagen', fileInput.files[0]); // fileInput = input HTML

fetch('http://localhost:8080/api/productos/crear', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error('Error:', error));
