package retrofit;

import model.Cita;
import model.UsuarioRegistro;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiCitas {
    @POST("citas")
    Call<Cita> addCita(@Body Cita cita);
}
