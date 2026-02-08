# Overview

The goal of this project was to strengthen my understanding of Kotlin by building a complete, working application that
interacts with an API. My focus was on core Kotlin concepts while writing clean, readable code and organizing
the program in a way that could be expanded later.

# Description

This software is a Kotlin console application that helps users find recipes based on the ingredients they already have.
The user enters a list of ingredients and chooses how many recipes to view. The program then calls the Spoonacular API
and displays matching recipes, including which ingredients are used and which are missing. After viewing the list, the
user can select a recipe to see detailed, step-by-step cooking instructions.

[Software Demo Video](https://youtu.be/RpnNwf92wDc)

---

# Development Environment

The software was developed using the IntelliJ IDEA IDE. I used Gradle to manage the Kotlin project structure
and dependencies. The application runs as a console program and uses environment variables to securely store the API
key. The programming language used for this project is Kotlin. The application uses:

- The Kotlin standard library
- java.net.HttpURLConnection for HTTP requests
- The org.json library for parsing JSON responses from the Spoonacular API

### Prerequisites

- Java JDK (21+ recommended, project uses Gradle toolchain)
- [Spoonacular API Key](https://spoonacular.com/food-api)

## Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/emh68/pantry-recipe-finder
   cd <repository-folder>
   ```

2. **Configure the API Key**\
   To keep your key secure, the program reads from an environment variable named `SPOONACULAR_API_KEY`.

    * **In IntelliJ IDEA (Recommended):**
        1. Click the **Run** menu -> **Edit Configurations**.
        2. Select your `Main.kt` configuration.
        3. In the **Environment variables** field, enter: `SPOONACULAR_API_KEY=your_actual_key_here`.


3. **Permissions (Mac/Linux Only)**\
   If you are on a Unix-based system, ensure the Gradle wrapper has execution permissions:
   ```bash
   chmod +x gradlew
   ```

---

## Execution

### Click run MainKt in IntelliJ or to Run via Terminal:

#### Windows

```bash
gradlew run
````

#### On Mac/Linux

```bash
./gradlew run
```

### Usage

1. Enter ingredients when prompted (e.g., chicken, garlic, onions).
2. Specify the number of recipes to return.
3. The program will output a list of matching recipes found via the Spoonacular API.
4. Enter the list number to see the recipe instructions.

# Useful Websites

- [Kotlin Official Docs (Home)](https://kotlinlang.org/docs/home.html)
- [Kotlin Official Docs (Get Started/Tour)](https://kotlinlang.org/docs/getting-started.html)
- [W3WSchools Kotlin Tutorial](https://www.w3schools.com/KOTLIN/index.php)
- [Full 2025 Kotlin Crash Course For Beginners - Philipp Lackner](https://www.youtube.com/watch?v=dzUc9vrsldM)
- [Kotlin Tutorial For Beginners - Amigoscode](https://www.youtube.com/watch?v=TEXaoSC_8lQ)

# Future Work

- Add support for dietary restrictions and food intolerances
- Allow filtering by cuisine type and meal type
- Improve ingredient matching logic
- Display recipe images for use in a future mobile application
- Improve instruction formatting and UI/UX experience

# AI Disclosure

I had prior experience with JetBrains tools but was unfamiliar with IntelliJ, Gradle, and the setup required for a
Kotlin project. I was also not aware that Kotlin does not include built-in JSON parsing support and that there are
multiple approaches to handling JSON.

I used ChatGPT and YouTube tutorials to help with the initial project setup in IntelliJ and to better understand
different JSON parsing options. I chose to use org.json for this project to keep the scope manageable and meet time
constraints rather than using kotlinx.serialization, which I may switch to in the future.

ChatGPT was used for setup assistance and for reviewing JSON parsing approaches; however, all application code was
written by me.