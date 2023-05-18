package com.alura.jdbc.pruebas;

import com.alura.jdbc.factory.ConnectionFactory;
import com.sun.source.tree.ReturnTree;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PruebaDelete {
    public static void main(String[] args) throws SQLException {
        Connection con = new ConnectionFactory().recuperarConexion();

        Statement statement = con.createStatement();
        statement.execute("DELETE FROM producto WHERE ID = 99");

        System.out.println(statement.getUpdateCount());
    }
}
