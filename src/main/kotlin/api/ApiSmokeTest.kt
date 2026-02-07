package api

/**
 * This file is a simple "smoke test" to verify that:
 * 1) The program can read the Spoonacular API key from an environment variable
 * 2) An HTTP request can be made successfully
 * 3) The Spoonacular API returns a valid response
 *
 * This is not the final application logic. It is just a quick way to verify that RecipeClient works.
 */
fun main() {

    // Read the API key from an environment variable to avoid hardcoding sensitive info.
    val apiKey = System.getenv("SPOONACULAR_API_KEY")

    // If the API key is missing, print an error and stop execution
    if (apiKey.isNullOrBlank()) {
        println("ERROR: SPOONACULAR_API_KEY environment variable is not set.")
        return
    }

    val client = RecipeClient(apiKey)
    val recipes = client.findRecipesByIngredients("chicken, rice", 3)

    if (recipes.isEmpty()) {
        println("ERROR: No recipes found for those ingredients.")
    } else {
        for ((index, recipe) in recipes.withIndex()) {
            println("${index + 1}. ${recipe.title}")
            println("   Recipe ID#: ${recipe.id}")
            println("   Used: ${recipe.usedIngredientCount}")
            println("   Missing: ${recipe.missedIngredientCount}")
            println("   Missing ingredients: ${recipe.missedIngredients.joinToString(", ")}")
            println()
        }
    }
}
