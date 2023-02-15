package parser;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Anime {
    private String title;

    private String originalTitle;

    private List<String> genre = new ArrayList<>();

    private String studio;

    private String poster;

    private String description;

    private String year;

    private String url;
}
