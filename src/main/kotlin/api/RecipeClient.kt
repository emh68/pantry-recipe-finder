package api

import java.net.HttpURLConnection
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.json.JSONArray
import model.RecipeSummary

class RecipeClient(private val apiKey: String) {
    fun findRecipesByIngredients(ingredients: String, number: Int): List<RecipeSummary> {
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

            // If the response code is not successful, print error and stop execution
            if (responseCode != 200) {
                println("ERROR: Request failed with code $responseCode")
                val errorText = connection.errorStream?.bufferedReader()?.readText()
                if (!errorText.isNullOrEmpty()) {
                    println(errorText)
                }
                return emptyList()
            }

            // Read the raw JSON response from the API
            val response = connection.inputStream.bufferedReader().readText()
            return parseRecipeJson(response)
        } catch (e: Exception) {
            println("ERROR: ${e.message}")
            return emptyList()
        } finally {
            connection.disconnect()
        }
    }

    private fun parseRecipeJson(response: String): List<RecipeSummary> {
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
        return recipeList
    }
}