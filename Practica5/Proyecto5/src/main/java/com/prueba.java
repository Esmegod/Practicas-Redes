package com;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class prueba {
    public static void main(String[] args) throws IOException {
        ArrayList<String> frutas = new ArrayList<>();
        frutas.add("sandia");
        frutas.add("melon");
        frutas.add("guayaba");

         for (String link : findLinks("https://www.escom.ipn.mx/")) {
            System.out.println(link);
        }
        

    }
    private static Set<String> findLinks(String url) throws java.io.IOException {

        Set<String> links = new HashSet<>();

        Document doc = Jsoup.connect(url)
                .timeout(3000)
                .get();

        Elements elements = doc.select("a[href]");
        Elements elements2 = doc.select("img[src]");
        for (Element element : elements) {
            links.add(element.absUrl("href"));
        }
         for (Element element : elements2) {
            links.add(element.absUrl("src"));
        }
        return links; 
    }
}
