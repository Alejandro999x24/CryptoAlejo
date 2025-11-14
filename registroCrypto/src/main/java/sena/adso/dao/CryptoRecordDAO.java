package sena.adso.dao;

import sena.adso.model.CryptoRecord;
import sena.adso.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CryptoRecordDAO {

    public CryptoRecordDAO() {
        ensureTableExists();
    }

    // Crear un nuevo registro
    public boolean crear(CryptoRecord cryptoRecord) {
        String sql = "INSERT INTO crypto_record (codigo_registro, coin_id, nombre, simbolo, precio_actual, " +
                "precio_compra, cantidad_comprada, valor_total, cambio_24h, cambio_7d, market_cap, " +
                "volumen_24h, logo_url, nombre_inversor, email_inversor, plataforma, fecha_compra, " +
                "estrategia, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cryptoRecord.getCodigoRegistro());
            pstmt.setString(2, cryptoRecord.getCoinId());
            pstmt.setString(3, cryptoRecord.getNombre());
            pstmt.setString(4, cryptoRecord.getSimbolo());
            pstmt.setBigDecimal(5, cryptoRecord.getPrecioActual());
            pstmt.setBigDecimal(6, cryptoRecord.getPrecioCompra());
            pstmt.setBigDecimal(7, cryptoRecord.getCantidadComprada());
            pstmt.setBigDecimal(8, cryptoRecord.getValorTotal());
            pstmt.setBigDecimal(9, cryptoRecord.getCambio24h());
            pstmt.setBigDecimal(10, cryptoRecord.getCambio7d());
            pstmt.setLong(11, cryptoRecord.getMarketCap());
            pstmt.setLong(12, cryptoRecord.getVolumen24h());
            pstmt.setString(13, cryptoRecord.getLogoUrl());
            pstmt.setString(14, cryptoRecord.getNombreInversor());
            pstmt.setString(15, cryptoRecord.getEmailInversor());
            pstmt.setString(16, cryptoRecord.getPlataforma());
            pstmt.setDate(17, Date.valueOf(cryptoRecord.getFechaCompra()));
            pstmt.setString(18, cryptoRecord.getEstrategia());
            pstmt.setString(19, cryptoRecord.getObservaciones());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al crear registro: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("SQL Error al crear registro (state=" + e.getSQLState() +
                    ", code=" + e.getErrorCode() + "): " + e.getMessage(), e);
        }
    }   // ← ESTA LLAVE FALTABA

    // Crear tabla si no existe
    private void ensureTableExists() {
        String sql = "CREATE TABLE IF NOT EXISTS crypto_record (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "codigo_registro VARCHAR(50) UNIQUE NOT NULL, " +
                "coin_id VARCHAR(100) NOT NULL, " +
                "nombre VARCHAR(200) NOT NULL, " +
                "simbolo VARCHAR(20) NOT NULL, " +
                "precio_actual DECIMAL(20, 8) NOT NULL, " +
                "precio_compra DECIMAL(20, 8) NOT NULL, " +
                "cantidad_comprada DECIMAL(20, 8) NOT NULL, " +
                "valor_total DECIMAL(20, 8) NOT NULL, " +
                "cambio_24h DECIMAL(10, 4) DEFAULT 0, " +
                "cambio_7d DECIMAL(10, 4) DEFAULT 0, " +
                "market_cap BIGINT DEFAULT 0, " +
                "volumen_24h BIGINT DEFAULT 0, " +
                "logo_url VARCHAR(500), " +
                "nombre_inversor VARCHAR(200) NOT NULL, " +
                "email_inversor VARCHAR(200) NOT NULL, " +
                "plataforma VARCHAR(100), " +
                "fecha_compra DATE NOT NULL, " +
                "estrategia VARCHAR(100), " +
                "observaciones TEXT, " +
                "fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "INDEX idx_codigo_registro (codigo_registro), " +
                "INDEX idx_coin_id (coin_id), " +
                "INDEX idx_email_inversor (email_inversor)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.println("No se pudo verificar/crear la tabla crypto_record: " + e.getMessage());
        }
    }

    // Leer todos los registros
    public List<CryptoRecord> leerTodos() {
        List<CryptoRecord> registros = new ArrayList<>();
        String sql = "SELECT * FROM crypto_record ORDER BY fecha_registro DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                registros.add(mapResultSetToCryptoRecord(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al leer registros: " + e.getMessage());
            e.printStackTrace();
        }

        return registros;
    }

    // Leer por ID
    public CryptoRecord leerPorId(int id) {
        String sql = "SELECT * FROM crypto_record WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCryptoRecord(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer registro por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Leer por código de registro
    public CryptoRecord leerPorCodigoRegistro(String codigoRegistro) {
        String sql = "SELECT * FROM crypto_record WHERE codigo_registro = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigoRegistro);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCryptoRecord(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error al leer registro por código: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Actualizar registro
    public boolean actualizar(CryptoRecord cryptoRecord) {
        String sql = "UPDATE crypto_record SET coin_id = ?, nombre = ?, simbolo = ?, precio_actual = ?, " +
                "precio_compra = ?, cantidad_comprada = ?, valor_total = ?, cambio_24h = ?, cambio_7d = ?, " +
                "market_cap = ?, volumen_24h = ?, logo_url = ?, nombre_inversor = ?, email_inversor = ?, " +
                "plataforma = ?, fecha_compra = ?, estrategia = ?, observaciones = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cryptoRecord.getCoinId());
            pstmt.setString(2, cryptoRecord.getNombre());
            pstmt.setString(3, cryptoRecord.getSimbolo());
            pstmt.setBigDecimal(4, cryptoRecord.getPrecioActual());
            pstmt.setBigDecimal(5, cryptoRecord.getPrecioCompra());
            pstmt.setBigDecimal(6, cryptoRecord.getCantidadComprada());
            pstmt.setBigDecimal(7, cryptoRecord.getValorTotal());
            pstmt.setBigDecimal(8, cryptoRecord.getCambio24h());
            pstmt.setBigDecimal(9, cryptoRecord.getCambio7d());
            pstmt.setLong(10, cryptoRecord.getMarketCap());
            pstmt.setLong(11, cryptoRecord.getVolumen24h());
            pstmt.setString(12, cryptoRecord.getLogoUrl());
            pstmt.setString(13, cryptoRecord.getNombreInversor());
            pstmt.setString(14, cryptoRecord.getEmailInversor());
            pstmt.setString(15, cryptoRecord.getPlataforma());
            pstmt.setDate(16, Date.valueOf(cryptoRecord.getFechaCompra()));
            pstmt.setString(17, cryptoRecord.getEstrategia());
            pstmt.setString(18, cryptoRecord.getObservaciones());
            pstmt.setInt(19, cryptoRecord.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar registro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar registro
    public boolean eliminar(int id) {
        String sql = "DELETE FROM crypto_record WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar registro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Buscar por email del inversor
    public List<CryptoRecord> buscarPorEmail(String email) {
        List<CryptoRecord> registros = new ArrayList<>();
        String sql = "SELECT * FROM crypto_record WHERE email_inversor = ? ORDER BY fecha_registro DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                registros.add(mapResultSetToCryptoRecord(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar por email: " + e.getMessage());
            e.printStackTrace();
        }

        return registros;
    }

    // Mapear ResultSet a CryptoRecord
    private CryptoRecord mapResultSetToCryptoRecord(ResultSet rs) throws SQLException {
        CryptoRecord record = new CryptoRecord();
        record.setId(rs.getInt("id"));
        record.setCodigoRegistro(rs.getString("codigo_registro"));
        record.setCoinId(rs.getString("coin_id"));
        record.setNombre(rs.getString("nombre"));
        record.setSimbolo(rs.getString("simbolo"));
        record.setPrecioActual(rs.getBigDecimal("precio_actual"));
        record.setPrecioCompra(rs.getBigDecimal("precio_compra"));
        record.setCantidadComprada(rs.getBigDecimal("cantidad_comprada"));
        record.setValorTotal(rs.getBigDecimal("valor_total"));
        record.setCambio24h(rs.getBigDecimal("cambio_24h"));
        record.setCambio7d(rs.getBigDecimal("cambio_7d"));
        record.setMarketCap(rs.getLong("market_cap"));
        record.setVolumen24h(rs.getLong("volumen_24h"));
        record.setLogoUrl(rs.getString("logo_url"));
        record.setNombreInversor(rs.getString("nombre_inversor"));
        record.setEmailInversor(rs.getString("email_inversor"));
        record.setPlataforma(rs.getString("plataforma"));

        Date fechaCompra = rs.getDate("fecha_compra");
        if (fechaCompra != null) {
            record.setFechaCompra(fechaCompra.toLocalDate());
        }

        record.setEstrategia(rs.getString("estrategia"));
        record.setObservaciones(rs.getString("observaciones"));

        Timestamp fechaRegistro = rs.getTimestamp("fecha_registro");
        if (fechaRegistro != null) {
            record.setFechaRegistro(fechaRegistro.toLocalDateTime());
        }

        return record;
    }
}
