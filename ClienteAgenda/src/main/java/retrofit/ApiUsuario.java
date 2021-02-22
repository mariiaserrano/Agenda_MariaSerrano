package retrofit;

import model.Usuario;
import model.UsuarioLogin;
import model.UsuarioRegistro;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.security.PublicKey;

public interface ApiUsuario {

    @POST("usuario")
    Call<UsuarioRegistro> addUser(@Body UsuarioRegistro usuario);

    @GET("usuario")
    Call<UsuarioLogin> login(@Body UsuarioLogin login);
}
