package plauti.fifaApp.Scraper;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


@Data
public class Scraper {

   //private final String URL = "https://www.fifaindex.com/fifa21_486/?page=";
    //private final String URL_END = "&order=desc";

    public ArrayList<TeamObject> scrapeBySearch(final String url) {

        ArrayList<TeamObject> result = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(url).get();
            Elements ratings = doc.select("[data-title=\"OVR\"]");
            Elements name = doc.select("[data-title=\"Name\"]");

            String[] splitRating = ratings.text().split(" ");
            ArrayList<String> teamList = new ArrayList<>();
            name.forEach(team -> teamList.add(team.text()) );
            ArrayList<String> rateList = new ArrayList<>(Arrays.asList(splitRating));



            if(teamList.size() == rateList.size()){
                for (int i = 0; i < teamList.size(); i++) {
                    TeamObject teamObject = new TeamObject();
                    //result.put(teamList.get(i), rateList.get(i));
                    teamObject.setTeamRating(rateList.get(i));
                    teamObject.setTeamName(teamList.get(i));
                    result.add(teamObject);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    @Data
    public class TeamObject {
        public String teamName;
        public String teamRating;

    }

}
