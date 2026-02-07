import api.RecipeClient


fun main() {
    // Read the API key from an environment variable to avoid hardcoding sensitive info.
    val apiKey = System.getenv("SPOONACULAR_API_KEY")

    // If the API key is missing, print an error and stop execution
    if (apiKey.isNullOrBlank()) {
        println("ERROR: SPOONACULAR_API_KEY environment variable is not set.")
        println("Please set it in your Run -> Edit Configuration before running the program.")
        return
    }

    val client = RecipeClient(apiKey)

    println("Enter ingredients (separated by commas): ")
    val ingredientsInput = readln()
    val ingredients = ingredientsInput
        .split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .joinToString(",")

    // If no ingredients were entered, stop and don't make an API call
    if (ingredients.isBlank()) {
        println("Please enter at least one ingredient.")
        return
    }

    println("How many recipes would you like to see? (e.g. 3): ")
    val number = readln().toIntOrNull() ?: 3

    // Call the API and get recipe matches
    val recipes = client.findRecipesByIngredients(ingredients, number)

    if (recipes.isEmpty()) {
        println("No recipes found. Check your ingredients or API key.")
    } else {
        for ((index, recipe) in recipes.withIndex()) {
            println("${index + 1}. ${recipe.title}")
            println("   Recipe ID#: ${recipe.id}")
            println("   Used: ${recipe.usedIngredientCount}")
            println("   Used ingredients: ${recipe.usedIngredients.joinToString(", ")}")
            println("   Missing: ${recipe.missedIngredientCount}")
            println("   Missing ingredients: ${recipe.missedIngredients.joinToString(", ")}")
            println()
        }
    }
}