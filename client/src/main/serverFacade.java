import chess_server.Game;
import com.google.gson.Gson;
import requests.*;
import responses.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;

public class serverFacade {
    public String callLogin(LoginRequest newLogin) throws Exception {
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(newLogin);
            outputStream.write(jsonBody.getBytes());
        }
        http.connect();
        if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return "";
        }else{
            return getAuthFromBody(http, LoginResponse.class);
        }
    }

    public String callRegister(RegisterRequest newRegister) throws Exception{
        URI uri = new URI("http://localhost:8080/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(newRegister);
            outputStream.write(jsonBody.getBytes());
        }
        //System.out.print(http.getOutputStream() + "\n");
        http.connect();
        if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
            return "";
        }else{
            return getAuthFromBody(http, RegisterResponse.class);
        }

    }

    public HashSet<Game> callList(String auth) throws Exception{
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth);
        http.connect();
        http.getResponseMessage();
        return readResponseBody(http);
    }

    public int callLogout(String auth) throws Exception{
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth);
        http.connect();
        return http.getResponseCode();
    }



    public int callJoin(JoinGameRequest newJoin, String auth) throws Exception{
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(newJoin);
            outputStream.write(jsonBody.getBytes());
        }
        http.connect();
        return http.getResponseCode();
    }

    public int callCreate(CreateGameRequest newCreate, String auth) throws Exception{
        URI uri = new URI("http://localhost:8080/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("Authorization", auth);
        try (var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(newCreate);
            outputStream.write(jsonBody.getBytes());
        }
        //System.out.print(http.getOutputStream() + "\n");
        http.connect();
        if(http.getResponseCode() == 200){
            return getIDFromBody(http, CreateGameResponse.class);
        }else{
            return 5;
        }

        //System.out.print(http.getResponseMessage() + "\n");
    }

    public void callClear() throws Exception {
        URI uri = new URI("http://localhost:8080/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.addRequestProperty("Content-Type", "application/json");
        http.connect();
        int test = http.getResponseCode();
    }

    private static <T> String getAuthFromBody(HttpURLConnection http, Class<T> clazz) throws Exception {
        String auth = "";
        Map responseBody;
        Object test;
        try (InputStream respBody = http.getInputStream()) {

            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

            responseBody = new Gson().fromJson(inputStreamReader, Map.class);
            test = responseBody.get("authToken");
            auth = test.toString();

        }
        return auth;
    }

    private static <T> int getIDFromBody(HttpURLConnection http, Class<T> clazz) throws Exception {
        int auth;
        double auth2;
        Map responseBody;
        Object test;
        try (InputStream respBody = http.getInputStream()) {

            InputStreamReader inputStreamReader = new InputStreamReader(respBody);

            responseBody = new Gson().fromJson(inputStreamReader, Map.class);
            test = responseBody.get("gameID");
            auth2 = Double.parseDouble(test.toString());
            auth = (int) auth2;

        }
        return auth;
    }

    private static HashSet<Game> readResponseBody(HttpURLConnection http) throws Exception {
        HashSet<Game> responseBody;
        ListGameResponse response;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            response = new Gson().fromJson(inputStreamReader, ListGameResponse.class);
            responseBody = response.getGames();
        }
        return responseBody;
    }
}
