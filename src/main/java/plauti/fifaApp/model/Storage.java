package plauti.fifaApp.model;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import plauti.fifaApp.Scraper.Scraper;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration
@Data
public class Storage {

    //set true to update teams
    private final boolean UPDATE = false;

    private ArrayList<String[]> GAME_DATA = new ArrayList<>();
    private ArrayList<String[]> PLAYER_DATA = new ArrayList<>();
    private ArrayList<String[]> TEAM_DATABASE = new ArrayList<>();
    public static HashMap<UUID, Game> GAME_MAP = new HashMap<>();

    public Storage() {

        System.out.println("Preparing storage");
        try {
            GAME_DATA = readCSV("src/main/resources/static/storage/GAMESTORAGE.csv");
            PLAYER_DATA = readCSV("src/main/resources/static/storage/PLAYER_DATA.csv");
            TEAM_DATABASE = readCSV("src/main/resources/static/storage/TEAM_DATABASE.csv");

            for (String[] line : GAME_DATA) {

                Game game = new Game();

                game.setDate(LocalDate.parse(line[0]));
                game.setTeamOne(new Team(Integer.parseInt(line[7]), line[3], Integer.parseInt(line[5]), line[1]));
                game.setTeamTwo(new Team(Integer.parseInt(line[8]), line[4], Integer.parseInt(line[6]), line[2]));
                UUID gameId = UUID.randomUUID();

                GAME_MAP.put(gameId, game);
            }

            System.out.println("Storage initialization complete");

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (UPDATE) {
            getTeamData();
        }
    }

    private void getTeamData() {
        String clubUrl = "https://www.fifaindex.com/teams/fifa21_486/?page=";               //"https://www.fifaindex.com/fifa21_486/?page=1";
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

        try (PrintWriter pw = new PrintWriter("src/main/resources/static/storage/TEAM_DATABASE.csv")) {
            //reset file
            pw.print("");
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        }
    }

    public void writeToCSV(final String fileName, ArrayList<String[]> data){

        try (PrintWriter pw = new PrintWriter(fileName)) {
            data.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
        }
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    public String escapeSpecialCharacters(String data) {
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
            for (String[] line : GAME_DATA) {
                System.out.println(Arrays.toString(line));
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return database;
    }

}
