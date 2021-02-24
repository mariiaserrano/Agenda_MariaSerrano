package retrofit;

import model.ClienteCitas;
import model.Usuario;
import model.UsuarioLogin;
import model.UsuarioRegistro;
import retrofit2.Call;
import retrofit2.http.*;

import java.security.PublicKey;

public interface ApiUsuario {

    @POST("usuario")
    Call<UsuarioRegistro> addUser(@Body UsuarioRegistro usuario);

    @GET("usuario")
    Call<ClienteCitas> login(@Query("nombre") String nombre, @Query("firma") String firma);

}
