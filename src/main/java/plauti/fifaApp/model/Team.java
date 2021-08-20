package plauti.fifaApp.model;

import lombok.Data;

@Data
public class Team {

    private Integer rank;
    private String name;
    private Integer goals;
    private Player player;

}
