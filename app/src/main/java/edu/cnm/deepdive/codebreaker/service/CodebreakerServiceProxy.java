package edu.cnm.deepdive.codebreaker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.codebreaker.model.Game;
import edu.cnm.deepdive.codebreaker.model.Guess;
import io.reactivex.Single;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Proxy interface (implemented by Retrofit). &hellip;
 */
public interface CodebreakerServiceProxy {

  /**
   * Constructs and returns a {@link Single} instance that may be used to send a request to the web
   * service to generate a new secret code (start a new game).
   *
   * @param game "Stub" of {@link Game} specifying the characters that can be used in the code
   *             generated by the web service, and the length of the code to be generated.
   * @return {@link Single} instance that may be used to send the HTTP request and receive the
   * response.
   */
  @POST("codes")
  Single<Game> startGame(@Body Game game);

  @GET("codes/{id}")
  Single<Game> getGame(@Path("id") String id);

  @POST("codes/{id}/guesses")
  Single<Guess> submitGuess(@Path("id") String id, @Body Guess guess);

  @GET("codes/{id}/guesses")
  Single<List<Guess>> getGuesses(@Path("id") String id);

  static CodebreakerServiceProxy getInstance() {
    return InstanceHolder.INSTANCE;
  }

  static Gson getGsonInstance() {
    return InstanceHolder.GSON;
  }

  /**
   * Implements the "lazy" <a
   * href="https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">initialization-on-demand
   * holder idiom</a>. This creates an instance of {@link CodebreakerServiceProxy} only when the
   * {@code InstanceHolder} class is initialized; since this initialization happens only once, and
   * since only 1 thread is allowed to load a class into memory, this guarantees that only one
   * instance of {@link CodebreakerServiceProxy} will be created&mdash;that is, {@code
   * CodebreakerServiceProxy} is a "singleton".
   */
  class InstanceHolder {

    private static final Gson GSON;
    private static final CodebreakerServiceProxy INSTANCE;

    static {
      // Creation of instances of Gson, OkHttpClient, Retrofit all employ the "builder pattern", as
      // supported by those libraries.
      GSON = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(Level.NONE);
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor)
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl("https://ddc-java.services/codebreaker/")
          .addConverterFactory(GsonConverterFactory.create(GSON))
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(client)
          .build();
      INSTANCE = retrofit.create(CodebreakerServiceProxy.class);
    }

  }

}
