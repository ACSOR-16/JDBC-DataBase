package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public void modificar(String nombre, String descripcion, Integer id) {
		// TODO
	}

	public void eliminar(Integer id) {
		// TODO
	}

	public List<Map<String, String>> listar() throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();
		Statement statement = con.createStatement();
		boolean result = statement.execute("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM producto");
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
		Connection con = new ConnectionFactory().recuperarConexion();

		Statement statement = con.createStatement();

		statement.execute("INSERT INTO producto (nombre, descripcion, cantidad)"
				+ " VALUES ('" + producto.get("NOMBRE") + "', '"
				+ producto.get("DESCRIPCION") + "',"
				+ producto.get("CANTIDAD") + ")", Statement.RETURN_GENERATED_KEYS);

		ResultSet resultSet = statement.getGeneratedKeys();

		while (resultSet.next()) {
			System.out.println(
					String.format(
						"Fue inseratado un producto de ID %d",
							resultSet.getInt(1)));
		}
	}

}
