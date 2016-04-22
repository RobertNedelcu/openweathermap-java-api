package org.openweathermap.api;

import org.openweathermap.api.query.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UrlConnectionWeatherClient extends AbstractWeatherClient {
    private final String apiKey;

    public UrlConnectionWeatherClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected String makeRequest(Query query) {
        String queryString = query.toStringRepresentation(apiKey);
        try {
            URL url = getUrl(queryString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            return readResponse(urlConnection);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String readResponse(URLConnection urlConnection) throws IOException {
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    /**
     * Gets url from query string.
     *
     * @param query the query
     * @return the url
     */
    private URL getUrl(String query) {
        try {
            return new URL(query);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed query", e);
        }
    }

}