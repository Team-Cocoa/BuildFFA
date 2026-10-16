package kr.teamcocoa.buildffa.network.stats;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface StatsService {

  /** 200 with the stats, or 404 if the player has no stats yet */
  @GET("stats/{uuid}")
  Call<StatsDto> getStats(@Path("uuid") String uuid);

  @PUT("stats/{uuid}")
  Call<Void> upsertStats(@Path("uuid") String uuid, @Body StatsDto stats);
}
