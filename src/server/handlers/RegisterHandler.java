package server.handlers;

import com.google.gson.Gson;
import service.requests.RegisterRequest;
import service.responses.RegisterResponse;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import service.RegisterService;
import chess_server.User;

import java.util.Map;

/**
 * Your Handlers will convert an HTTP request into Java usable objects & data.
 * That data can then be passed to the services to execute the API.
 * Finally, your Handlers will convert the data returned by Services into an HTTP result.
 */
public class RegisterHandler {

    public RegisterHandler(){
        run();
    }
    public void run(){
        Spark.post("/user", (req, res) -> {
            User bodyObj = getBody(req, User.class);
            res.type("application/json");
            RegisterRequest request = new RegisterRequest(bodyObj);
            RegisterResponse response = new RegisterService().register(request);
            res.status(response.getReturnCode());
            if(response.getReturnCode() == 200){
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
