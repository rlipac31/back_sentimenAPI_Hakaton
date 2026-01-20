# SentimentAPI:Analizador de comentarios.
## Proyecto: Java Spring Boot & Python AI Integration

Este proyecto consiste en una arquitectura robusta de microservicios diseñada para clasificar automáticamente el sentimiento de los comentarios de usuarios. Integra un backend empresarial con un motor de Inteligencia Artificial para la toma de decisiones basada en datos.

---

## 🛠️ Stack Tecnológico

### Backend & Seguridad
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![JSON Web Tokens](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)

### Inteligencia Artificial & Data Science
![Python](https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54)
![Flask](https://img.shields.io/badge/flask-%23000.svg?style=for-the-badge&logo=flask&logoColor=white)
![scikit-learn](https://img.shields.io/badge/scikit--learn-%23F7931E.svg?style=for-the-badge&logo=scikit-learn&logoColor=white)
![Pandas](https://img.shields.io/badge/pandas-%23150458.svg?style=for-the-badge&logo=pandas&logoColor=white)

### Herramientas de Desarrollo
![Lombok](https://img.shields.io/badge/Lombok-red?style=for-the-badge)
![Flyway](https://img.shields.io/badge/Flyway-CC0202?style=for-the-badge&logo=flyway&logoColor=white)
![Maven](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

---

## 🧠 Inteligencia Artificial: Análisis Híbrido de Sentimientos

El componente de Python actúa como un cerebro analítico. A diferencia de un modelo básico, este servicio utiliza un **enfoque híbrido**:

1.  **Modelo de Machine Learning:** Utiliza un vectorizador y un modelo entrenado por el equipo de Data Science para predecir la probabilidad de sentimiento basándose en patrones estadísticos.
2.  **Ajuste Manual (Heurístico):** El script `app.py` implementa una función de `calcular_score_manual` que busca palabras clave críticas (como "pesadilla", "excelente", "malo") para ajustar la confianza del modelo.
3.  **Lógica de Decisión:** Combina la probabilidad de la IA con el ajuste manual para entregar una clasificación final de **POSITIVO** o **NEGATIVO**.

---

## 🔄 Flujo de Datos

1.  **Petición:** El cliente envía un comentario a la API de Spring Boot (autenticado vía JWT).
2.  **Comunicación Inter-service:** Spring Boot consume el endpoint `/predict` del servicio de Python mediante un `RestTemplate` o `WebClient`.
3.  **Procesamiento:** El servicio de Python procesa el texto, aplica el modelo y devuelve el JSON con la `prevision` y su `probabilidad`.
4.  **Persistencia:** Spring Boot recibe la respuesta y la guarda en la base de datos **MySQL** para futuras consultas estadísticas.

---

  <p align="center">
      <img src="https://res.cloudinary.com/rlipac/image/upload/v1768942169/Gemini_Generated_Image_xr1kamxr1kamxr1k_vlmoom.png" alt="Diagrama de flujo SentimentAPI"                 width=600">
  </p>

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
**Endpoint de IA:** `POST /predict`
**Response:**
```json
{
  "texto": "El servicio fue excelente y rápido",
  "prevision": "POSITIVO",
  "probabilidad": 0.95
}
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
| **Excel**          | OpenCSV                                       | Para la lectura y escritura (import and export .csv) |

## ⚙️ Configuración y Despliegue Local

### 1. Prerrequisitos

Asegúrate de tener instalado lo siguiente:

* Java Development Kit (JDK) 17 o superior.
* MySQL Server (o Docker para facilitar la ejecución)(Instar e Iniciar el servidor de MySql).
* Servicio Python Iniciado (Modelo IA ) EndPoint: [tu_direcionIP_puerto/predict](ejemplo:[http://127.0.0.1:8000/predict])(necesaria para el servicio de clasificación de lo comentarios).
* link del repositorio del Servicio de Python

````
https://github.com/rlipac31/API-PYTHON_sentimentAPI
````

## Descargar o  Clonar el proyecto desde este repositorio

````
git clone https://github.com/rlipac31/back_sentimenAPI_Hakaton.git

````

* Maven(Actualizar las dependncias sincronizando el archivo pom.xml).

Crea o modifica tu archivo `src/main/resources/application.properties` (o `application.yml`) y añade las siguientes configuraciones, reemplazando los valores de ejemplo con tus credenciales reales:

```properties
# CONFIGURACIÓN DE BASE DE DATOS MYSQL
spring.datasource.url=jdbc:mysql://localhost:3306/Nombre_tu_Base_de_Datos?createDatabaseIfNotExist=true   ## si no existe la crea
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

## Creacion de usuario (indispensable)
  <p align="left">
    <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929753/CREACION_ADMIN_pne5oa.png" alt="Inicio SentimentAPI" width=550">
    <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929753/CREACION_Hash_hawm5k.png" alt="Inicio SentimentAPI" width=450">
  </p>


## Iniciar API de java [Run File: SentimenAPiAplication ]



## endPoinds

|                       Home ([http://localhost:8090/](http://localhost:8090/))                        |                  Login ([http://localhost:8090/login](http://localhost:8090/login))                   |
|:----------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------:|
| <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929796/Home_yqfhn6.png" width="500px"> | <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929797/Login_ovqoyj.png" width="610px"> |





* Procesar comentarios individuales o lista de comentarios

* formato json (Importante si no da error 403)
  [
  { "texto": "comentario........."}
  ]

  http://localhost:8090/sentiment

   <p align="center">
     <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929766/Predict_ynd5oe.png" alt="Inicio SentimentAPI" width=900">
   </p>

| lista los 20 ultimos comentarios y susu estadisticas ([http://localhost:8090/stats](http://localhost:8090/stats)) | Listando Los ultimos (numero de comentarios ingresado por el usuario) ([http://localhost:8090/stats?size=500](http://localhost:8090/stats?size=500)) |
|:-----------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------:|
|     <img src="https://res.cloudinary.com/rlipac/image/upload/v1768934677/Stadic_20_k1ddjx.png" width="100%">      |                        <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929766/Statics_zvotfo.png" width="100%">                        |





* Analisis masivo  mediante archivos .CSV (solo se admite archivos .csv)
*
* BODY(multipart:FormData)  name:file
  http://localhost:8090/sentiment/upload-csv

  <p align="center">
    <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929767/Upload_saxlgc.png" alt="Inicio SentimentAPI" width=900">
  </p>

## Proyecto Terminado (todas los requisitos basicos listos y la mayoria de adicinales exigidos en la Hackathon)
###  Frontend
###  Descargar e Iniciar Frontend (elaborado con next.js version 15  y talwidCss version 4)
*  En tu terminal CMD (win) o BASH(linux o mac)

````
    git clone https://github.com/rlipac31/fron-sentiment-api.git
    # ingresa a la carpeta del repositorio de frontend descargado
    cd front-sentiment-api
    # instalar las dependenciass
    pnpm install
    # ejecutar el proyecto
     npm start
    
````


|                       Frontend_Home ([http://localhost:3000/](http://localhost:3000/))                        |                  Frontend_Login ([http://localhost:3000/login](http://localhost:3000/login))                   |
|:-------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------:|
| <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929795/Frontend_Home_jxgkle.png"  width="100%"> | <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929795/Frontend_Login_jrpq09.png"  width="100%"> |

#### Analisis de comentarios individuales name:file


   <p align="center">
     <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929796/Frontend_sentiment_uf7gll.png" alt="Inicio SentimentAPI" width=900">
   </p>

#### Analisis estadistico

|                                                 Frontend_Stadisticas                                                  |                                      Frontend_Stats_Exportando_resultados                                       |
|:---------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------:|
| <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929795/Frontend_Estadisticas_kx6eiy.png"  width="100%"> | <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929795/Frontend_Export_jir80v.png"  width="100%"> |

#### Analisi masivo de comentarios a traves de un archivo .CSV

  <p align="center">
     <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929796/Frontend_Uload_Page_p78wgw.png" alt="Analiosis Masivo" width=900">
   </p>

|                                                   Frontend_Upload_csv                                                   |                                              Frontend_Upload_resultados                                              |
|:-----------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------:|
| <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929796/Frontend_UPload_ARCHIVO_m2rvpy.png"  width="100%"> | <img src="https://res.cloudinary.com/rlipac/image/upload/v1768929796/Frontend_UPload_SAve_czvtph.png"  width="100%"> |

### URL del SentimenAPI(YA SE PUEDEN PROBAR EN POSTMANT INSOMNIA, ETC)
```
https://lipa-sentiment-api.azurewebsites.net
```

### URL Front-End subido a Verce (Ya se puede usar)

````
 https://fron-sentiment-api.vercel.app/
````

## URL API PYTHON
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





