package dao;

import com.google.gson.Gson;
import io.vavr.control.Either;
import model.Cita;
import model.Usuario;
import model.UsuarioRegistro;
import okhttp3.MediaType;
import retrofit.ApiCitas;
import retrofit.ApiError;
import retrofit.ApiUsuario;
import retrofit.ConfigurationSingleton_OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;

public class DaoCitas {

    public Either<ApiError, Cita> addCita(Cita cita) {
        Either<ApiError, Cita> resultado = null;

        Retrofit retrofit = ConfigurationSingleton_OkHttpClient.getInstance();

        ApiCitas addCitas = retrofit.create(ApiCitas.class);


        Call<Cita> call = addCitas.addCita(cita);
        try {
            Response<Cita> response = call.execute();
            if (response.isSuccessful()) {
                Cita u = response.body();
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

}
