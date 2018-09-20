# gradle-integration-test-task
The main goal of project is to provide implemenation of integration task in gradle

# assumptions for integration test
* Should have own structure for source files and resources
* Should have own dependency configuration
* May use classpath from main sources files
* Should have own task to runs integratin test
* Should be in `verification` task group
* Should be exclude from incremental gradle build mechanism
* Should be run before `check` gradle task

# implementation 
1. Should have own structure for source files and resources :

 defininig decided source set with resources under `src/intTest`
 ```intTest {
        groovy {
            srcDir "src/intTest/groovy"
        }

        resources {
            srcDir "src/intTest/resources"
        }
```
and they are be deploying in groovy

2. Should have own dependency configuration :

 to enabled dependency configuration to initTest defining 
  
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

3. May use classpath from main sources files

```
sourceSets {
    intTest {
        ...
        compileClasspath += sourceSets.main.output + configurations.intTestCompile
        runtimeClasspath += sourceSets.main.output + configurations.intTestRuntime
    }
}
```
4. Should have own task to runs integration test
  
  Gralde runs test if task is type of Test
  ```
  task integrationTest(type: Test) {
    ...
  }
  ```
5. Should be in `verification` task group
 ```
 task integrationTest(type: Test) {
    description = 'Integration tests'
    group = 'verification'
      ...
}
```
6. Should be exclude from incremental gradle build mechanism
 ```
 task integrationTest(type: Test) {
    ...
    outputs.upToDateWhen {false}
}
```
7. Should be run before `check` gradle task
```
check.dependsOn integrationTest
```
