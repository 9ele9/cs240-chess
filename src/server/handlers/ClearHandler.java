package server.handlers;

import chess_server.User;
import com.google.gson.Gson;
import service.ClearService;
import service.RegisterService;
import service.requests.RegisterRequest;
import service.responses.ClearResponse;
import spark.Request;
import spark.Spark;

public class ClearHandler {
    public ClearHandler(){run();}
    public void run(){
        Spark.delete("/db", (req, res) -> {
            res.type("application/json");
            ClearResponse result = new ClearService().clear();
            res.status(result.getReturnCode());
            if(res.status() != 200){
                return new Gson().toJson(result);
            }else{
                return new Gson().toJson(new Object());
            }

        });
    }
}
