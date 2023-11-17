package server.handlers;

import com.google.gson.Gson;
import service.LoginService;
import service.LogoutService;
import service.requests.LoginRequest;
import service.responses.LoginResponse;
import service.responses.LogoutResponse;
import spark.Request;
import spark.Spark;

public class LogoutHandler {
    public LogoutHandler(){
        run();
    }

    public void run(){
        Spark.delete("/session", (req, res) -> {
            String headObj = getHeader(req);
            res.type("application/json");
            LogoutResponse response = new LogoutService().logout(headObj);

            res.status(response.getReturnCode());
            if(response.getReturnCode() == 200){
                return new Gson().toJson(new Object());
            }else{
                return new Gson().toJson(response);
            }
        });
    }
    private static String getHeader(Request request) {
        var head = request.headers("Authorization");
        if (head == null) {
            throw new RuntimeException("missing required header");
        }
        return head;
    }
}
