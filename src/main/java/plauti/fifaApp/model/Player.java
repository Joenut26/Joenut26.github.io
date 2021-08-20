package plauti.fifaApp.model;

import lombok.Data;

@Data
public class Player {

    private String name;
    private Integer elo;
    private Integer gamesPlayed;
    private Integer points;
    private Integer goalsScored;
    private Integer goalsConceded;
    private Integer gamesWon;
    private Integer gamesLost;

}
