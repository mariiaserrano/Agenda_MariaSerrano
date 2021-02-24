package dao;

import io.vavr.control.Either;
import model.Cita;
import model.UsuarioRegistro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoCitas {
    private static final String QUERY_INSERT_CITAS = "insert into Cita (nombreEmisor,descripcion,firma,claveSimetrica,lugar,hora,iv,salt,it) values (?,?,?,?,?,?,?,?,?)";

    public Either<String, Cita> addCitas(Cita cita) {
        AtomicReference<Either<String, Cita>> result = new AtomicReference<>();
        DBConnection dbConnection = new DBConnection();
        ResultSet resultSet = null;
        Connection con = null;
        PreparedStatement stmt = null;
        boolean anadido = false;

        try {
            con = dbConnection.getConnection();

            stmt = con.prepareStatement(QUERY_INSERT_CITAS);
            stmt.setString(1, cita.getNombreEmisor());
            stmt.setString(2, cita.getDescripcion());
            stmt.setString(3, cita.getFirma());
            stmt.setString(4, cita.getClaveSimetrica());
            stmt.setString(5, cita.getLugar());
            stmt.setString(6, cita.getFecha());
            stmt.setString(7, cita.getIv());
            stmt.setString(8, cita.getSalt());
            stmt.setInt(9, cita.getIteraciones());


            int filasAnadidas = -1;
            filasAnadidas = stmt.executeUpdate();


            if (filasAnadidas > 0) {
                anadido = true;
                result.set(Either.right(cita));
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
