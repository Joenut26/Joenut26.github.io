package plauti.fifaApp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import plauti.fifaApp.model.Game;
import plauti.fifaApp.model.Team;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Service
public class FifaService {

    public Game setGame(Team teamOne, Team teamTwo){

        Game game = new Game();
        game.setTeamOne(teamOne);
        game.setTeamTwo(teamTwo);
        game.setDate(LocalDate.now());

        //update player stats
        //log game
        //update ranking


        return game;

    }
}
