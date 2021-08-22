package plauti.fifaApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Team {

    public Team(Integer rank, String name, Integer goals, String player) {
        this.rank = rank;
        this.name = name;
        this.goals = goals;
        this.player = player;
    }

    private Integer rank;
    private String name;
    private Integer goals;
    private String player;

    public Team() {

    }
}
