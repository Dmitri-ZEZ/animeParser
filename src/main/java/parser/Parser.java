package parser;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    public static void main(String[] args) throws IOException, InterruptedException {
        Document doc = null;
        final String url = "https://animego.org/anime?sort=a.title&direction=desc&type=animes&page=";

        List<Anime> animeList = new ArrayList<>();

        for (int numberPage = 1; numberPage < 112; numberPage++) {
            Thread.sleep(1000);
            System.out.println(numberPage);
            try {
                doc = Jsoup.connect(url.concat(Integer.toString(numberPage))).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc != null) {
                Elements animeCards = doc
                        .selectFirst("div#anime-list-container")
                        .select("div[class^=animes-list-item media]");


                Elements titles = animeCards.select("div[class^=h5 font-weight-normal mb-1]")
                        .select("a");

                Elements originalTitles = animeCards
                        .select("div[class^=text-gray-dark-6 small mb-2]");

                Elements posters = animeCards
                        .select("div[class^=animes-list-item-picture media-left mr-3 position-relative]")
                        .select("div[class^=anime-list-lazy lazy]");

                Elements descriptions = animeCards
                        .select("div[class^=description d-none d-sm-block]");

                Elements years = animeCards
                        .select("span[class^=anime-year mb-2]");

                Elements genres = animeCards
                        .select("span[class^=anime-genre d-none d-sm-inline]");


                for (int i = 0; i < animeCards.size(); i++) {
                    try {
                        Anime anime = new Anime();
                        anime.setTitle(titles.get(i).text());
                        anime.setOriginalTitle(originalTitles.get(i).text());
                        anime.setUrl(titles.get(i).attr("href"));
                        anime.setPoster(posters.get(i).attr("data-original"));
                        anime.setDescription(descriptions.get(i).text());
                        anime.setYear(years.get(i).text());
                        anime.setGenre(Arrays.asList(genres.get(i).text().split(", ").clone()));

                        animeList.add(anime);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        }

        Gson gson = new Gson();
        String s = gson.toJson(animeList);

        File file = new File("src/main/resources/anime.json");
        FileWriter writer = new FileWriter(file);
        writer.write(s);
        writer.flush();
        writer.close();
    }
}
