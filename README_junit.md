# JUnit 4 Setup and Environment for VS Code (Command Line)

This guide explains how to set up your entire Java and JUnit 4 environment for this project using only the command line, so you can run and develop tests in VS Code.

## 1. Install Java (if not already installed)

You need Java JDK 8 or later. To check:

```
java -version
```

If not installed, download and install from [Adoptium](https://adoptium.net/) or use Homebrew (macOS):

```
brew install --cask temurin
```

## 2. Set JAVA_HOME (macOS/Linux)

Find your JDK path (example for Temurin 21):

```
export JAVA_HOME="/Library/Java/JavaVirtualMachines/temurin-21.jdk/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"
```

Add these lines to your `~/.bash_profile` or `~/.zshrc` for persistence.

## 3. Download JUnit 4 and Hamcrest

From the project root:

```
mkdir -p lib
curl -L -o lib/junit-4.13.2.jar https://search.maven.org/remotecontent?filepath=junit/junit/4.13.2/junit-4.13.2.jar
curl -L -o lib/hamcrest-core-1.3.jar https://search.maven.org/remotecontent?filepath=org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
```

## 4. VS Code Java Extension Pack

Install the Java Extension Pack in VS Code for best experience:

- Open VS Code
- Go to Extensions (Ctrl+Shift+X)
- Search for "Java Extension Pack" and install

## 5. VS Code Java Project Settings (Command Line)

Ensure `.vscode/settings.json` contains:

```
{
  "java.project.referencedLibraries": [
    "lib/junit-4.13.2.jar",
    "lib/hamcrest-core-1.3.jar"
  ],
  "java.project.sourcePaths": ["src", "src/test/java"]
}
```

If not, create or edit `.vscode/settings.json` with the above content.

## 6. Compile and Run JUnit Tests (Command Line)

```
# Compile source and test files
mkdir -p out/test
javac -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:src" -d out/test src/model/*.java src/test/java/model/*.java

# Run a test class (example: CarTest)
java -cp "out/test:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:src" org.junit.runner.JUnitCore model.CarTest
```

## 7. Troubleshooting

- If VS Code does not recognize JUnit classes, reload the window or restart VS Code.
- Make sure your test files are in `src/test/java/model/` and use the correct package declaration (e.g., `package model;`).
- If you change the directory structure, update the paths in `.vscode/settings.json` and the compile/run commands accordingly.

---

You can now develop, compile, and run JUnit 4 tests in VS Code using only the command line for setup.
