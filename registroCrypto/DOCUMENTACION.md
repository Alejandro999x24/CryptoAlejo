# 📚 Documentación Técnica - Sistema de Registro de Criptomonedas

## 1. Arquitectura del Sistema

### 1.1 Patrón de Diseño
El proyecto utiliza el patrón **MVC (Model-View-Controller)**:
- **Model**: `CryptoRecord.java` - Representa la entidad de datos
- **View**: `index.jsp` - Interfaz de usuario
- **Controller**: `CryptoRecordServlet.java` - Maneja las peticiones HTTP

### 1.2 Capas de la Aplicación

```
┌─────────────────────────────────────┐
│         Frontend (JSP/JS)           │
│  - index.jsp                        │
│  - app.js                           │
│  - styles.css                       │
└──────────────┬──────────────────────┘
               │ HTTP Requests
┌──────────────▼──────────────────────┐
│      Controller (Servlet)           │
│  - CryptoRecordServlet.java         │
│  - Maneja GET, POST, PUT, DELETE    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Business Logic (DAO)           │
│  - CryptoRecordDAO.java             │
│  - Operaciones CRUD                 │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Database (MySQL)                │
│  - registro_crypto                  │
│  - Tabla: crypto_record             │
└─────────────────────────────────────┘
```

## 2. Componentes Principales

### 2.1 Modelo: CryptoRecord

**Ubicación**: `src/main/java/sena/adso/model/CryptoRecord.java`

**Responsabilidades**:
- Representar la entidad de datos
- Contener todos los atributos del registro de criptomoneda
- Proporcionar métodos para cálculos (ganancia/pérdida)

**Métodos Importantes**:
- `calcularGananciaPerdida()`: Calcula la diferencia entre valor actual y valor invertido
- `calcularPorcentajeGananciaPerdida()`: Calcula el porcentaje de cambio

### 2.2 DAO: CryptoRecordDAO

**Ubicación**: `src/main/java/sena/adso/dao/CryptoRecordDAO.java`

**Responsabilidades**:
- Abstraer el acceso a la base de datos
- Implementar operaciones CRUD
- Mapear ResultSet a objetos CryptoRecord

**Métodos**:
- `crear(CryptoRecord)`: Inserta un nuevo registro
- `leerTodos()`: Obtiene todos los registros
- `leerPorId(int)`: Obtiene un registro por ID
- `leerPorCodigoRegistro(String)`: Busca por código único
- `actualizar(CryptoRecord)`: Actualiza un registro existente
- `eliminar(int)`: Elimina un registro
- `buscarPorEmail(String)`: Filtra por email del inversor

### 2.3 Servlet: CryptoRecordServlet

**Ubicación**: `src/main/java/sena/adso/servlet/CryptoRecordServlet.java`

**Responsabilidades**:
- Manejar peticiones HTTP (GET, POST, PUT, DELETE)
- Integrar con CoinGecko API
- Serializar/deserializar JSON
- Gestionar respuestas HTTP

**Endpoints**:

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/crypto` | Lista todos los registros |
| GET | `/api/crypto/buscar?q={query}` | Busca criptomoneda en CoinGecko |
| GET | `/api/crypto/id/{id}` | Obtiene registro por ID |
| GET | `/api/crypto/email/{email}` | Filtra por email |
| POST | `/api/crypto` | Crea nuevo registro |
| PUT | `/api/crypto` | Actualiza registro |
| DELETE | `/api/crypto/id/{id}` | Elimina registro |

**Integración con CoinGecko**:
- `buscarCriptomoneda(String)`: Busca y obtiene datos de una criptomoneda
- `actualizarPrecioActual(CryptoRecord)`: Actualiza precio desde CoinGecko
- `hacerPeticionHTTP(String)`: Método auxiliar para peticiones HTTP

### 2.4 Utilidades

#### DatabaseConnection
**Ubicación**: `src/main/java/sena/adso/util/DatabaseConnection.java`

- Gestiona la conexión a MySQL
- Implementa patrón Singleton
- Maneja errores de conexión

#### LocalDateAdapter y LocalDateTimeAdapter
**Ubicación**: `src/main/java/sena/adso/util/`

- Adaptadores Gson para serialización/deserialización de fechas
- Convierten entre LocalDate/LocalDateTime y JSON

## 3. Integración con CoinGecko API

### 3.1 Endpoints Utilizados

1. **Búsqueda de Criptomonedas**:
   ```
   GET https://api.coingecko.com/api/v3/search?query={query}
   ```

2. **Datos Detallados**:
   ```
   GET https://api.coingecko.com/api/v3/coins/{coinId}?market_data=true
   ```

3. **Precio Simple**:
   ```
   GET https://api.coingecko.com/api/v3/simple/price?ids={coinId}&vs_currencies=usd&include_24hr_change=true
   ```

### 3.2 Límites de la API

- **Plan Gratuito**: 10-50 llamadas/minuto
- **Sin Autenticación**: No requiere API key
- **Rate Limiting**: Respetar límites para evitar bloqueos

### 3.3 Manejo de Errores

- Timeout de conexión: 5 segundos
- Manejo de errores HTTP
- Mensajes de error descriptivos al usuario

## 4. Base de Datos

### 4.1 Esquema

**Base de Datos**: `registro_crypto`
**Tabla**: `crypto_record`

### 4.2 Índices

- `idx_codigo_registro`: Búsqueda rápida por código
- `idx_coin_id`: Búsqueda por ID de CoinGecko
- `idx_email_inversor`: Filtrado por email

### 4.3 Relaciones

Actualmente no hay relaciones con otras tablas. La estructura permite futuras extensiones como:
- Tabla de inversores
- Tabla de plataformas
- Tabla de estrategias

## 5. Frontend

### 5.1 Estructura

- **index.jsp**: Página principal con formularios y tabla
- **app.js**: Lógica JavaScript para interacción con API
- **styles.css**: Estilos y diseño responsive

### 5.2 Funcionalidades JavaScript

1. **buscarCriptomoneda()**: Busca en CoinGecko y muestra resultados
2. **registrarCompra()**: Envía datos del formulario al servidor
3. **cargarPortafolio()**: Carga y muestra todos los registros
4. **filtrarPorEmail()**: Filtra registros por email
5. **eliminarRegistro()**: Elimina un registro
6. **calcularValorTotal()**: Calcula automáticamente el valor total

### 5.3 Diseño Responsive

- Media queries para dispositivos móviles
- Tablas con scroll horizontal en pantallas pequeñas
- Formularios adaptativos

## 6. Flujo de Datos

### 6.1 Registro de Compra

```
Usuario busca criptomoneda
    ↓
