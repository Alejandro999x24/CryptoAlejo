package sena.adso.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import sena.adso.dao.CryptoRecordDAO;
import sena.adso.model.CryptoRecord;
import sena.adso.util.LocalDateAdapter;
import sena.adso.util.LocalDateTimeAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(name = "CryptoRecordServlet", urlPatterns = {"/api/crypto/*"})
public class CryptoRecordServlet extends HttpServlet {
    
    private CryptoRecordDAO dao;
    private Gson gson;
    private static final String COINGECKO_API_BASE = "https://api.coingecko.com/api/v3";
    
    @Override
    public void init() throws ServletException {
        super.init();
        dao = new CryptoRecordDAO();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(java.time.LocalDateTime.class, new LocalDateTimeAdapter());
        gson = gsonBuilder.setPrettyPrinting().create();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Listar todos los registros
                List<CryptoRecord> registros = dao.leerTodos();
                out.print(gson.toJson(registros));
                
            } else if (pathInfo.startsWith("/buscar")) {
                // Buscar criptomoneda en CoinGecko
                String query = request.getParameter("q");
                if (query == null || query.trim().isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print(gson.toJson(Map.of("error", "Parámetro 'q' requerido")));
                    return;
                }
                
                String coinData = buscarCriptomoneda(query);
                out.print(coinData);
                
            } else if (pathInfo.startsWith("/id/")) {
                // Obtener registro por ID
                String idStr = pathInfo.substring(4);
                int id = Integer.parseInt(idStr);
                CryptoRecord record = dao.leerPorId(id);
                
                if (record != null) {
                    // Actualizar precio actual desde CoinGecko
                    actualizarPrecioActual(record);
                    out.print(gson.toJson(record));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(gson.toJson(Map.of("error", "Registro no encontrado")));
                }
                
            } else if (pathInfo.startsWith("/email/")) {
                // Buscar registros por email
                String email = pathInfo.substring(7);
                List<CryptoRecord> registros = dao.buscarPorEmail(email);
                out.print(gson.toJson(registros));
                
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print(gson.toJson(Map.of("error", "Endpoint no encontrado")));
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of(
                    "error", e.getClass().getName() + ": " + (e.getMessage() != null ? e.getMessage() : ""))))
            ;
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            String contentType = request.getContentType();
            CryptoRecord record = null;
            if (contentType != null && contentType.toLowerCase().contains("application/json")) {
                BufferedReader reader = request.getReader();
                StringBuilder json = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
                record = gson.fromJson(json.toString(), CryptoRecord.class);
            }
            if (record == null || record.getCoinId() == null) {
                CryptoRecord r = new CryptoRecord();
                String coinId = request.getParameter("coinId");
                String nombre = request.getParameter("nombre");
                String simbolo = request.getParameter("simbolo");
                String precioCompraStr = request.getParameter("precioCompra");
                String cantidadCompradaStr = request.getParameter("cantidadComprada");
                String fechaCompraStr = request.getParameter("fechaCompra");
                String nombreInversor = request.getParameter("nombreInversor");
                String emailInversor = request.getParameter("emailInversor");
                String plataforma = request.getParameter("plataforma");
                String estrategia = request.getParameter("estrategia");
                String observaciones = request.getParameter("observaciones");
                String precioActualStr = request.getParameter("precioActual");
                String cambio24hStr = request.getParameter("cambio24h");
                String cambio7dStr = request.getParameter("cambio7d");
                String marketCapStr = request.getParameter("marketCap");
                String volumen24hStr = request.getParameter("volumen24h");
                String logoUrl = request.getParameter("logoUrl");
                String codigoRegistro = request.getParameter("codigoRegistro");
                r.setCoinId(coinId);
                r.setNombre(nombre);
                r.setSimbolo(simbolo);
                if (precioCompraStr != null && !precioCompraStr.isBlank()) {
                    r.setPrecioCompra(new java.math.BigDecimal(precioCompraStr));
                }
                if (cantidadCompradaStr != null && !cantidadCompradaStr.isBlank()) {
                    r.setCantidadComprada(new java.math.BigDecimal(cantidadCompradaStr));
                }
                if (fechaCompraStr != null && !fechaCompraStr.isBlank()) {
                    try {
                        r.setFechaCompra(java.time.LocalDate.parse(fechaCompraStr));
                    } catch (Exception ex) {
                        try {
                            r.setFechaCompra(java.time.LocalDate.parse(
                                fechaCompraStr,
                                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            ));
                        } catch (Exception ex2) {
                            throw new IllegalArgumentException("Fecha de compra inválida: " + fechaCompraStr);
                        }
                    }
                }
                if (precioActualStr != null && !precioActualStr.isBlank()) {
                    r.setPrecioActual(new java.math.BigDecimal(precioActualStr));
                }
                if (cambio24hStr != null && !cambio24hStr.isBlank()) {
                    r.setCambio24h(new java.math.BigDecimal(cambio24hStr));
                }
                if (cambio7dStr != null && !cambio7dStr.isBlank()) {
                    r.setCambio7d(new java.math.BigDecimal(cambio7dStr));
                }
                if (marketCapStr != null && !marketCapStr.isBlank()) {
                    r.setMarketCap(Long.parseLong(marketCapStr));
                }
                if (volumen24hStr != null && !volumen24hStr.isBlank()) {
                    r.setVolumen24h(Long.parseLong(volumen24hStr));
                }
                if (logoUrl != null && !logoUrl.isBlank()) {
                    r.setLogoUrl(logoUrl);
                }
                if (nombreInversor != null) {
                    r.setNombreInversor(nombreInversor);
                }
                if (emailInversor != null) {
                    r.setEmailInversor(emailInversor);
                }
                if (plataforma != null) {
                    r.setPlataforma(plataforma);
                }
                if (estrategia != null) {
                    r.setEstrategia(estrategia);
                }
                if (observaciones != null) {
                    r.setObservaciones(observaciones);
                }
                if (codigoRegistro != null && !codigoRegistro.isBlank()) {
                    r.setCodigoRegistro(codigoRegistro);
                }
                record = r;
            }
            
            // Validaciones mínimas de campos requeridos
            if (record.getCoinId() == null || record.getCoinId().isBlank() ||
                record.getNombre() == null || record.getNombre().isBlank() ||
                record.getSimbolo() == null || record.getSimbolo().isBlank() ||
                record.getPrecioCompra() == null || record.getCantidadComprada() == null ||
                record.getFechaCompra() == null ||
                record.getNombreInversor() == null || record.getNombreInversor().isBlank() ||
                record.getEmailInversor() == null || record.getEmailInversor().isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("error", "Faltan campos obligatorios")));
                return;
            }

            // Generar código de registro único si no existe
            if (record.getCodigoRegistro() == null || record.getCodigoRegistro().trim().isEmpty()) {
                record.setCodigoRegistro("CRYPTO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            }
            
            // Defaults y sanitización
            if (record.getPrecioActual() == null) {
                record.setPrecioActual(record.getPrecioCompra());
            }
            if (record.getCambio24h() == null) {
                record.setCambio24h(java.math.BigDecimal.ZERO);
            }
            if (record.getCambio7d() == null) {
                record.setCambio7d(java.math.BigDecimal.ZERO);
            }
            if (record.getMarketCap() == 0L) {
                record.setMarketCap(0L);
            }
            if (record.getVolumen24h() == 0L) {
                record.setVolumen24h(0L);
            }

            // Recalcular SIEMPRE valor total: precioCompra * cantidadComprada
            record.setValorTotal(record.getPrecioCompra().multiply(record.getCantidadComprada()));
            
            boolean exito;
            try {
                exito = dao.crear(record);
            } catch (RuntimeException ex) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of(
                        "error", ex.getClass().getName() + ": " + (ex.getMessage() != null ? ex.getMessage() : ""))))
                ;
                ex.printStackTrace();
                return;
            }
            
            if (exito) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print(gson.toJson(record));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of("error", "Error al crear el registro")));
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("error", e.getMessage())));
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            BufferedReader reader = request.getReader();
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            
            CryptoRecord record = gson.fromJson(json.toString(), CryptoRecord.class);
            
            // Recalcular valor total
            if (record.getPrecioCompra() != null && record.getCantidadComprada() != null) {
                record.setValorTotal(record.getPrecioCompra().multiply(record.getCantidadComprada()));
            }
            
            boolean exito = dao.actualizar(record);
            
            if (exito) {
                out.print(gson.toJson(record));
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(gson.toJson(Map.of("error", "Error al actualizar el registro")));
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("error", e.getMessage())));
            e.printStackTrace();
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo != null && pathInfo.startsWith("/id/")) {
                String idStr = pathInfo.substring(4);
                int id = Integer.parseInt(idStr);
                
                boolean exito = dao.eliminar(id);
                
                if (exito) {
                    out.print(gson.toJson(Map.of("mensaje", "Registro eliminado correctamente")));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print(gson.toJson(Map.of("error", "Registro no encontrado")));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(gson.toJson(Map.of("error", "ID requerido")));
            }
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("error", e.getMessage())));
            e.printStackTrace();
        }
    }
    
    // Método para buscar criptomoneda en CoinGecko
    private String buscarCriptomoneda(String query) throws IOException {
        try {
            // Primero buscar por nombre/símbolo
            String searchUrl = COINGECKO_API_BASE + "/search?query=" + 
                              URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            
            String searchResponse = hacerPeticionHTTP(searchUrl);
            Map<String, Object> searchResult = gson.fromJson(searchResponse, Map.class);
            
            List<Map<String, Object>> coins = (List<Map<String, Object>>) searchResult.get("coins");
            
            if (coins == null || coins.isEmpty()) {
                return gson.toJson(Map.of("error", "No se encontraron criptomonedas"));
            }
            
            // Obtener el primer resultado
            Map<String, Object> firstCoin = coins.get(0);
            String coinId = (String) firstCoin.get("id");
            
            // Obtener datos detallados de la criptomoneda
            String detailUrl = COINGECKO_API_BASE + "/coins/" + coinId + 
                             "?localization=false&tickers=false&market_data=true&community_data=false&developer_data=false&sparkline=false";
            
            String detailResponse = hacerPeticionHTTP(detailUrl);
            Map<String, Object> coinData = gson.fromJson(detailResponse, Map.class);
            
            // Extraer información relevante
            Map<String, Object> marketData = (Map<String, Object>) coinData.get("market_data");
            Map<String, Object> result = new HashMap<>();
            
            result.put("id", coinData.get("id"));
            result.put("nombre", coinData.get("name"));
            result.put("simbolo", coinData.get("symbol"));
            result.put("precioActual", ((Map<String, Object>) marketData.get("current_price")).get("usd"));
            result.put("cambio24h", marketData.get("price_change_percentage_24h"));
            result.put("cambio7d", marketData.get("price_change_percentage_7d"));
            result.put("marketCap", ((Map<String, Object>) marketData.get("market_cap")).get("usd"));
            result.put("volumen24h", ((Map<String, Object>) marketData.get("total_volume")).get("usd"));
            result.put("logoUrl", ((Map<String, Object>) coinData.get("image")).get("large"));
            
            return gson.toJson(result);
            
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(Map.of("error", "Error al buscar criptomoneda: " + e.getMessage()));
        }
    }
    
    // Método para actualizar precio actual desde CoinGecko
    private void actualizarPrecioActual(CryptoRecord record) {
        try {
            String url = COINGECKO_API_BASE + "/simple/price?ids=" + record.getCoinId() + "&vs_currencies=usd&include_24hr_change=true&include_7d_change=true&include_market_cap=true&include_24hr_vol=true";
            String response = hacerPeticionHTTP(url);
            
            Map<String, Object> data = gson.fromJson(response, Map.class);
            Map<String, Object> coinData = (Map<String, Object>) data.get(record.getCoinId());
            
            if (coinData != null) {
                if (coinData.get("usd") != null) {
                    record.setPrecioActual(new BigDecimal(coinData.get("usd").toString()));
                }
                if (coinData.get("usd_24h_change") != null) {
                    record.setCambio24h(new BigDecimal(coinData.get("usd_24h_change").toString()));
                }
                if (coinData.get("usd_7d_change") != null) {
                    record.setCambio7d(new BigDecimal(coinData.get("usd_7d_change").toString()));
                }
                if (coinData.get("usd_market_cap") != null) {
                    record.setMarketCap(Long.parseLong(coinData.get("usd_market_cap").toString()));
                }
                if (coinData.get("usd_24h_vol") != null) {
                    record.setVolumen24h(Long.parseLong(coinData.get("usd_24h_vol").toString()));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar precio: " + e.getMessage());
        }
    }
    
    // Método auxiliar para hacer peticiones HTTP
    private String hacerPeticionHTTP(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new IOException("Error HTTP: " + responseCode);
        }
    }
}

