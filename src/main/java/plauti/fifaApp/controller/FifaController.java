package plauti.fifaApp.controller;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import plauti.fifaApp.controller.dto.TeamDto;
import plauti.fifaApp.model.Game;
import plauti.fifaApp.model.Team;
import plauti.fifaApp.service.FifaService;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping(path = "/fifaPlauti")
public class FifaController {



    @Autowired
    private final FifaService fifaService;

    @PostMapping("/gameForm")
    public ResponseEntity<Game> recordGame(@RequestBody TeamDto teams){

        log.info("setGame: {}", teams);
        return ResponseEntity.ok(fifaService.setGame(teams.getTeamOne(), teams.getTeamTwo()));
    }
}
