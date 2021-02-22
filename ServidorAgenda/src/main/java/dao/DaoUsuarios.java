package dao;

import io.vavr.control.Either;
import model.Usuario;
import model.UsuarioRegistro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoUsuarios {
    private static final String QUERY_INSERT_USUARIO = "insert into usuario (nombre,rutaCert) values (?,?)";
    private static final String QUERY_UPDATE_USUARIO = "update usuario set rutaCert=? where nombre=?";


    public Either<String, UsuarioRegistro> addUsuario(UsuarioRegistro usuario) {
        AtomicReference<Either<String, UsuarioRegistro>> result = new AtomicReference<>();
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = null;
        Connection con = null;
        PreparedStatement stmt = null;
        boolean anadido = false;

        try {
            con = dbConnection.getConnection();

            stmt = con.prepareStatement(QUERY_INSERT_USUARIO);
            stmt.setString(1, usuario.getUsuario().getUsuario());
            stmt.setString(2,usuario.getUsuario().getRutaCert());

            int filasAnadidas = -1;
            filasAnadidas = stmt.executeUpdate();


            if (filasAnadidas > 0) {
                anadido = true;
                result.set(Either.right(usuario));
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DaoUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DaoUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbConnection.cerrarConexion(con);
            dbConnection.cerrarResultSet(resultSet);
            dbConnection.cerrarStatement(stmt);

        }

        return result.get();

    }

    public Either<String, UsuarioRegistro> updateRutaCert(UsuarioRegistro usuario) {
        AtomicReference<Either<String, UsuarioRegistro>> result = new AtomicReference<>();
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = null;
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = dbConnection.getConnection();

            stmt = con.prepareStatement(QUERY_UPDATE_USUARIO);
            stmt.setString(1, usuario.getUsuario().getRutaCert());
            stmt.setString(2, usuario.getUsuario().getUsuario());

            int filasAnadidas = -1;
            filasAnadidas = stmt.executeUpdate();


            if (filasAnadidas > 0) {
                result.set(Either.right(usuario));
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DaoUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DaoUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            dbConnection.cerrarConexion(con);
            dbConnection.cerrarResultSet(resultSet);
            dbConnection.cerrarStatement(stmt);

        }

        return result.get();

    }

}
