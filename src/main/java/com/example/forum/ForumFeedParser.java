package com.example.forum;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Simple utility that fetches the public Atom feed of the forum and stores each
 * entry as a line in {@code forum_dump.txt}.
 */
public class ForumFeedParser {
    private static final String FEED_URL = "http://bboard.negonki.ru/feed.php?mode=topics_active";

    public static void main(String[] args) throws IOException {
        Document doc;
        try {
            doc = Jsoup.connect(FEED_URL)
                    .header("Accept", "application/atom+xml")
                    .userAgent("Mozilla/5.0")
                    .timeout(10_000)
                    .parser(Parser.xmlParser())
                    .get();
        } catch (IOException ex) {
            // If network is unavailable, fall back to a local file named 'feed.xml'.
            doc = Jsoup.parse(Files.readString(Path.of("feed.xml")), "", Parser.xmlParser());
        }

        Path out = Path.of("forum_dump.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
            for (Element entry : doc.select("entry")) {
                String date = entry.selectFirst("updated").text();
                String user = entry.selectFirst("author > name").text();
                String topic = entry.selectFirst("title").text();
                String contentHtml = entry.selectFirst("content").text();
                String text = Jsoup.parse(contentHtml).text().replaceAll("\n", " ");
                writer.write(String.join(", ", date, user, topic, text));
                writer.newLine();
            }
        }
    }
}
