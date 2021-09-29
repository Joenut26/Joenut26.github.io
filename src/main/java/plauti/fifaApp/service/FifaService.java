package plauti.fifaApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import plauti.fifaApp.model.Game;
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

        Game game = new Game();
        game.setTeamOne(teamOne);
        game.setTeamTwo(teamTwo);
        game.setDate(LocalDate.now());


        //calculations
        //update player stats
        //log game
        Storage.gameMap.put(UUID.randomUUID(), game);
        //update ranking


        return game;

    }

    public void saveData() {

        Storage.saveData();

    }
}
