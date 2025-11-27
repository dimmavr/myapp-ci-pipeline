pipeline {
    agent any

    tools {
        maven 'MAVEN3.9'
    }

    parameters {
        string(name: 'GIT_BRANCH', defaultValue: 'main', description: 'git branch')
        string(name: 'APP_VERSION', defaultValue: '1.0.0', description: 'app version')
        booleanParam(name: 'SKIP_TESTS', defaultValue: false, description: 'skip tests')
        booleanParam(name: 'RUN_FULL_BUILD', defaultValue: true, description: 'run full build')
    }

    stages {
        stage('checkout') {
            steps {
                echo "git branch: ${params.GIT_BRANCH}"

                git branch: params.GIT_BRANCH,
                    url: 'https://github.com/hkhcoder/vprofile-project.git'
            }
        }

        stage('build') {
            steps {
                echo "SKIP_TESTS = ${params.SKIP_TESTS}"
                echo "RUN_FULL_BUILD = ${params.RUN_FULL_BUILD}"
                echo "APP_VERSION = ${params.APP_VERSION}"

                sh """
                if [ "${params.SKIP_TESTS}" = "true" ]; then
                    echo "Skipping tests"
                    mvn clean package -DskipTests
                else
                    echo "Running full build (tests ON)"
                    mvn clean test package
                fi
                """
            }
        }

        stage('package-artifact') {
            steps {
                sh """
                # Βρίσκουμε το πρώτο .war στο target/
                ARTIFACT=\$(ls target/*.war | head -n 1)
                echo "Found artifact: \$ARTIFACT"

                # Φτιάχνουμε versioned αρχεία
                cp "\$ARTIFACT" "target/myapp-${params.APP_VERSION}.war"
                echo "Created target/myapp-${params.APP_VERSION}.war"

                cp "\$ARTIFACT" "target/myapp-${BUILD_NUMBER}.war"
                echo "Created target/myapp-${BUILD_NUMBER}.war"
                """
            }
        }

        stage('archive') {
            steps {
                archiveArtifacts artifacts: 'target/myapp-*.war', fingerprint: true
            }
        }
    }
}
