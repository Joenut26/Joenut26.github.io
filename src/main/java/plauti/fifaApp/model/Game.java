package plauti.fifaApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;


@Data
public class Game {

    public Game() {

    }

    private Team teamOne;
    private Team teamTwo;
    private LocalDate date;


}
