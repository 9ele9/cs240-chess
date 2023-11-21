package server.handlers;

import com.google.gson.Gson;
import service.CreateGameService;
import requests.CreateGameRequest;
import responses.CreateGameResponse;
import spark.Request;
import spark.Spark;

public class CreateGameHandler {
    public CreateGameHandler(){run();}
    public void run(){
        Spark.post("/game", (req, res) -> {
            createBody bodyObj = getBody(req, createBody.class);
            String headObj = getHeader(req);
            res.type("application/json");

            CreateGameRequest request = new CreateGameRequest(bodyObj.getGameName());
            CreateGameResponse response = new CreateGameService().createGame(request, headObj);
            res.status(response.getReturnCode());
            return new Gson().toJson(response);
        });
    }
    private static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    private static String getHeader(Request request) {
        var head = request.headers("Authorization");
        if (head == null) {
            throw new RuntimeException("missing required header");
        }
        return head;
    }

    public static class createBody {
        String gameName;

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }
    }
}
