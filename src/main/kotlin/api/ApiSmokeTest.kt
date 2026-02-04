package api

import java.net.HttpURLConnection
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.json.JSONArray
import model.RecipeSummary

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

    // Encode ingredients so URL doesn't break with spaces/special characters
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

        // Parse JSON array from API response
        val recipes = JSONArray(response)
        // List to hold RecipeSummary objects built from the JSON
        val recipeList = mutableListOf<RecipeSummary>()

        // Loop through each recipe object in the JSON array
        for (i in 0 until recipes.length()) {
            val recipeObj = recipes.getJSONObject(i)

            // Extract just the fields that are needed
            val title = recipeObj.getString("title")
            val usedIngredientCount = recipeObj.getInt("usedIngredientCount")
            val missedIngredientCount = recipeObj.getInt("missedIngredientCount")
            val missedIngredientsJson = recipeObj.getJSONArray("missedIngredients")
            val missedIngredients = mutableListOf<String>()

            for (j in 0 until missedIngredientsJson.length()) {
                val ingredientObj = missedIngredientsJson.getJSONObject(j)
                missedIngredients.add(ingredientObj.getString("name"))
            }

            val usedIngredientsJson = recipeObj.getJSONArray("usedIngredients")
            val usedIngredients = mutableListOf<String>()

            for (j in 0 until usedIngredientsJson.length()) {
                usedIngredients.add(usedIngredientsJson.getJSONObject(j).getString("name"))
            }

            // Convert the JSON recipe into a data class
            recipeList.add(
                RecipeSummary(
                    id = recipeObj.getInt("id"),
                    title = title,
                    usedIngredientCount = usedIngredientCount,
                    usedIngredients = usedIngredients,
                    missedIngredientCount = missedIngredientCount,
                    missedIngredients = missedIngredients
                )
            )
        }

        // Display user-friendly recipe list
        if (recipeList.isEmpty()) {
            println("No recipes found for those ingredients.")
        } else {
            for ((index, recipe) in recipeList.withIndex()) {
                println("${index + 1}. ${recipe.title}")
                println("   Recipe ID#: ${recipe.id}")
                println("   Used: ${recipe.usedIngredientCount}")
                println("   Missing: ${recipe.missedIngredientCount}")
                println("   Missing ingredients: ${recipe.missedIngredients.joinToString(", ")}")
                println()
            }
        }

    } catch (e: Exception) {
        // Catch and display any unexpected errors during the request
        println("ERROR: ${e.message}")
    } finally {
        // Always close the connection to free resources
        connection.disconnect()
    }
}