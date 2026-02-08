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
        return
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

    // Prompt user to pick recipe by list number to see recipe instructions
    println("Enter a recipe number to see recipe instructions (or press Enter to quit): ")
    val choiceInput = readln()

    if (choiceInput.isBlank()) {
        return
    }

    val choice = choiceInput.toIntOrNull()
    if (choice == null || choice < 1 || choice > recipes.size) {
        println("Please enter a valid choice")
        return
    }

    val selectedRecipe = recipes[choice - 1]
    // Use selected recipe's ID to get instructions
    val steps = client.getRecipeInstructions(selectedRecipe.id)

    if (steps.isEmpty()) {
        println("No instructions were found for this recipe.")
    } else {
        println("\n--- Instructions for ${selectedRecipe.title} ---")
        for ((i, step) in steps.withIndex()) {
            println("${i + 1}. $step")
        }
    }
}