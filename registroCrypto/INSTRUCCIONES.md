# 🚀 Instrucciones Rápidas de Configuración

## Paso 1: Configurar XAMPP

1. **Iniciar XAMPP**
   - Abrir XAMPP Control Panel
   - Iniciar **Apache** y **MySQL**

2. **Crear la Base de Datos**
   - Abrir phpMyAdmin: http://localhost/phpmyadmin
   - Ir a la pestaña "SQL"
   - Copiar y pegar el contenido del archivo `database.sql`
   - Ejecutar el script
   - Verificar que se creó la base de datos `registro_crypto`

## Paso 2: Configurar NetBeans

1. **Abrir el Proyecto**
   - Abrir NetBeans
   - File → Open Project
   - Seleccionar la carpeta `registroCrypto`
   - NetBeans detectará automáticamente que es un proyecto Maven

2. **Verificar Configuración del Proyecto**
   - Click derecho en el proyecto → Properties
   - **Sources**: Verificar que Java está configurado correctamente
   - **Libraries**: Maven descargará automáticamente las dependencias

3. **Configurar Servidor**
   - Click derecho en el proyecto → Properties
   - **Run** → **Server**: Seleccionar "Apache Tomcat" (si no está, agregarlo)
   - **Context Path**: `/RegistroCryptoApi`
   - **Browser**: Seleccionar tu navegador preferido

4. **Verificar Credenciales de Base de Datos**
   - Abrir: `src/main/java/sena/adso/util/DatabaseConnection.java`
   - Verificar que `USER = "root"` y `PASSWORD = ""` (o tu contraseña de MySQL)
   - Si tu MySQL tiene contraseña, actualizar la línea:
     ```java
     private static final String PASSWORD = "tu_contraseña";
     ```

## Paso 3: Compilar y Ejecutar

1. **Compilar el Proyecto**
   - Click derecho en el proyecto → **Clean and Build**
   - Esperar a que Maven descargue las dependencias (primera vez puede tardar)
   - Verificar que no hay errores en la consola

2. **Ejecutar el Proyecto**
   - Click derecho en el proyecto → **Run**
   - NetBeans iniciará Tomcat automáticamente
   - Se abrirá el navegador en: `http://localhost:8080/RegistroCryptoApi`

## Paso 4: Probar la Aplicación

1. **Buscar una Criptomoneda**
   - En el campo de búsqueda, escribir: `bitcoin` o `BTC`
   - Click en "Buscar"
   - Debería mostrar los datos de Bitcoin desde CoinGecko

2. **Registrar una Compra**
   - Después de buscar, el formulario se llenará automáticamente
   - Completar los campos requeridos:
     - Nombre del Inversor
     - Email del Inversor
     - Precio de Compra (puede modificarse)
     - Cantidad Comprada
   - Click en "Registrar Compra"

3. **Ver el Portafolio**
   - El portafolio se carga automáticamente
   - Debería mostrar la compra registrada
   - Los precios se actualizan desde CoinGecko

## ⚠️ Solución de Problemas Comunes

### Error: "No se puede conectar a la base de datos"
- ✅ Verificar que MySQL esté corriendo en XAMPP
- ✅ Verificar credenciales en `DatabaseConnection.java`
- ✅ Verificar que la base de datos `registro_crypto` exista
- ✅ Probar conexión manual en phpMyAdmin

### Error: "404 Not Found"
- ✅ Verificar que el Context Path sea `/RegistroCryptoApi`
- ✅ Verificar que el proyecto se haya desplegado correctamente
- ✅ Revisar la consola de NetBeans para errores

### Error: "No se encuentra la criptomoneda"
- ✅ Verificar conexión a internet
- ✅ Verificar que la API de CoinGecko esté disponible
- ✅ Probar con otro nombre (ej: "ethereum", "cardano")

### Error: "Maven no descarga dependencias"
- ✅ Verificar conexión a internet
- ✅ Click derecho en proyecto → **Reload Project**
- ✅ Verificar configuración de Maven en NetBeans: Tools → Options → Java → Maven

### El proyecto no compila
- ✅ Verificar que Java JDK 11+ esté instalado
- ✅ Verificar configuración de Java en NetBeans: Tools → Options → Java
- ✅ Limpiar y reconstruir: **Clean and Build**

## 📝 Notas Importantes

- **Primera ejecución**: La primera vez que ejecutes el proyecto, Maven descargará todas las dependencias. Esto puede tardar varios minutos.
- **Base de datos**: Asegúrate de que MySQL esté corriendo antes de ejecutar la aplicación.
- **API CoinGecko**: El plan gratuito tiene límites (10-50 llamadas/minuto). No abuses de las búsquedas.
- **Puerto 8080**: Si el puerto 8080 está ocupado, puedes cambiarlo en la configuración de Tomcat en NetBeans.

## 🎯 Próximos Pasos

Una vez que la aplicación esté funcionando:
1. Explora todas las funcionalidades
2. Prueba registrar diferentes criptomonedas
3. Prueba el filtrado por email
4. Revisa la documentación completa en `DOCUMENTACION.md`
5. Importa la colección de Postman para probar la API directamente

## 📞 Soporte

Si encuentras problemas:
1. Revisa los logs en la consola de NetBeans
2. Revisa los logs de Tomcat
3. Verifica la configuración paso a paso
4. Consulta `DOCUMENTACION.md` para más detalles técnicos


