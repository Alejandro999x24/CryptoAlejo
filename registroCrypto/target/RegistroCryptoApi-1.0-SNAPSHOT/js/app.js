// Detecta automáticamente el contexto de la app (nombre del WAR) y arma la URL base de la API
const APP_CONTEXT = (function() {
  const path = window.location.pathname; // p.ej. '/RegistroCryptoApi/index.jsp' o '/RegistroCryptoApi/'
  const segments = path.split('/').filter(Boolean); // ['RegistroCryptoApi', 'index.jsp']
  const context = segments.length > 0 ? `/${segments[0]}` : '';
  return context;
})();
const API_BASE = `${APP_CONTEXT}/api/crypto`;

// Función para buscar criptomoneda en CoinGecko
async function buscarCriptomoneda() {
    const query = document.getElementById('searchInput').value.trim();
    
    if (!query) {
        alert('Por favor ingrese un nombre o símbolo de criptomoneda');
        return;
    }
    
    const resultsDiv = document.getElementById('searchResults');
    resultsDiv.innerHTML = '<p>Buscando...</p>';
    
    try {
        const response = await fetch(`${API_BASE}/buscar?q=${encodeURIComponent(query)}`);
        const data = await response.json();
        
        if (data.error) {
            resultsDiv.innerHTML = `<div class="error">${data.error}</div>`;
            return;
        }
        
        // Mostrar resultados
        mostrarResultadoBusqueda(data);
        
        // Llenar formulario con datos de la criptomoneda
        llenarFormulario(data);
        
    } catch (error) {
        console.error('Error al buscar:', error);
        resultsDiv.innerHTML = `<div class="error">Error al buscar criptomoneda: ${error.message}</div>`;
    }
}

// Mostrar resultado de búsqueda
function mostrarResultadoBusqueda(data) {
    const resultsDiv = document.getElementById('searchResults');
    const cambio24h = parseFloat(data.cambio24h || 0);
    const cambio7d = parseFloat(data.cambio7d || 0);
    const cambio24hClass = cambio24h >= 0 ? 'positive' : 'negative';
    const cambio7dClass = cambio7d >= 0 ? 'positive' : 'negative';
    
    resultsDiv.innerHTML = `
        <div class="coin-result">
            <div class="coin-header">
                <img src="${data.logoUrl}" alt="${data.nombre}" class="coin-logo">
                <div>
                    <h3>${data.nombre} (${data.simbolo.toUpperCase()})</h3>
                    <p class="coin-id">ID: ${data.id}</p>
                </div>
            </div>
            <div class="coin-details">
                <div class="detail-item">
                    <span class="label">Precio Actual:</span>
                    <span class="value">$${formatNumber(data.precioActual)}</span>
                </div>
                <div class="detail-item">
                    <span class="label">Cambio 24h:</span>
                    <span class="value ${cambio24hClass}">${cambio24h >= 0 ? '+' : ''}${cambio24h.toFixed(2)}%</span>
                </div>
                <div class="detail-item">
                    <span class="label">Cambio 7d:</span>
                    <span class="value ${cambio7dClass}">${cambio7d >= 0 ? '+' : ''}${cambio7d.toFixed(2)}%</span>
                </div>
                <div class="detail-item">
                    <span class="label">Market Cap:</span>
                    <span class="value">$${formatLargeNumber(data.marketCap)}</span>
                </div>
                <div class="detail-item">
                    <span class="label">Volumen 24h:</span>
                    <span class="value">$${formatLargeNumber(data.volumen24h)}</span>
                </div>
            </div>
        </div>
    `;
}

// Llenar formulario con datos de la criptomoneda
function llenarFormulario(data) {
    document.getElementById('coinId').value = data.id;
    document.getElementById('coinNombre').value = data.nombre;
    document.getElementById('coinSimbolo').value = data.simbolo;
    document.getElementById('coinLogoUrl').value = data.logoUrl;
    document.getElementById('precioActual').value = data.precioActual;
    document.getElementById('cambio24h').value = data.cambio24h || 0;
    document.getElementById('cambio7d').value = data.cambio7d || 0;
    document.getElementById('precioCompra').value = data.precioActual;
    
    // Mostrar sección del formulario
    document.getElementById('formSection').style.display = 'block';
    document.getElementById('formSection').scrollIntoView({ behavior: 'smooth' });
}

// Calcular valor total
function calcularValorTotal() {
    const precioCompra = parseFloat(document.getElementById('precioCompra').value) || 0;
    const cantidad = parseFloat(document.getElementById('cantidadComprada').value) || 0;
    const valorTotal = precioCompra * cantidad;
    document.getElementById('valorTotal').value = valorTotal.toFixed(2);
}

