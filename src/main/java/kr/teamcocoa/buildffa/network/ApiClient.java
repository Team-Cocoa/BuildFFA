package kr.teamcocoa.buildffa.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import org.bukkit.configuration.file.FileConfiguration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class ApiClient {

  private final Retrofit retrofit;

  @Inject
  public ApiClient(FileConfiguration config) {
    String baseUrl = config.getString("api.base-url", "http://localhost:8080/");
    if (!baseUrl.endsWith("/")) {
      baseUrl += "/";
    }
    int timeoutSeconds = config.getInt("api.timeout-seconds", 5);

    OkHttpClient.Builder httpClient =
        new OkHttpClient.Builder()
            .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutSeconds, TimeUnit.SECONDS);

    retrofit =
        new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
  }

  public <T> T create(Class<T> service) {
    return retrofit.create(service);
  }
}