Frontend llama a /api/crypto/buscar?q=bitcoin
    ↓
Servlet consulta CoinGecko API
    ↓
CoinGecko devuelve datos
    ↓
Frontend muestra datos y llena formulario
    ↓
Usuario completa formulario y envía
    ↓
Frontend llama a POST /api/crypto
    ↓
Servlet valida y crea CryptoRecord
    ↓
DAO inserta en base de datos
    ↓
Respuesta JSON con registro creado
    ↓
Frontend actualiza portafolio
```

### 6.2 Visualización de Portafolio

```
Usuario carga página
    ↓
Frontend llama a GET /api/crypto
    ↓
DAO consulta todos los registros
    ↓
Para cada registro, actualizar precio desde CoinGecko
    ↓
Servlet devuelve array JSON
    ↓
Frontend renderiza tabla con datos
    ↓
Calcula y muestra ganancias/pérdidas
```

## 7. Seguridad

### 7.1 Consideraciones Actuales

- Validación de datos en frontend y backend
- Prepared Statements para prevenir SQL Injection
- Sanitización de inputs

### 7.2 Mejoras Futuras

- Autenticación de usuarios
- Encriptación de datos sensibles
- HTTPS para producción
- Validación de tokens CSRF
- Rate limiting en servidor

## 8. Testing

### 8.1 Pruebas Manuales

1. **Búsqueda de Criptomonedas**:
   - Probar con diferentes nombres y símbolos
   - Verificar manejo de errores cuando no se encuentra

2. **Registro de Compras**:
   - Validar campos requeridos
   - Verificar cálculos automáticos
   - Probar inserción en base de datos

3. **Portafolio**:
   - Verificar carga de datos
   - Probar filtrado por email
   - Verificar actualización de precios

### 8.2 Colección Postman

Ver archivo `Postman_Collection_RegistroCryptoAPI.json` para pruebas de API.

## 9. Despliegue

### 9.1 Desarrollo Local

1. NetBeans con Tomcat embebido
2. XAMPP para MySQL
3. Acceso: `http://localhost:8080/RegistroCryptoApi`

### 9.2 Producción

**Requisitos**:
- Servidor con Java 11+
- Apache Tomcat 9+
- MySQL 8+
- Certificado SSL (recomendado)

**Pasos**:
1. Compilar proyecto: `mvn clean package`
2. Desplegar WAR en Tomcat
3. Configurar base de datos de producción
4. Actualizar `DatabaseConnection.java` con credenciales de producción

## 10. Mantenimiento

### 10.1 Logs

- Errores de conexión a BD se registran en consola
- Errores de API se registran en consola
- Considerar implementar logging con Log4j o SLF4J

### 10.2 Monitoreo

- Monitorear uso de API de CoinGecko
- Monitorear rendimiento de consultas SQL
- Monitorear uso de memoria

## 11. Extensiones Futuras

1. **Autenticación**: Sistema de usuarios y sesiones
2. **Gráficas**: Visualización de evolución de precios
3. **Alertas**: Notificaciones de cambios de precio
4. **Exportación**: Exportar portafolio a PDF/Excel
5. **Múltiples Monedas**: Soporte para diferentes monedas fiat
6. **Historial**: Historial de transacciones
7. **Dashboard**: Panel con estadísticas y métricas

## 12. Referencias

- [CoinGecko API Documentation](https://www.coingecko.com/en/api)
- [Java Servlet Documentation](https://javaee.github.io/servlet-spec/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)


