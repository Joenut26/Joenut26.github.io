package plauti.fifaApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import plauti.fifaApp.model.Game;
import plauti.fifaApp.model.Player;
import plauti.fifaApp.model.Storage;
import plauti.fifaApp.model.Team;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
public class FifaService {

    Storage storage = new Storage();
    private final Integer K_VALUE = 32;

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

        //check if players exist already
        checkForPlayers(game);

        //update player stats
        //calc ELO player 1
        updatePlayer(game, false);
        //calc ELO player 2
        updatePlayer(game, true);

        //log game
        Storage.gameMap.put(UUID.randomUUID(), game);
        //update ranking

        saveData();

        return game;

    }

    private void checkForPlayers(final Game game) {

        String player1Name = game.getTeamOne().getPlayer();
        String player2Name = game.getTeamTwo().getPlayer();

        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.add(player1Name);
        playerNames.add(player2Name);

        //check if players exist, if not make them
        playerNames.forEach(player -> {
            if (!(Storage.playerList.containsKey(player))) {
                ;
                Storage.playerList.put(player, new Player(player));
            }
        });

    }

    private void updatePlayer(final Game game, final boolean switched) {

        Team playerTeam;
        Team opponentTeam;
        Player player;
        Player opponent;

        if (!switched) {
            playerTeam = game.getTeamOne();
            opponentTeam = game.getTeamTwo();

            player = Storage.playerList.get(game.getTeamOne().getPlayer());
            opponent = Storage.playerList.get(game.getTeamTwo().getPlayer());
        } else {
            playerTeam = game.getTeamTwo();
            opponentTeam = game.getTeamOne();

            player = Storage.playerList.get(game.getTeamTwo().getPlayer());
            opponent = Storage.playerList.get(game.getTeamOne().getPlayer());
        }

        //calculate expected score
        double deltaE = 100 * ((double) opponent.getElo() / (double) playerTeam.getRank() - (double) player.getElo() / (double) opponentTeam.getRank());
        System.out.println("deltaE: " + deltaE);
        double expectedScore = 1 / (1 + Math.exp(deltaE / 500));
        System.out.println("expected: " + expectedScore);
        //calculate actual score
        double deltaA = 100 * ((double) opponentTeam.getGoals() / (double) opponentTeam.getRank() - (double) playerTeam.getGoals() / (double) playerTeam.getRank());
        System.out.println("deltaA: " + deltaA);
        double actualScore = 1 / (1 + Math.exp(deltaA / 5));
        System.out.println("actual: " + actualScore);


        //update ELO
        System.out.println(actualScore - expectedScore);
        player.setElo(player.getElo() + (int) Math.round(K_VALUE * (actualScore - expectedScore)));

        player.setGamesPlayed(player.getGamesPlayed() + 1);
        player.setGoalsScored(player.getGoalsScored() + playerTeam.getGoals());
        player.setGoalsConceded(player.getGoalsConceded() + opponentTeam.getGoals());

        Team.Status status = playerTeam.getStatus();
        if (status.equals(Team.Status.WON)) {
            player.setGamesWon(player.getGamesWon() + 1);
            player.setPoints(player.getPoints() + 3);
        } else if (status.equals(Team.Status.LOST)) {
            player.setGamesLost(player.getGamesLost() + 1);
        } else if (status.equals(Team.Status.DRAW)) {
            player.setGamesDrawn(player.getGamesDrawn() + 1);
            player.setPoints(player.getPoints() + 1);
        }

    }

    public void saveData() {
        Storage.saveData();
    }
}
