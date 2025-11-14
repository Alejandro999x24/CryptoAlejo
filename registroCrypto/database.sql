-- Base de datos para Sistema de Registro de Criptomonedas
-- Ejecutar en MySQL (XAMPP)

CREATE DATABASE IF NOT EXISTS registro_crypto CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE registro_crypto;

-- Tabla para almacenar registros de criptomonedas
CREATE TABLE IF NOT EXISTS crypto_record (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_registro VARCHAR(50) UNIQUE NOT NULL,
    coin_id VARCHAR(100) NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    simbolo VARCHAR(20) NOT NULL,
    precio_actual DECIMAL(20, 8) NOT NULL,
    precio_compra DECIMAL(20, 8) NOT NULL,
    cantidad_comprada DECIMAL(20, 8) NOT NULL,
    valor_total DECIMAL(20, 8) NOT NULL,
    cambio_24h DECIMAL(10, 4) DEFAULT 0,
    cambio_7d DECIMAL(10, 4) DEFAULT 0,
    market_cap BIGINT DEFAULT 0,
    volumen_24h BIGINT DEFAULT 0,
    logo_url VARCHAR(500),
    nombre_inversor VARCHAR(200) NOT NULL,
    email_inversor VARCHAR(200) NOT NULL,
    plataforma VARCHAR(100),
    fecha_compra DATE NOT NULL,
    estrategia VARCHAR(100),
    observaciones TEXT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_codigo_registro (codigo_registro),
    INDEX idx_coin_id (coin_id),
    INDEX idx_email_inversor (email_inversor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Datos de ejemplo (opcional)
INSERT INTO crypto_record (
    codigo_registro, coin_id, nombre, simbolo, precio_actual, precio_compra,
    cantidad_comprada, valor_total, cambio_24h, cambio_7d, market_cap, volumen_24h,
    logo_url, nombre_inversor, email_inversor, plataforma, fecha_compra, estrategia, observaciones
) VALUES (
    'CRYPTO-001', 'bitcoin', 'Bitcoin', 'BTC', 45000.00, 42000.00,
    0.5, 21000.00, 2.5, 5.2, 850000000000, 25000000000,
    'https://assets.coingecko.com/coins/images/1/large/bitcoin.png',
    'Juan Pérez', 'juan.perez@email.com', 'Binance', '2024-01-15',
    'Largo plazo', 'Inversión inicial en Bitcoin'
);


