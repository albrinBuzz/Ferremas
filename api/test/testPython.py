import requests

# Paso 1: Login para obtener el token
login_url = 'http://localhost:8080/auth/login'
login_data = {
    'username': 'cliente1@ferremas.cl',
    'password': '123'
}

login_response = requests.post(login_url, json=login_data)
token = login_response.json()['token']

# Paso 2: Acceder al endpoint protegido
pedidos_url = 'http://localhost:8080/pedidos'
headers = {
    'Authorization': f'Bearer {token}'
}

response = requests.get(pedidos_url, headers=headers)

if response.status_code == 200:
    print("Pedidos del usuario:")
    print(response.json())
else:
    print(f"Error ({response.status_code}): {response.text}")
