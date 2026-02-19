# JUnit 4 Setup and Usage (Command Line)

This project uses JUnit 4 for unit testing. Follow these steps to set up and run tests from the command line.

## 1. Download Dependencies

Ensure the following files exist in the `lib/` directory:
- `junit-4.13.2.jar`
- `hamcrest-core-1.3.jar`

If not, download them:

```
# Download JUnit 4
curl -L -o lib/junit-4.13.2.jar https://search.maven.org/remotecontent?filepath=junit/junit/4.13.2/junit-4.13.2.jar
# Download Hamcrest
curl -L -o lib/hamcrest-core-1.3.jar https://search.maven.org/remotecontent?filepath=org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar
```

## 2. Compile the Code and Tests

Assuming your source code is in `src/model/` and your tests are in `src/test/java/model/`:

```
# Create output directory if it doesn't exist
mkdir -p out/test

# Compile source and test files
javac -cp "lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:src" -d out/test src/model/*.java src/test/java/model/*.java
```

## 3. Run the Tests

```
# Run a test class (example: CarTest)
java -cp "out/test:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar:src" org.junit.runner.JUnitCore model.CarTest
```

- Replace `model.CarTest` with the fully qualified name of your test class as needed.

## Notes
- All commands assume you are in the project root directory.
- Adjust paths if your structure is different.
- Only JUnit 4 and Hamcrest jars are required for testing.
