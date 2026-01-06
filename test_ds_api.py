import requests
import json

# URL del equipo de Data Science
url = "https://aa18360bdde6.ngrok-free.app/sentiment"

# Datos de prueba
data = {
    "text": "El servicio fue excelente, muy recomendado"
}

try:
    # Hacer la petición POST
    response = requests.post(url, json=data, headers={'Content-Type': 'application/json'})
    
    print(f"Status Code: {response.status_code}")
    print(f"Response Headers: {response.headers}")
    print(f"Response Body: {response.text}")
    
    if response.status_code == 200:
        result = response.json()
        print(f"\n✅ SUCCESS!")
        print(f"Texto: {result.get('texto')}")
        print(f"Predicción: {result.get('prevision')}")
        print(f"Probabilidad: {result.get('probabilidad')}")
    else:
        print(f"\n❌ ERROR: {response.status_code}")
        
except Exception as e:
    print(f"❌ Exception: {e}")
