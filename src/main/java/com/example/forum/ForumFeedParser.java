package com.example.forum;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * Crawls the public forum and stores extracted posts in {@code forum_dump.txt}.
 *
 * <p>The crawler starts from the index page, walks through all visible forums
 * and for each forum fetches every topic, following pagination within each
 * topic. For every post the date, author, topic title and plain text are
 * written to the output file. The parser mimics a regular browser by sending a
 * modern User-Agent string which allows it to read pages that would otherwise
 * redirect to the login form.</p>
 */
public class ForumFeedParser {
    private static final String ROOT_URL =
            System.getProperty("forum.root", "http://bboard.negonki.ru");
    private static final String INDEX_URL = ROOT_URL + "/index.php";
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
            "(KHTML, like Gecko) Chrome/115.0 Safari/537.36";

    private static final Set<String> VISITED_FORUMS = new HashSet<>();

    public static void main(String[] args) throws IOException {
        Document index = Jsoup.connect(INDEX_URL)
                .userAgent(USER_AGENT)
                .timeout(10_000)
                .get();

        Path out = Path.of("forum_dump.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
            for (Element forumLink : index.select("a.forumlink")) {
                parseForum(forumLink.absUrl("href"), writer);
            }
        }
    }

    private static void parseForum(String forumUrl, BufferedWriter writer) throws IOException {
        if (!VISITED_FORUMS.add(forumUrl)) {
            return; // avoid revisiting the same forum
        }

        Document forum = Jsoup.connect(forumUrl)
                .userAgent(USER_AGENT)
                .timeout(10_000)
                .get();

        // Recursively follow subforum links
        for (Element subforum : forum.select("a.forumlink")) {
            String href = subforum.absUrl("href");
            if (!href.equals(forumUrl)) {
                parseForum(href, writer);
            }
        }

        for (Element topic : forum.select("a.topictitle")) {
            parseTopic(topic.absUrl("href"), writer);
        }
    }

    private static void parseTopic(String topicUrl, BufferedWriter writer) throws IOException {
        String pageUrl = topicUrl;
        while (pageUrl != null) {
            Document doc = Jsoup.connect(pageUrl)
                    .userAgent(USER_AGENT)
                    .timeout(10_000)
                    .get();

            Element titleEl = doc.selectFirst("h2 a.titles");
            String topicTitle = titleEl != null ? titleEl.text() : "";

            for (Element headerRow : doc.select("tr:has(b.postauthor)")) {
                String author = headerRow.selectFirst("b.postauthor").text();
                String date = headerRow.select("div:matchesOwn(Добавлено:)")
                        .text().replace("Добавлено:", "").trim();
                Element contentRow = headerRow.nextElementSibling();
                if (contentRow == null) {
                    continue;
                }
                Element body = contentRow.selectFirst("div.postbody");
                if (body == null) {
                    continue;
                }
                String text = body.text().replaceAll("\\s+", " ").trim();
                writer.write(String.join(" | ", date, author, topicTitle, text));
                writer.newLine();
            }

            Element nextPage = doc.selectFirst("a:matchesOwn(^След\\.?$)");
            pageUrl = nextPage != null ? nextPage.absUrl("href") : null;
        }
    }
}

