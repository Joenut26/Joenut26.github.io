package plauti.fifaApp.model;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;


@Configuration
public class Storage {

    private ArrayList<String[]> GAME_DATA = new ArrayList<>();
    private ArrayList<String[]> PLAYER_DATA = new ArrayList<>();
    public static HashMap<UUID, Game> GAME_MAP = new HashMap<>();

    public Storage() {
        System.out.println("Preparing storage");
        try {
            GAME_DATA = readCSV("src/main/resources/static/storage/GAMESTORAGE.csv");
            PLAYER_DATA = readCSV("src/main/resources/static/storage/PLAYER_DATA.csv");

            for (String[] line: GAME_DATA) {

                Game game = new Game();

                game.setDate(LocalDate.parse(line[0]));
                game.setTeamOne(new Team(Integer.parseInt(line[7]),line[3],Integer.parseInt(line[5]),line[1]));
                game.setTeamTwo(new Team(Integer.parseInt(line[8]),line[4],Integer.parseInt(line[6]),line[2]));
                UUID gameId = UUID.randomUUID();

                GAME_MAP.put(gameId, game);
            }

            System.out.println("Storage initialization complete");
            GAME_MAP.forEach((key, value) -> System.out.println(key + " " + value));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private ArrayList<String[]> readCSV(String path) throws IOException {
        ArrayList<String[]> database = new ArrayList<>();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(path));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                database.add(data);

            }
            for (String[] line : GAME_DATA) {
                System.out.println(Arrays.toString(line));
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return  database;
    }

}
