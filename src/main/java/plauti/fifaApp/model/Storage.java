package plauti.fifaApp.model;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import plauti.fifaApp.Scraper.Scraper;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
@Data
public class Storage {

    //set true to update teams
    private final boolean UPDATE = false;

    private final ArrayList<String[]> GAME_DATA = new ArrayList<>();
    private final ArrayList<String[]> PLAYER_DATA = new ArrayList<>();
    private final ArrayList<String[]> TEAM_DATABASE = new ArrayList<>();
    public static HashMap<UUID, Game> gameMap = new HashMap<>();
    public static ArrayList<Player> playerList = new ArrayList<>();

    public Storage() {

        System.out.println("Preparing storage");
        try {
            GAME_DATA.addAll(readCSV("src/main/resources/static/storage/GAMESTORAGE.csv"));
            PLAYER_DATA.addAll(readCSV("src/main/resources/static/storage/PLAYER_DATA.csv"));
            TEAM_DATABASE.addAll(readCSV("src/main/resources/static/storage/TEAM_DATABASE.csv"));

            for (String[] line : GAME_DATA) {

                Game game = new Game();

                game.setDate(LocalDate.parse(line[0]));
                game.setTeamOne(new Team(Integer.parseInt(line[7]), line[3], Integer.parseInt(line[5]), line[1]));
                game.setTeamTwo(new Team(Integer.parseInt(line[8]), line[4], Integer.parseInt(line[6]), line[2]));
                UUID gameId = UUID.randomUUID();

                gameMap.put(gameId, game);

                System.out.println("Getting game data...");
                System.out.println(Arrays.toString(line));

            }

            for (String[] line : PLAYER_DATA) {

                Player player = new Player();

                player.setName(line[0]);
                player.setElo(Integer.parseInt(line[1]));
                player.setGamesPlayed(Integer.parseInt(line[2]));
                player.setPoints(Integer.parseInt(line[3]));
                player.setGoalsScored(Integer.parseInt(line[4]));
                player.setGoalsConceded(Integer.parseInt(line[5]));
                player.setGamesWon(Integer.parseInt(line[6]));
                player.setGamesLost(Integer.parseInt(line[7]));
                player.setGamesDrawn(Integer.parseInt(line[8]));

                playerList.add(player);
                System.out.println("Getting player data...");
                System.out.println(Arrays.toString(line));

            }

            System.out.println("Storage initialization complete");

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (UPDATE) {
            getTeamData();
        }
    }

    public static void saveData() {

        ArrayList<String[]> data = new ArrayList<>();
        gameMap.values().forEach(game -> {
            data.add(new String[]{
                    game.getDate().toString(),
                    game.getTeamOne().getPlayer(),
                    game.getTeamTwo().getPlayer(),
                    game.getTeamOne().getName(),
                    game.getTeamTwo().getName(),
                    game.getTeamOne().getGoals().toString(),
                    game.getTeamTwo().getGoals().toString(),
                    game.getTeamOne().getRank().toString(),
                    game.getTeamTwo().getRank().toString()}); //new String[]{teamObject.getTeamName(), teamObject.getTeamRating()}));
        });

        writeToCSV("src/main/resources/static/storage/GAMESTORAGE.csv", data);
        //clear data
        data.clear();
        playerList.forEach(player -> {
            data.add(new String[]{
                    player.getName(),
                    player.getElo().toString(),
                    player.getGamesPlayed().toString(),
                    player.getPoints().toString(),
                    player.getGoalsScored().toString(),
                    player.getGoalsConceded().toString(),
                    player.getGamesWon().toString(),
                    player.getGamesLost().toString(),
                    player.getGamesDrawn().toString()
            });
        });

        writeToCSV("src/main/resources/static/storage/PLAYER_DATA.csv", data);
        data.clear();

    }

    private void getTeamData() {
        String clubUrl = "https://www.fifaindex.com/teams/fifa21_486/?page=";
        String nationalUrl = "https://www.fifaindex.com/teams/fifa21_486/?type=1&page=";

        ArrayList<Scraper.TeamObject> allTeams = new ArrayList<>();

        for (int i = 1; i < 24; i++) {

            try {
                ArrayList<Scraper.TeamObject> teamsPerPage = new ArrayList<>();
                String pageUrl = clubUrl + i;
                Scraper scraper = new Scraper();
                teamsPerPage = scraper.scrapeBySearch(pageUrl);
                System.out.println("getting data on page " + pageUrl);
                allTeams.addAll(teamsPerPage);
            } catch (Exception e) {
                System.out.println("Page does not exist");
                System.out.println(e.getMessage());
            }
        }

        for (int i = 1; i < 3; i++) {

            try {
                ArrayList<Scraper.TeamObject> teamsPerPage = new ArrayList<>();
                String pageUrl = nationalUrl + i;
                Scraper scraper = new Scraper();
                teamsPerPage = scraper.scrapeBySearch(pageUrl);
                System.out.println("getting data on page " + pageUrl);
                allTeams.addAll(teamsPerPage);
            } catch (Exception e) {
                System.out.println("Page does not exist");
                System.out.println(e.getMessage());
            }
        }

        ArrayList<String[]> dataLines = new ArrayList<>();
        allTeams.forEach(teamObject -> dataLines.add(new String[]{teamObject.getTeamName(), teamObject.getTeamRating()}));

        writeToCSV("src/main/resources/static/storage/TEAM_DATABASE.csv", dataLines);

    }

    public static void writeToCSV(final String fileName, ArrayList<String[]> data) {

        try (PrintWriter pw = new PrintWriter(fileName)) {
            pw.print("");
            data.stream()
                    .map(Storage::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        }
    }

    public static String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(Storage::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return database;
    }

}
