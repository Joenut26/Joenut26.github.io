package plauti.fifaApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Team {

    public enum Status {
        WON,
        LOST,
        DRAW,
        UNDEFINED
    }

    public Team(Integer rank, String name, Integer goals, String player) {
        this.rank = rank;
        this.name = name;
        this.goals = goals;
        this.player = player;
        this.status = Status.UNDEFINED;
    }

    private Integer rank;
    private String name;
    private Integer goals;
    private String player;
    private Status status;


    public Team() {

    }
}
