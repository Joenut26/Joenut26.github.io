package plauti.fifaApp.model;

import lombok.Data;

@Data
public class Player {

    private String name;
    private Integer elo = 1000;
    private Integer gamesPlayed = 0;
    private Integer points = 0;
    private Integer goalsScored = 0;
    private Integer goalsConceded = 0;
    private Integer gamesWon = 0;
    private Integer gamesLost = 0;
    private Integer gamesDrawn = 0;

    public Player() {

    }

    public Player(final String name) {
        this.name = name;

    }


}
