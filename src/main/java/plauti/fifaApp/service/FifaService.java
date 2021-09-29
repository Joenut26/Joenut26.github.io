package plauti.fifaApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import plauti.fifaApp.model.Game;
import plauti.fifaApp.model.Player;
import plauti.fifaApp.model.Storage;
import plauti.fifaApp.model.Team;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class FifaService {

    final Storage storage = new Storage();

    public Game setGame(Team teamOne, Team teamTwo) {

        //get winner
        if (teamOne.getGoals() > teamTwo.getGoals()) {
            teamOne.setStatus(Team.Status.WON);
            teamTwo.setStatus(Team.Status.LOST);
        } else if (teamOne.getGoals() < teamTwo.getGoals()) {
            teamOne.setStatus(Team.Status.LOST);
            teamTwo.setStatus(Team.Status.WON);
        } else if (teamOne.getGoals().equals(teamTwo.getGoals())) {
            teamOne.setStatus(Team.Status.DRAW);
            teamTwo.setStatus(Team.Status.DRAW);
        }

        Game game = new Game();
        game.setTeamOne(teamOne);
        game.setTeamTwo(teamTwo);
        game.setDate(LocalDate.now());


        //calculations
        //update player stats
        String player1Name = game.getTeamOne().getPlayer();
        String player2Name = game.getTeamTwo().getPlayer();

        updatePlayer1(player1Name, game);
        updatePlayer2(player2Name, game);

        //log game
        Storage.gameMap.put(UUID.randomUUID(), game);
        //update ranking


        return game;

    }

    private void updatePlayer1(final String playerName, final Game game) {

        //check if players exist, if not make them
        if (!(Storage.playerList.containsKey(playerName))) {
            ;
            Storage.playerList.put(playerName, new Player(playerName));
        }

        //get player by name
        Player player = Storage.playerList.get(playerName);

        //update player
        player.setGamesPlayed(player.getGamesPlayed() + 1);
        player.setGoalsScored(player.getGoalsScored() + game.getTeamOne().getGoals());
        player.setGoalsConceded(player.getGoalsConceded() + game.getTeamTwo().getGoals());

        Team.Status status = game.getTeamOne().getStatus();
        if (status.equals(Team.Status.WON)) {
            player.setGamesWon(player.getGamesWon() + 1);
        } else if (status.equals(Team.Status.LOST)) {
            player.setGamesLost(player.getGamesLost() + 1);
        } else if (status.equals(Team.Status.DRAW)) {
            player.setGamesDrawn(player.getGamesDrawn() + 1);
        }

    }

    private void updatePlayer2(final String playerName, final Game game) {

        //check if players exist, if not make them
        if (!(Storage.playerList.containsKey(playerName))) {
            Storage.playerList.put(playerName, new Player(playerName));
        }

        //get player by name
        Player player = Storage.playerList.get(playerName);

        //update player
        player.setGamesPlayed(player.getGamesPlayed() + 1);
        player.setGoalsScored(player.getGoalsScored() + game.getTeamTwo().getGoals());
        player.setGoalsConceded(player.getGoalsConceded() + game.getTeamOne().getGoals());

        Team.Status status = game.getTeamTwo().getStatus();
        if (status.equals(Team.Status.WON)) {
            player.setGamesWon(player.getGamesWon() + 1);
        } else if (status.equals(Team.Status.LOST)) {
            player.setGamesLost(player.getGamesLost() + 1);
        } else if (status.equals(Team.Status.DRAW)) {
            player.setGamesDrawn(player.getGamesDrawn() + 1);
        }

    }

    public void saveData() {

        Storage.saveData();

    }
}
