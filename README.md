# gradle-integration-test-task
The main goal of project is to provide implemenation of integration task in gradle

# integration tests assumptions
1. dedicated structure for source files and resources
1. separate dependency configuration
1. use classpath from main sources files
1. unique task for running integration tests
1. `verification` task group
1. excluded from incremental gradle build mechanism
1. run before `check`

# implementation 
1. dedicated structure for source files and resources (`src/intTest`)
   ```
   intTest {
        groovy {
            srcDir "src/intTest/groovy"
        }

        resources {
            srcDir "src/intTest/resources"
        }
   ```
1. separate dependency configuration
   ```
   configurations {
     intTestCompile.extendsFrom testCompile
     intTestCompile.extendsFrom integrationTestCompile, testRuntime
     intTestImplementation.extendsFrom implementation
     intTestRuntimeOnly.extendsFrom runtimeOnly
   }
   ```
   **`configurations` should be before `dependencies` sections**
   ```
   dependencies {
       ...
       intTestCompile group: 'org.codehaus.groovy', name: 'groovy', version: '2.4.15'
       intTestCompile group: 'org.spockframework', name: 'spock-core', version: '1.1-groovy-2.4'
    }
    ```
1. use classpath from main sources files
   ```
   sourceSets {
       intTest {
           ...
           compileClasspath += sourceSets.main.output + configurations.intTestCompile
           runtimeClasspath += sourceSets.main.output + configurations.intTestRuntime
       }
   }
   ```
1. unique task for running integration tests
   ```
   task integrationTest(type: Test) {
     ...
   }
   ```
1. `verification` task group
   ```
    task integrationTest(type: Test) {
       description = 'Integration tests'
       group = 'verification'
       ...
   }
   ```
1. excluded from incremental gradle build mechanism
   ```
    task integrationTest(type: Test) {
       ...
       outputs.upToDateWhen {false}
   }
   ```
1. run before `check`
   ```
     check.dependsOn integrationTest
   ```
