package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import service.requests.CreateGameRequest;
import service.responses.CreateGameResponse;

import java.sql.SQLException;

/**
 * Creates a new game.
 */
public class CreateGameService {
    /**
     * Creates a new game.
     */
    public CreateGameResponse createGame(CreateGameRequest request, String token) throws SQLException, DataAccessException {
        if(request.getGameName() == null || request.getGameName().isEmpty()){
            return new CreateGameResponse("Error: bad request", 400);
        }
        AuthDAO checkLoggedIn = new AuthDAO();
        if(!checkLoggedIn.validate(token)){
            return new CreateGameResponse("Error: unauthorized", 401);
        }else{
            GameDAO newGame = new GameDAO();
            newGame.addGame(request.getGameName());
            if(newGame.getGameByName(request.getGameName()) != null){
                return new CreateGameResponse(newGame.getGameByName(request.getGameName()).getGameID());
            }
        }
        return new CreateGameResponse("Error: description", 500);
    }
}