// Registrar compra
async function registrarCompra(event) {
    event.preventDefault();
    
    const precioActual = parseFloat(document.getElementById('precioActual').value);
    const precioCompra = parseFloat(document.getElementById('precioCompra').value);
    const cantidad = parseFloat(document.getElementById('cantidadComprada').value);
    const cambio24h = parseFloat(document.getElementById('cambio24h').value);
    const cambio7d = parseFloat(document.getElementById('cambio7d').value);

    if (isNaN(precioCompra) || isNaN(cantidad)) {
        alert('Ingrese precio de compra y cantidad válidos');
        return;
    }

    const valorTotal = +(precioCompra * cantidad).toFixed(8);

    const formData = {
        coinId: document.getElementById('coinId').value,
        nombre: document.getElementById('coinNombre').value,
        simbolo: document.getElementById('coinSimbolo').value,
        precioActual: isNaN(precioActual) ? precioCompra : precioActual,
        precioCompra: precioCompra,
        cantidadComprada: cantidad,
        valorTotal: valorTotal,
        cambio24h: isNaN(cambio24h) ? 0 : cambio24h,
        cambio7d: isNaN(cambio7d) ? 0 : cambio7d,
        marketCap: 0,
        volumen24h: 0,
        logoUrl: document.getElementById('coinLogoUrl').value,
        nombreInversor: document.getElementById('nombreInversor').value,
        emailInversor: document.getElementById('emailInversor').value,
        plataforma: document.getElementById('plataforma').value,
        fechaCompra: document.getElementById('fechaCompra').value,
        estrategia: document.getElementById('estrategia').value,
        observaciones: document.getElementById('observaciones').value
    };
    
    try {
        const response = await fetch(API_BASE, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=UTF-8'
            },
            body: JSON.stringify(formData)
        });
        
        const data = await response.json();
        
        if (response.ok) {
            alert('✅ Compra registrada exitosamente!');
            limpiarFormulario();
            cargarPortafolio();
        } else {
            alert('❌ Error al registrar compra: ' + (data.error || 'Error desconocido'));
        }
    } catch (error) {
        console.error('Error:', error);
        alert('❌ Error al registrar compra: ' + error.message);
    }
}

// Cargar portafolio
async function cargarPortafolio() {
    const tableDiv = document.getElementById('portfolioTable');
    tableDiv.innerHTML = '<p>Cargando portafolio...</p>';
    
    try {
        const response = await fetch(API_BASE);
        const registros = await response.json();
        
        if (registros.length === 0) {
            tableDiv.innerHTML = '<p class="no-data">No hay registros en el portafolio</p>';
            return;
        }
        
        // Actualizar precios actuales
        for (let registro of registros) {
            try {
                const updateResponse = await fetch(`${API_BASE}/id/${registro.id}`);
                if (updateResponse.ok) {
                    registro = await updateResponse.json();
                }
            } catch (e) {
                console.error('Error al actualizar precio:', e);
            }
        }
        
        mostrarPortafolio(registros);
        
    } catch (error) {
        console.error('Error al cargar portafolio:', error);
        tableDiv.innerHTML = `<div class="error">Error al cargar portafolio: ${error.message}</div>`;
    }
}

