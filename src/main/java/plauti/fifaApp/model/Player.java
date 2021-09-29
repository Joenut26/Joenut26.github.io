package plauti.fifaApp.model;

import lombok.AllArgsConstructor;
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
    private Integer gamesDrawn;

    public Player(){

    }
    public Player(final String name){
        this.name = name;
    }





}
