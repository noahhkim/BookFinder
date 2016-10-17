package com.example.android.bookfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Noah on 8/5/2016.
 */
public class Utils {

    // Tag for log messages
    public static final String LOG_TAG = ResultsActivity.class.getSimpleName();

    // Query Google books and return a {@link Book} object to represent list of books
    public static ArrayList<Book> getBooksData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        ArrayList<Book> books = null;
        try {
            books = getBooksDataFromJson(jsonResponse);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem displaying JSON results.", e);
        }
        return books;
    }

    // Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl) {
        stringUrl = stringUrl.replaceAll(" ", "%20");
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    // Make an HTTP request to the given URL and return a String as the response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return empty JSON response
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // If URL has 200 response code, proceed with reading input stream
            // and parsing JSON response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving book results.", e);
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Convert into a {@Link InputStream} String which contains whole JSON response from server
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    // Load image from image URL in JSON array
    private static Bitmap loadImage(String utl2) {
        Log.v("utl2--", utl2);
        URL imageURL = null;

        Bitmap bitmap = null;
        try {
            imageURL = new URL(utl2);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem retrieving image.", e);
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) imageURL.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Could not connect to image source.", e);
        }
        return bitmap;
    }

    private static ArrayList<Book> getBooksDataFromJson(String booksJSON) throws JSONException {

        //Create an empty ArrayList we can add books to
        ArrayList<Book> books = new ArrayList<>();

        // Build up list of Book objects from JSON
        JSONObject booksJson = new JSONObject(booksJSON);
        JSONArray itemsArray = booksJson.getJSONArray("items");
        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject currentBook = itemsArray.getJSONObject(i);
            JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

            // Extract out title values
            String title = volumeInfo.getString("title");

            // Extract out author values
            String author = "By ";
            String authorNames;
            JSONArray authors;
            try {
                authors = volumeInfo.getJSONArray("authors");
                for (int a = 0; a < authors.length(); a++) {
                    authorNames = authors.getString(a);
                    // add comma between multiple authors
                    if (authors.length() > 1) {
                        author += authorNames + ", ";
                        // return single author
                    } else {
                        author += authorNames;
                    }
                }
                // if there are multiple authors, remove comma after last author
                if (authors.length() > 1) {
                    author = author.substring(0, author.length() - 2);
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, "No author available", e);
            }

            // Extract out image
            Bitmap bitmap = null;
            try {
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                bitmap = loadImage(imageLinks.getString("thumbnail"));
            } catch (JSONException e) {
                Log.e(LOG_TAG, "No image available", e);
            }

            //Extract out preview URL
            String url = volumeInfo.getString("infoLink");


            Book book = new Book(title, author, bitmap, url);

            // Add book object to books array
            books.add(book);
        }
        // Return the list of books
        return books;
    }


}
