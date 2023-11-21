package server.handlers;

import chess_server.User;
import com.google.gson.Gson;
import requests.LoginRequest;
import responses.LoginResponse;
import service.LoginService;
import spark.Request;
import spark.Spark;

public class LoginHandler {
    public LoginHandler(){
        run();
    }

    public void run(){
        Spark.post("/session", (req, res) -> {
            User bodyObj = getBody(req, User.class);
            res.type("application/json");
            LoginRequest request = new LoginRequest(bodyObj);
            LoginResponse response = new LoginService().login(request);
            res.status(response.getReturnCode());
            if(response.getReturnCode() == 200 && response.getAuthToken() != null){
                return new Gson().toJson(response.getAuthToken());
            }else{
                return new Gson().toJson(response);
            }
        });
    }
    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }
}
