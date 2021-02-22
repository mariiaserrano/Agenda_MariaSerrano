package dao;

import com.google.gson.Gson;
import encriptacion.Claves;
import io.vavr.control.Either;
import model.Usuario;
import model.UsuarioLogin;
import model.UsuarioRegistro;
import okhttp3.MediaType;
import retrofit.ApiError;
import retrofit.ApiUsuario;
import retrofit.ConfigurationSingleton_OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DaoUsuarios {

    private Claves cl = new Claves();

    public Either<ApiError, UsuarioRegistro> addUsuario(Usuario usuario) {
    Either<ApiError, UsuarioRegistro> resultado = null;
    //Generamos las claves del usuario
    Either<ApiError, KeyPair> claves = cl.generarClaves(usuario);

    Retrofit retrofit = ConfigurationSingleton_OkHttpClient.getInstance();

    ApiUsuario usuarioAdd = retrofit.create(ApiUsuario.class);

    //Cogemos la clave publica de las que hemos generado
    PublicKey clavePublica = claves.get().getPublic();
    //Codificamos en base64
    String clave64 = Base64.getUrlEncoder().encodeToString(clavePublica.getEncoded());
    //Generamos el usuarioRegistrado con el usuario y su clave publica
    UsuarioRegistro usuarioRegistro = new UsuarioRegistro(usuario,clave64);

    Call<UsuarioRegistro> call = usuarioAdd.addUser(usuarioRegistro);
    try {
        Response<UsuarioRegistro> response = call.execute();
        if (response.isSuccessful()) {
            UsuarioRegistro u = response.body();
            resultado = Either.right(u);
        } else {
            ApiError api = new ApiError();
            if (response.errorBody().contentType().equals(MediaType.get("application/json"))) {
                Gson g = new Gson();
                api = g.fromJson(response.errorBody().string(), ApiError.class);
            } else
                api.setMessage(response.errorBody().string());
            resultado = Either.left(api);
        }
    } catch (Exception e) {

        resultado = Either.left(ApiError.builder().message("Error de comunicacion").build());
    }

    return resultado;
}

    public Either<ApiError, String> login(UsuarioLogin login) {
        Either<ApiError, String> resultado = null;

        Retrofit retrofit = ConfigurationSingleton_OkHttpClient.getInstance();

        ApiUsuario usuariosApi = retrofit.create(ApiUsuario.class);

        Call<UsuarioLogin> call = usuariosApi.login(login);
        try {
            Response<UsuarioLogin> response = call.execute();
            if (response.isSuccessful())
            {


                resultado = Either.right(response.body().getFirma());
            }
            else
            {
                ApiError api = new ApiError();
                if (response.errorBody().contentType().equals(MediaType.get("application/json"))) {
                    Gson g = new Gson();
                    api = g.fromJson(response.errorBody().string(),ApiError.class);
                }
                else
                    api.setMessage(response.errorBody().string());
                resultado = Either.left(api);
            }
        }
        catch (Exception e)
        {

            resultado= Either.left(ApiError.builder().message("Error de comunicacion").build());
        }

        return resultado;
    }


}
