# 💰 Sistema de Registro de Criptomonedas

Sistema web desarrollado en Java para gestionar un portafolio de inversiones en criptomonedas, integrado con la API de CoinGecko para obtener datos en tiempo real.

## 📋 Descripción

Este proyecto permite a los usuarios:
- Buscar criptomonedas utilizando la API de CoinGecko
- Registrar compras de criptomonedas con información detallada
- Visualizar el portafolio de inversiones
- Calcular automáticamente ganancias/pérdidas
- Filtrar registros por email del inversor

## 🛠️ Tecnologías Utilizadas

- **Backend**: Java 11, Servlets, JSP
- **Base de Datos**: MySQL (XAMPP)
- **API Externa**: CoinGecko API
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla)
- **Build Tool**: Maven
- **Servidor**: Apache Tomcat

## 📁 Estructura del Proyecto

```
RegistroCryptoApi/
├── src/
│   └── main/
│       ├── java/
│       │   └── sena/adso/
│       │       ├── dao/
│       │       │   └── CryptoRecordDAO.java
│       │       ├── model/
│       │       │   └── CryptoRecord.java
│       │       ├── servlet/
│       │       │   └── CryptoRecordServlet.java
│       │       └── util/
│       │           ├── DatabaseConnection.java
│       │           ├── LocalDateAdapter.java
│       │           └── LocalDateTimeAdapter.java
│       └── webapp/
│           ├── css/
│           │   └── styles.css
│           ├── js/
│           │   └── app.js
│           ├── WEB-INF/
│           │   └── web.xml
│           └── index.jsp
├── database.sql
├── pom.xml
└── README.md
```

## 🚀 Instalación y Configuración

### Requisitos Previos

1. **Java JDK 11** o superior
2. **Apache NetBeans IDE**
3. **XAMPP** (para MySQL y Apache)
4. **Maven** (incluido en NetBeans)
5. **Apache Tomcat** (incluido en NetBeans o descargar por separado)

### Pasos de Instalación

1. **Clonar o descargar el proyecto**

