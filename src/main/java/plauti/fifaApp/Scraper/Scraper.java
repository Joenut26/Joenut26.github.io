package plauti.fifaApp.Scraper;

import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Configuration
public class Scraper {

    public Scraper() {
        try {
            URL url = new URL("https://www.fifaindex.com/teams/?name=port&order=desc");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);


            int status = connection.getResponseCode();
            System.out.println(status);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.contains("OVR")) {

                    String[] split = inputLine.split(" title=");
                    String[] split1 = inputLine.split("</span></td><td data-title=\"Team Rating\">");
                    if (split.length > 1) {
                        String team = split[1].split(" FIFA 21")[0];
                        //content.append(inputLine);
                        String a = split1[0];
                        String overall = a.substring(a.length()-2);

                        content.append("Team: " + team + ", Rating: " + overall + ". ");
                    }

                }

            }
            in.close();

            connection.disconnect();

            System.out.println(content);
            System.out.println(ResponseBuilder.getFullResponse(connection));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
