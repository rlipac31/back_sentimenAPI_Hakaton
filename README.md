# 🚀 SentimentAPI: Java Spring Boot & Python AI Integration

Este proyecto consiste en una arquitectura robusta de microservicios (Backend + IA) diseñada para clasificar automáticamente el sentimiento de los comentarios de usuarios. El sistema integra un backend en **Spring Boot** con un motor de análisis de sentimiento desarrollado en **Python**, permitiendo el almacenamiento persistente y la generación de estadísticas.



## 🛠️ Tecnologías Utilizadas

### Backend (Java)
* **Java 17+** & **Spring Boot 3**
* **Spring Security & JWT:** Implementación de seguridad basada en roles y tokens para proteger los endpoints.
* **Spring Data JPA:** Gestión de la capa de persistencia.
* **MySQL:** Base de datos relacional para el almacenamiento de comentarios y resultados.
* **Flyway:** Control de versiones de la base de datos (Migrations).
* **Lombok:** Reducción de código boilerplate.

### AI Service (Python)
* **Flask:** Microframework para exponer el modelo como una API REST.
* **Scikit-Learn & Joblib:** Carga y ejecución del modelo de Machine Learning entrenado.
* **NLP Utils:** Procesamiento de lenguaje natural (limpieza de texto y normalización).
* * **Joblib:** Para la serialización del modelo.


---

## 🧠 El Corazón del Proyecto: Servicio de Clasificación (IA)

El componente de Python actúa como un cerebro analítico. A diferencia de un modelo básico, este servicio utiliza un **enfoque híbrido**:

1.  **Modelo de Machine Learning:** Utiliza un vectorizador y un modelo entrenado por el equipo de Data Science para predecir la probabilidad de sentimiento basándose en patrones estadísticos.
2.  **Ajuste Manual (Heurístico):** El script `app.py` implementa una función de `calcular_score_manual` que busca palabras clave críticas (como "pesadilla", "excelente", "malo") para ajustar la confianza del modelo.
3.  **Lógica de Decisión:** Combina la probabilidad de la IA con el ajuste manual para entregar una clasificación final de **POSITIVO** o **NEGATIVO**.

---

## 🔄 Flujo de Datos

1.  **Petición:** El cliente envía un comentario a la API de Spring Boot (autenticado vía JWT).
2.  **Comunicación Inter-service:** Spring Boot consume el endpoint `/predict` del servicio de Python mediante un `RestTemplate` o `WebClient`.
3.  **Procesamiento:** El servicio de Python procesa el texto, aplica el modelo y devuelve el JSON con la `prevision` y su `probabilidad`.
4.  **Persistencia:** Spring Boot recibe la respuesta, la asocia al usuario/contexto y la guarda en la base de datos **MySQL** para futuras consultas estadísticas.

---

## ⚙️ Configuración y Ejecución

### Requisitos
* JDK 17 o superior.
* Python 3.9 o superior.
* Instancia de MySQL.

### Ejecutar Servicio de Python (IA)
```bash
cd python-service
pip install flask joblib scikit-learn
python app.py
```

### 🌟 Características Principales

* **Clasificación de Sentimiento con IA:** Integración directa con **Mistral AI** para clasificar textos en POSITIVO, NEGATIVO o NEUTRO.
* **API RESTful:** Endpoints para la creación, listado y análisis de comentarios.
* **Persistencia Robusta:** Base de datos **MySQL** con gestión de esquemas de datos mediante **Flyway**.
* **Seguridad:** Implementación de autenticación basada en roles (`ADMIN` y `USER`) con Spring Security.
* **Paginación:** Manejo eficiente de grandes volúmenes de datos en las listas de comentarios.

## 🛠️ Tecnologías Utilizadas

| Categoría          | Tecnología                                    | Versión Clave |
|:-------------------|:----------------------------------------------| :--- |
| **Backend**        | Spring Boot                                   | 3.x |
| **Colap**          | Modelo entrenado por el equipo de Data Ciense |
| **Persistencia**   | MySQL                                         | Base de datos principal |
| **Migraciones DB** | Flyway                                        | Gestión de esquemas de BD |
| **Utilidades**     | Lombok                                        | Reducción de código boilerplate |
| **Seguridad**      | Spring Security                               | Autenticación y Autorización |
| **Serialización**  | Jackson (`ObjectMapper`)                      | Manejo de JSON de la IA |

## ⚙️ Configuración y Despliegue Local

### 1. Prerrequisitos

Asegúrate de tener instalado lo siguiente:

* Java Development Kit (JDK) 17 o superior.
* Maven.
* MySQL Server (o Docker para facilitar la ejecución).
* Una clave API válida para **Mistral AI** (necesaria para el servicio de clasificación).



Crea o modifica tu archivo `src/main/resources/application.properties` (o `application.yml`) y añade las siguientes configuraciones, reemplazando los valores de ejemplo con tus credenciales reales:

```properties
# CONFIGURACIÓN DE BASE DE DATOS MYSQL
spring.datasource.url=jdbc:mysql://localhost:3306/sentimentdb
spring.datasource.username=tu_usuario_mysql
spring.datasource.password=tu_contraseña_mysql
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
```
### Configuracion de base de datos usando variables de entorno
```
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
```

### 3. Rutas

## endPoinds
LISTANDO COMENTARIOS
  * formato json (Importante si no da error 403)
  [
    { "texto": "comentario........."}
  ]
  
    * Home
    http://localhost:8090/
    
    * lista los 20 ultimos comentarios y susu estadisticas
    http://localhost:8090/stats
    
    * se puede ingresar el numero deseado de comentarios a anzalizar  como parametro
    http://localhost:8090/stats?size=50
    

  * mediante archivos .CSV
  * BODY(multipart:FormData)  name:file
   http://localhost:8090/sentiment/upload-csv
  
    

## Proyecto Preeliminar(todas los requisitos basicos listos)

### URL del SentimenAPI(montado en azure capa de prueba)(YA SE PUEDEN PROBAR EN POSTMANT INSOMNIA, ETC)
```
https://lipa-sentiment-api.azurewebsites.net/sentiment
```
### URL API PYTHON
```
https://rlipac-python-api.hf.space/
```
### Repositorio de Backend Java(elaborado por el equipo backend)
```
https://github.com/rlipac31/back_sentimenAPI_Hakaton.git
```

### Repositorio de API PYTHON ( elaborado por el equipo de Data)
```
https://github.com/rlipac31/API-PYTHON_sentimentAPI.git

```
### URL  Dataset 
```
https://drive.google.com/drive/folders/1-83KeJKAytLJoX0y9JmYJ3u9yQMMdcro?usp=sharing
```



