package server.handlers;

import com.google.gson.Gson;
import service.CreateGameService;
import service.ListGamesService;
import service.requests.CreateGameRequest;
import service.responses.CreateGameResponse;
import service.responses.ListGameResponse;
import spark.Request;
import spark.Spark;

public class ListGamesHandler {
    public ListGamesHandler(){run();}

    public void run(){
        Spark.get("/game", (req, res) -> {
            String headObj = getHeader(req);
            res.type("application/json");
            ListGameResponse response = new ListGamesService().listGames(headObj);
            res.status(response.getReturnCode());
            return new Gson().toJson(response);
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
