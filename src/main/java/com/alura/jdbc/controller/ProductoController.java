package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public int modificar(String nombre, String descripcion, Integer id, Integer cantidad) throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();

		PreparedStatement statement = con.prepareStatement("UPDATE producto SET " +
				"NOMBRE = ?, " +
				"DESCRIPCION = ?, " +
				"CANTIDAD = ? " +
				"WHERE ID = ?");
		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);
		statement.setInt(4, id);
		statement.execute();

		int updateCount = statement.getUpdateCount();
		con.close();
		return updateCount;
	}

	public int eliminar(Integer id) throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();

		PreparedStatement statement = con.prepareStatement("DELETE FROM producto WHERE ID = ?");
		statement.setInt(1,  id);
		statement.execute();

		return statement.getUpdateCount();
	}

	public List<Map<String, String>> listar() throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();
		PreparedStatement statement = con.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM producto");
		statement.execute();
//		System.out.println(result);
		ResultSet resultSet = statement.getResultSet();

		List<Map<String, String>> resultado = new ArrayList<>();
		while(resultSet.next()) {
			Map<String, String> fila = new HashMap<>();
			fila.put("ID", String.valueOf(resultSet.getInt("id")));
			fila.put("NOMBRE", resultSet.getString("NOMBRE"));
			fila.put("DESCRIPCION", resultSet.getString("DESCRIPCION"));
			fila.put("CANTIDAD", String.valueOf(resultSet.getInt("CANTIDAD")));

			resultado.add(fila);
		}

		con.close();
		return resultado;
	}

    public void guardar(HashMap<String, String> producto) throws SQLException {
		String nombre = producto.get("NOMBRE");
		String descripcion = producto.get("DESCRIPCION");
		Integer cantidad = Integer.valueOf(producto.get("CANTIDAD"));
		Integer maximaCantidad = 50;
		
		Connection con = new ConnectionFactory().recuperarConexion();
		con.setAutoCommit(false);

		PreparedStatement statement = con.prepareStatement("INSERT INTO producto (nombre, descripcion, cantidad)"
				+ " VALUES (?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		try {
			do {
				int cantidadParaGuardar = Math.min(cantidad, maximaCantidad);
				ejecutaRegistro(nombre, descripcion, cantidadParaGuardar, statement);
				cantidad -= maximaCantidad;
			} while (cantidad > 0);
			con.commit();
			System.out.println("COMMIT");
		} catch (Exception e) {
			con.rollback();
			System.out.println("ROLLBACK");
		}
		statement.close();

		con.close();
	}

	private static void ejecutaRegistro(String nombre, String descripcion, Integer cantidad, PreparedStatement statement) throws SQLException {
		if (cantidad < 50) {
			throw new RuntimeException("ocurrrio un problema");
		}
		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);

		statement.execute();

		ResultSet resultSet = statement.getGeneratedKeys();

		while (resultSet.next()) {
			System.out.println(
					String.format(
						"Fue inseratado un producto de ID %d",
							resultSet.getInt(1)));
		}
	}

}
