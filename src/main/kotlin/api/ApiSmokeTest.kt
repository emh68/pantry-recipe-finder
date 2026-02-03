package api

import java.net.HttpURLConnection
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * This file is a simple "smoke test" to verify that:
 * 1) The program can read the Spoonacular API key from an environment variable
 * 2) An HTTP request can be made successfully
 * 3) The Spoonacular API returns a valid response
 *
 * This is not the final application logic. It is used only to test connectivity
 * and inspect the raw API response before integrating parsing and logic.
 */
fun main() {

    // Read the API key from an environment variable to avoid hardcoding sensitive info.
    val apiKey = System.getenv("SPOONACULAR_API_KEY")

    // If the API key is missing, print an error and stop execution
    if (apiKey.isNullOrBlank()) {
        println("ERROR: SPOONACULAR_API_KEY environment variable is not set.")
        println("Please set it in your Run -> Edit Configuration before running the program.")
        return
    }

    // Temporary test ingredients used to verify the API call
    // Will replace later with user input
    val ingredients = "chicken,rice"

    // Limit the number of recipes returned by the API
    val number = 3

    // This ensures that whatever is in 'ingredients' is correctly encoded for the URL
    val encodedIngredients = URLEncoder.encode(ingredients, StandardCharsets.UTF_8.toString())

    // Build the request URL for the Spoonacular "findByIngredients" endpoint
    // ranking=2 prioritizes recipes with fewer missing ingredients
    // ignorePantry=true ignores common pantry items such as salt or water
    val urlString =
        "https://api.spoonacular.com/recipes/findByIngredients" +
                "?ingredients=$encodedIngredients" +
                "&number=$number" +
                "&ranking=2" +
                "&ignorePantry=true" +
                "&apiKey=$apiKey"

    // Create URL and open HTTP connection
    val url = URI.create(urlString).toURL()
    val connection = url.openConnection() as HttpURLConnection

    try {
        // Configure the HTTP request
        connection.requestMethod = "GET"
        connection.connectTimeout = 5000
        connection.readTimeout = 5000

        // Read the HTTP response code returned by the server
        val responseCode = connection.responseCode
        println("Response Code: $responseCode")

        // If the response code is not successful, print error and stop execution
        if (responseCode != 200) {
            println("ERROR: Request failed.")
            return
        }

        // Read the raw JSON response from the API
        val response = connection.inputStream.bufferedReader().readText()

        // Print part (500 characters) of the raw response for inspection and debugging
        // This confirms the structure of the data before JSON parsing is added
        println("Raw API response (first 500 chars):")
        println(response.take(500))

    } catch (e: Exception) {
        // Catch and display any unexpected errors during the request
        println("ERROR: ${e.message}")
    } finally {
        // Always close the connection to free resources
        connection.disconnect()
    }
}