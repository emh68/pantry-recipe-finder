package model

data class RecipeSummary(

    // Recipe ID
    val id: Int,

    // Name/title of the recipe
    val title: String,

    // Number of ingredients user already has
    val usedIngredientCount: Int,

    // Names of ingredients that are used
    val usedIngredients: List<String>,

    // Number of additional ingredients needed
    val missedIngredientCount: Int,

    // Names of ingredients that are missing
    val missedIngredients: List<String>
)
