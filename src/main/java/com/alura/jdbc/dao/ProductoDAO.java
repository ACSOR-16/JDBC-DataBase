package com.alura.jdbc.dao;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoDAO {
    final private Connection con;

    public ProductoDAO(Connection con) {
        this.con = con;
    }

    public void guardar(Producto producto) {
        try (con) {
            final PreparedStatement statement = con.prepareStatement("INSERT INTO producto (nombre, descripcion, cantidad)"
                            + " VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            try (statement) {
                ejecutaRegistro(producto, statement);
                con.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ejecutaRegistro(Producto producto, PreparedStatement statement) throws SQLException {
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setInt(3, producto.getCantidad());

        statement.execute();

        final ResultSet resultSet = statement.getGeneratedKeys();

        try(resultSet) {
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format(
                        "Fue inseratado un producto de ID %s",
                        producto));
            }
        }
    }

    public List<Producto> listar() {
        List<Producto> resultado = new ArrayList<>();

        final Connection con = new ConnectionFactory().recuperarConexion();

        try(con) {
            final PreparedStatement statement = con.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM producto");
            try(statement) {
                statement.execute();

                final ResultSet resultSet = statement.getResultSet();

                try(resultSet) {
                    while (resultSet.next()) {
                        Producto fila = new Producto(resultSet.getInt("id"),
                                resultSet.getString("NOMBRE"),
                                resultSet.getString("DESCRIPCION"),
                                resultSet.getInt("CANTIDAD"));

                        resultado.add(fila);
                    }
                }
            }
            return resultado;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
