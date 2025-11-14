package sena.adso.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CryptoRecord {
    private int id;
    private String codigoRegistro;
    private String coinId;
    private String nombre;
    private String simbolo;
    private BigDecimal precioActual;
    private BigDecimal precioCompra;
    private BigDecimal cantidadComprada;
    private BigDecimal valorTotal;
    private BigDecimal cambio24h;
    private BigDecimal cambio7d;
    private long marketCap;
    private long volumen24h;
    private String logoUrl;
    private String nombreInversor;
    private String emailInversor;
    private String plataforma;
    private LocalDate fechaCompra;
    private String estrategia;
    private String observaciones;
    private LocalDateTime fechaRegistro;
    
    // Constructor vacío
    public CryptoRecord() {
    }
    
    // Constructor completo
    public CryptoRecord(String codigoRegistro, String coinId, String nombre, String simbolo,
                       BigDecimal precioActual, BigDecimal precioCompra, BigDecimal cantidadComprada,
                       BigDecimal valorTotal, BigDecimal cambio24h, BigDecimal cambio7d,
                       long marketCap, long volumen24h, String logoUrl, String nombreInversor,
                       String emailInversor, String plataforma, LocalDate fechaCompra,
                       String estrategia, String observaciones) {
        this.codigoRegistro = codigoRegistro;
        this.coinId = coinId;
        this.nombre = nombre;
        this.simbolo = simbolo;
        this.precioActual = precioActual;
        this.precioCompra = precioCompra;
        this.cantidadComprada = cantidadComprada;
        this.valorTotal = valorTotal;
        this.cambio24h = cambio24h;
        this.cambio7d = cambio7d;
        this.marketCap = marketCap;
        this.volumen24h = volumen24h;
        this.logoUrl = logoUrl;
        this.nombreInversor = nombreInversor;
        this.emailInversor = emailInversor;
        this.plataforma = plataforma;
        this.fechaCompra = fechaCompra;
        this.estrategia = estrategia;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCodigoRegistro() {
        return codigoRegistro;
    }
    
    public void setCodigoRegistro(String codigoRegistro) {
        this.codigoRegistro = codigoRegistro;
    }
    
    public String getCoinId() {
        return coinId;
    }
    
    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getSimbolo() {
        return simbolo;
    }
    
    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
    
    public BigDecimal getPrecioActual() {
        return precioActual;
    }
    
    public void setPrecioActual(BigDecimal precioActual) {
        this.precioActual = precioActual;
    }
    
    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }
    
    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }
    
    public BigDecimal getCantidadComprada() {
        return cantidadComprada;
    }
    
    public void setCantidadComprada(BigDecimal cantidadComprada) {
        this.cantidadComprada = cantidadComprada;
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
    
    public BigDecimal getCambio24h() {
        return cambio24h;
    }
    
    public void setCambio24h(BigDecimal cambio24h) {
        this.cambio24h = cambio24h;
    }
    
    public BigDecimal getCambio7d() {
        return cambio7d;
    }
    
    public void setCambio7d(BigDecimal cambio7d) {
        this.cambio7d = cambio7d;
    }
    
    public long getMarketCap() {
        return marketCap;
    }
    
    public void setMarketCap(long marketCap) {
        this.marketCap = marketCap;
    }
    
    public long getVolumen24h() {
        return volumen24h;
    }
    
    public void setVolumen24h(long volumen24h) {
        this.volumen24h = volumen24h;
    }
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
    
    public String getNombreInversor() {
        return nombreInversor;
    }
    
    public void setNombreInversor(String nombreInversor) {
        this.nombreInversor = nombreInversor;
    }
    
    public String getEmailInversor() {
        return emailInversor;
    }
    
    public void setEmailInversor(String emailInversor) {
        this.emailInversor = emailInversor;
    }
    
    public String getPlataforma() {
        return plataforma;
    }
    
    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }
    
    public LocalDate getFechaCompra() {
        return fechaCompra;
    }
    
    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
    
    public String getEstrategia() {
        return estrategia;
    }
    
    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    // Método para calcular ganancia/pérdida
    public BigDecimal calcularGananciaPerdida() {
        if (precioActual != null && precioCompra != null && cantidadComprada != null) {
            BigDecimal valorActual = precioActual.multiply(cantidadComprada);
            return valorActual.subtract(valorTotal);
        }
        return BigDecimal.ZERO;
    }
    
    // Método para calcular porcentaje de ganancia/pérdida
    public BigDecimal calcularPorcentajeGananciaPerdida() {
        if (precioCompra != null && precioCompra.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal diferencia = precioActual.subtract(precioCompra);
            return diferencia.divide(precioCompra, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
        return BigDecimal.ZERO;
    }
}

