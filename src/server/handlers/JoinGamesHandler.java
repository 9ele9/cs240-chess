package server.handlers;

import com.google.gson.Gson;
import service.JoinGameService;
import requests.JoinGameRequest;
import responses.JoinGameResponse;
import spark.Request;
import spark.Spark;

public class JoinGamesHandler {
    public JoinGamesHandler(){run();}
    public void run(){
        Spark.put("/game", (req, res) -> {
            joinBody bodyObj = getBody(req, joinBody.class);
            String headObj = getHeader(req);
            res.type("application/json");

            JoinGameRequest request = new JoinGameRequest(bodyObj.getPlayerColor(), bodyObj.getGameID());
            JoinGameResponse response = new JoinGameService().joinGame(request, headObj);
            res.status(response.getReturnCode());
            if(response.getReturnCode() == 200){
                return new Gson().toJson(new Object());
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

    private static String getHeader(Request request) {
        var head = request.headers("Authorization");
        if (head == null) {
            throw new RuntimeException("missing required header");
        }
        return head;
    }

    public static class joinBody {
        String playerColor = null;
        int gameID;

        public String getPlayerColor() {
            return playerColor;
        }

        public void setPlayerColor(String playerColor) {
            this.playerColor = playerColor;
        }

        public int getGameID() {
            return gameID;
        }

        public void setGameID(int gameID) {
            this.gameID = gameID;
        }
    }
}
