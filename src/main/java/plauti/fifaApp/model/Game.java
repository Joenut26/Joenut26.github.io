package plauti.fifaApp.model;

import lombok.Data;

import java.util.Date;

@Data
public class Game {

    private Team teamOne;
    private Team teamTwo;
    private Date date;

}
