package plauti.fifaApp.Scraper;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@Configuration
public class Scraper {

    public Scraper() {

        String url = "https://www.fifaindex.com/teams/?name=port&order=desc";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements ratings = doc.select("[data-title=\"OVR\"]");
            Elements name = doc.select("[data-title=\"Name\"]");
            Elements teamName = doc.select("td");

            String[] splitRating = ratings.text().split(" ");
            ArrayList<String> teamList = new ArrayList<>();
            name.forEach(team -> teamList.add(team.text()) );
            ArrayList<String> rateList = new ArrayList<>(Arrays.asList(splitRating));


            HashMap<Integer, TeamObject> result = new HashMap<>();
            TeamObject teamObject = new TeamObject();

            if(teamList.size() == rateList.size()){
                for (int i = 0; i < teamList.size(); i++) {
                    //result.put(teamList.get(i), rateList.get(i));
                    teamObject.setTeamRating(rateList.get(i));
                    teamObject.setTeamName(teamList.get(i));

                    result.put(i, teamObject);
                    System.out.println(teamObject);
                    //System.out.println("Team: " + teamList.get(i) + ", Rating: " + rateList.get(i));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Data
    public class TeamObject {
        public String teamName;
        public String teamRating;

    }

}