2. **Configurar la Base de Datos**:
   - Iniciar XAMPP y activar MySQL
   - Abrir phpMyAdmin (http://localhost/phpmyadmin)
   - Importar el archivo `database.sql` o ejecutarlo desde la línea de comandos:
   ```sql
   mysql -u root -p < database.sql
   ```

3. **Configurar la Conexión a la Base de Datos**:
   - Editar `src/main/java/sena/adso/util/DatabaseConnection.java`
   - Verificar que las credenciales coincidan con tu configuración de MySQL:
     ```java
     private static final String USER = "root";
     private static final String PASSWORD = ""; // Tu contraseña de MySQL
     ```

4. **Abrir el Proyecto en NetBeans**:
   - Abrir NetBeans
   - File → Open Project
   - Seleccionar la carpeta del proyecto
   - NetBeans detectará automáticamente que es un proyecto Maven

5. **Configurar el Servidor**:
   - Click derecho en el proyecto → Properties
   - Run → Server: Seleccionar Apache Tomcat
   - Context Path: `/RegistroCryptoApi`

6. **Compilar y Ejecutar**:
   - Click derecho en el proyecto → Clean and Build
   - Click derecho en el proyecto → Run
   - El proyecto se abrirá en: `http://localhost:8080/RegistroCryptoApi`

## 📊 Modelo de Datos

### Tabla: crypto_record

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | INT | ID autoincremental |
| codigo_registro | VARCHAR(50) | Código único del registro |
| coin_id | VARCHAR(100) | ID de CoinGecko |
| nombre | VARCHAR(200) | Nombre de la criptomoneda |
| simbolo | VARCHAR(20) | Símbolo (BTC, ETH, etc.) |
| precio_actual | DECIMAL(20,8) | Precio actual en USD |
| precio_compra | DECIMAL(20,8) | Precio de compra en USD |
| cantidad_comprada | DECIMAL(20,8) | Cantidad comprada |
| valor_total | DECIMAL(20,8) | Valor total de la compra |
| cambio_24h | DECIMAL(10,4) | Cambio porcentual 24h |
| cambio_7d | DECIMAL(10,4) | Cambio porcentual 7 días |
| market_cap | BIGINT | Capitalización de mercado |
| volumen_24h | BIGINT | Volumen 24 horas |
| logo_url | VARCHAR(500) | URL del logo |
| nombre_inversor | VARCHAR(200) | Nombre del inversor |
| email_inversor | VARCHAR(200) | Email del inversor |
| plataforma | VARCHAR(100) | Plataforma de compra |
| fecha_compra | DATE | Fecha de compra |
| estrategia | VARCHAR(100) | Estrategia de inversión |
| observaciones | TEXT | Observaciones adicionales |
| fecha_registro | DATETIME | Fecha de registro |

## 🔌 API Endpoints

### GET `/api/crypto`
Obtiene todos los registros del portafolio.

**Respuesta**: Array de objetos CryptoRecord

### GET `/api/crypto/buscar?q={query}`
Busca una criptomoneda en CoinGecko.

**Parámetros**:
- `q`: Nombre o símbolo de la criptomoneda

**Respuesta**: Objeto con datos de la criptomoneda

### GET `/api/crypto/id/{id}`
Obtiene un registro por ID y actualiza el precio actual.

**Respuesta**: Objeto CryptoRecord

### GET `/api/crypto/email/{email}`
Obtiene todos los registros de un inversor por email.

**Respuesta**: Array de objetos CryptoRecord

### POST `/api/crypto`
Crea un nuevo registro de compra.

**Body**: Objeto CryptoRecord en JSON

**Respuesta**: Objeto CryptoRecord creado

### PUT `/api/crypto`
Actualiza un registro existente.

**Body**: Objeto CryptoRecord en JSON (debe incluir el ID)

**Respuesta**: Objeto CryptoRecord actualizado

### DELETE `/api/crypto/id/{id}`
Elimina un registro por ID.

**Respuesta**: Mensaje de confirmación

## 🎯 Funcionalidades

1. **Búsqueda de Criptomonedas**: Integración con CoinGecko API para buscar y obtener datos en tiempo real
2. **Registro de Compras**: Formulario completo para registrar inversiones
3. **Cálculo Automático**: Cálculo automático de ganancias/pérdidas y porcentajes
4. **Portafolio**: Visualización tabular de todas las inversiones
5. **Filtrado**: Filtrado de registros por email del inversor
6. **Actualización de Precios**: Actualización automática de precios desde CoinGecko

## 📝 Uso

1. **Buscar Criptomoneda**:
   - Ingrese el nombre o símbolo en el campo de búsqueda
   - Haga clic en "Buscar" o presione Enter
   - Se mostrarán los datos de la criptomoneda

2. **Registrar Compra**:
   - Después de buscar, se llenará automáticamente el formulario
   - Complete los campos requeridos (marcados con *)
   - El valor total se calcula automáticamente
   - Haga clic en "Registrar Compra"

3. **Ver Portafolio**:
   - El portafolio se carga automáticamente al iniciar
   - Use "Actualizar" para refrescar los datos
   - Use el filtro por email para ver registros específicos

## ⚠️ Notas Importantes

- La API de CoinGecko tiene límites de uso (10-50 llamadas/minuto en plan gratuito)
- Asegúrese de tener XAMPP corriendo antes de ejecutar la aplicación
- Verifique que MySQL esté activo en XAMPP
- La base de datos debe estar creada antes de ejecutar la aplicación

## 🐛 Solución de Problemas

**Error de conexión a la base de datos**:
- Verificar que MySQL esté corriendo en XAMPP
- Verificar credenciales en `DatabaseConnection.java`
- Verificar que la base de datos `registro_crypto` exista

**Error al buscar criptomonedas**:
- Verificar conexión a internet
- Verificar que la API de CoinGecko esté disponible
- Revisar límites de uso de la API

**Error 404 al acceder a la aplicación**:
- Verificar que el servidor Tomcat esté corriendo
- Verificar el Context Path en la configuración del proyecto
- Verificar que el proyecto se haya desplegado correctamente

## 👨‍💻 Autor

Desarrollado como proyecto académico para SENA ADSO.

## 📄 Licencia

Este proyecto es de uso educativo.


