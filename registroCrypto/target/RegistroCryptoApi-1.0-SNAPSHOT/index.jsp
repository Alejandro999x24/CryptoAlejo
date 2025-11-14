<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Registro de Criptomonedas</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <div class="container">
        <header>
            <h1>💰 Sistema de Registro de Criptomonedas</h1>
            <p>Gestiona tu portafolio de inversiones en criptomonedas</p>
        </header>

        <!-- Sección de Búsqueda -->
        <section class="search-section">
            <h2>🔍 Buscar Criptomoneda</h2>
            <div class="search-box">
                <input type="text" id="searchInput" placeholder="Buscar por nombre o símbolo (ej: Bitcoin, BTC, Ethereum)">
                <button onclick="buscarCriptomoneda()">Buscar</button>
            </div>
            <div id="searchResults" class="search-results"></div>
        </section>

        <!-- Formulario de Registro -->
        <section class="form-section" id="formSection" style="display: none;">
            <h2>📝 Registrar Compra de Criptomoneda</h2>
            <form id="cryptoForm" onsubmit="registrarCompra(event)">
                <input type="hidden" id="coinId">
                <input type="hidden" id="coinNombre">
                <input type="hidden" id="coinSimbolo">
                <input type="hidden" id="coinLogoUrl">
                
                <div class="form-group">
                    <label>Precio Actual (USD):</label>
                    <input type="number" id="precioActual" step="0.00000001" readonly>
                </div>
                
                <div class="form-group">
                    <label>Cambio 24h (%):</label>
                    <input type="number" id="cambio24h" step="0.01" readonly>
                </div>
                
                <div class="form-group">
                    <label>Cambio 7d (%):</label>
                    <input type="number" id="cambio7d" step="0.01" readonly>
                </div>
                
                <div class="form-group">
                    <label>Precio de Compra (USD): *</label>
                    <input type="number" id="precioCompra" step="0.00000001" required>
                </div>
                
                <div class="form-group">
                    <label>Cantidad Comprada: *</label>
                    <input type="number" id="cantidadComprada" step="0.00000001" required onchange="calcularValorTotal()">
                </div>
                
                <div class="form-group">
                    <label>Valor Total (USD):</label>
                    <input type="number" id="valorTotal" step="0.01" readonly>
                </div>
                
                <div class="form-group">
                    <label>Nombre del Inversor: *</label>
                    <input type="text" id="nombreInversor" required>
                </div>
                
                <div class="form-group">
                    <label>Email del Inversor: *</label>
                    <input type="email" id="emailInversor" required>
                </div>
                
                <div class="form-group">
                    <label>Plataforma:</label>
                    <select id="plataforma">
                        <option value="">Seleccione...</option>
                        <option value="Binance">Binance</option>
                        <option value="Coinbase">Coinbase</option>
                        <option value="Kraken">Kraken</option>
                        <option value="Bitfinex">Bitfinex</option>
                        <option value="Huobi">Huobi</option>
                        <option value="Otro">Otro</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Fecha de Compra: *</label>
                    <input type="date" id="fechaCompra" required>
                </div>
                
                <div class="form-group">
                    <label>Estrategia:</label>
                    <select id="estrategia">
                        <option value="">Seleccione...</option>
                        <option value="Largo plazo">Largo plazo</option>
                        <option value="Trading">Trading</option>
                        <option value="HODL">HODL</option>
                        <option value="Swing Trading">Swing Trading</option>
                        <option value="Day Trading">Day Trading</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Observaciones:</label>
                    <textarea id="observaciones" rows="3"></textarea>
                </div>
                
                <div class="form-actions">
                    <button type="submit">Registrar Compra</button>
                    <button type="button" onclick="limpiarFormulario()">Limpiar</button>
                </div>
            </form>
        </section>

        <!-- Tabla de Portafolio -->
        <section class="portfolio-section">
            <h2>💼 Portafolio de Inversiones</h2>
            <div class="portfolio-controls">
                <button onclick="cargarPortafolio()">🔄 Actualizar</button>
                <input type="email" id="filterEmail" placeholder="Filtrar por email">
                <button onclick="filtrarPorEmail()">Filtrar</button>
            </div>
            <div id="portfolioTable" class="portfolio-table"></div>
        </section>
    </div>

    <script src="js/app.js"></script>
</body>
</html>


