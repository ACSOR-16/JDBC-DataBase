package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;
import com.alura.jdbc.dao.ProductoDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {
	private  ProductoDAO productoDAO;

	public ProductoController() {
		this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperarConexion());
	}

	public int modificar(String nombre, String descripcion, Integer id, Integer cantidad) throws SQLException {
		final Connection con = new ConnectionFactory().recuperarConexion();
		try(con) {
			final  PreparedStatement statement = con.prepareStatement("UPDATE producto SET " +
					"NOMBRE = ?, " +
					"DESCRIPCION = ?, " +
					"CANTIDAD = ? " +
					"WHERE ID = ?");

			try (statement) {
				statement.setString(1, nombre);
				statement.setString(2, descripcion);
				statement.setInt(3, cantidad);
				statement.setInt(4, id);
				statement.execute();

				int updateCount = statement.getUpdateCount();

				return updateCount;
			}
		}
	}

	public int eliminar(Integer id) throws SQLException {
		final Connection con = new ConnectionFactory().recuperarConexion();

		try (con) {
			final PreparedStatement statement = con.prepareStatement("DELETE FROM producto WHERE ID = ?");
			try (statement) {
				statement.setInt(1, id);
				statement.execute();
				return statement.getUpdateCount();
			}
		}
	}

	public List<Map<String, String>> listar() throws SQLException {
		final Connection con = new ConnectionFactory().recuperarConexion();

		try(con) {
			final PreparedStatement statement = con.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM producto");
			try(statement) {
				statement.execute();

//		System.out.println(result);
				ResultSet resultSet = statement.getResultSet();

				List<Map<String, String>> resultado = new ArrayList<>();
				while (resultSet.next()) {
					Map<String, String> fila = new HashMap<>();
					fila.put("ID", String.valueOf(resultSet.getInt("id")));
					fila.put("NOMBRE", resultSet.getString("NOMBRE"));
					fila.put("DESCRIPCION", resultSet.getString("DESCRIPCION"));
					fila.put("CANTIDAD", String.valueOf(resultSet.getInt("CANTIDAD")));

					resultado.add(fila);
				}
				return resultado;
			}
		}
	}

	public void guardar(Producto producto) {
		ProductoDAO productoDAO = new ProductoDAO(new ConnectionFactory().recuperarConexion());
		productoDAO.guardar(producto);
	}

}