// Mostrar portafolio en tabla
function mostrarPortafolio(registros) {
    const tableDiv = document.getElementById('portfolioTable');
    
    let html = `
        <table class="portfolio-table-content">
            <thead>
                <tr>
                    <th>Código</th>
                    <th>Criptomoneda</th>
                    <th>Precio Compra</th>
                    <th>Precio Actual</th>
                    <th>Cantidad</th>
                    <th>Valor Total</th>
                    <th>Ganancia/Pérdida</th>
                    <th>% Cambio</th>
                    <th>Inversor</th>
                    <th>Fecha Compra</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
    `;
    
    let totalInvertido = 0;
    let totalActual = 0;
    
    registros.forEach(registro => {
        const precioCompra = parseFloat(registro.precioCompra);
        const precioActual = parseFloat(registro.precioActual);
        const cantidad = parseFloat(registro.cantidadComprada);
        const valorInvertido = precioCompra * cantidad;
        const valorActual = precioActual * cantidad;
        const gananciaPerdida = valorActual - valorInvertido;
        const porcentajeCambio = precioCompra > 0 ? ((precioActual - precioCompra) / precioCompra * 100) : 0;
        
        totalInvertido += valorInvertido;
        totalActual += valorActual;
        
        const gananciaClass = gananciaPerdida >= 0 ? 'positive' : 'negative';
        const cambioClass = porcentajeCambio >= 0 ? 'positive' : 'negative';
        
        html += `
            <tr>
                <td>${registro.codigoRegistro}</td>
                <td>
                    <div class="coin-cell">
                        <img src="${registro.logoUrl}" alt="${registro.nombre}" class="coin-logo-small">
                        <div>
                            <strong>${registro.nombre}</strong><br>
                            <small>${registro.simbolo.toUpperCase()}</small>
                        </div>
                    </div>
                </td>
                <td>$${formatNumber(precioCompra)}</td>
                <td>$${formatNumber(precioActual)}</td>
                <td>${formatNumber(cantidad)}</td>
                <td>$${formatNumber(valorInvertido)}</td>
                <td class="${gananciaClass}">
                    ${gananciaPerdida >= 0 ? '+' : ''}$${formatNumber(gananciaPerdida)}
                </td>
                <td class="${cambioClass}">
                    ${porcentajeCambio >= 0 ? '+' : ''}${porcentajeCambio.toFixed(2)}%
                </td>
                <td>
                    <div>
                        <strong>${registro.nombreInversor}</strong><br>
                        <small>${registro.emailInversor}</small>
                    </div>
                </td>
                <td>${formatearFecha(registro.fechaCompra)}</td>
                <td>
                    <button onclick="eliminarRegistro(${registro.id})" class="btn-delete">Eliminar</button>
                </td>
            </tr>
        `;
    });
    
    const gananciaTotal = totalActual - totalInvertido;
    const gananciaTotalClass = gananciaTotal >= 0 ? 'positive' : 'negative';
    
    html += `
            </tbody>
            <tfoot>
                <tr class="total-row">
                    <td colspan="5"><strong>Total:</strong></td>
                    <td><strong>$${formatNumber(totalInvertido)}</strong></td>
                    <td class="${gananciaTotalClass}">
                        <strong>${gananciaTotal >= 0 ? '+' : ''}$${formatNumber(gananciaTotal)}</strong>
                    </td>
                    <td colspan="4"></td>
                </tr>
            </tfoot>
        </table>
    `;
    
    tableDiv.innerHTML = html;
}

// Filtrar por email
async function filtrarPorEmail() {
    const email = document.getElementById('filterEmail').value.trim();
    
    if (!email) {
        cargarPortafolio();
        return;
    }
    
    const tableDiv = document.getElementById('portfolioTable');
    tableDiv.innerHTML = '<p>Filtrando...</p>';
    
    try {
        const response = await fetch(`${API_BASE}/email/${encodeURIComponent(email)}`);
        const registros = await response.json();
        
        if (registros.length === 0) {
            tableDiv.innerHTML = '<p class="no-data">No hay registros para este email</p>';
            return;
        }
        
        mostrarPortafolio(registros);
        
    } catch (error) {
        console.error('Error al filtrar:', error);
        tableDiv.innerHTML = `<div class="error">Error al filtrar: ${error.message}</div>`;
    }
}

// Eliminar registro
async function eliminarRegistro(id) {
    if (!confirm('¿Está seguro de eliminar este registro?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/id/${id}`, {
            method: 'DELETE'
        });
        
        const data = await response.json();
        
        if (response.ok) {
            alert('✅ Registro eliminado exitosamente');
            cargarPortafolio();
        } else {
            alert('❌ Error al eliminar: ' + (data.error || 'Error desconocido'));
        }
    } catch (error) {
        console.error('Error:', error);
        alert('❌ Error al eliminar: ' + error.message);
    }
}

// Limpiar formulario
function limpiarFormulario() {
    document.getElementById('cryptoForm').reset();
    document.getElementById('searchResults').innerHTML = '';
    document.getElementById('formSection').style.display = 'none';
}

// Funciones auxiliares
function formatNumber(num) {
    if (num == null || isNaN(num)) return '0.00';
    return new Intl.NumberFormat('es-ES', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 8
    }).format(num);
}

function formatLargeNumber(num) {
    if (num == null || isNaN(num)) return '0';
    if (num >= 1e12) return (num / 1e12).toFixed(2) + 'T';
    if (num >= 1e9) return (num / 1e9).toFixed(2) + 'B';
    if (num >= 1e6) return (num / 1e6).toFixed(2) + 'M';
    if (num >= 1e3) return (num / 1e3).toFixed(2) + 'K';
    return num.toString();
}

function formatearFecha(fecha) {
    if (!fecha) return '';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES');
}

// Cargar portafolio al iniciar
document.addEventListener('DOMContentLoaded', function() {
    cargarPortafolio();
    
    // Permitir búsqueda con Enter
    document.getElementById('searchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            buscarCriptomoneda();
        }
    });
    
    // Establecer fecha actual por defecto
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('fechaCompra').value = today;
});


