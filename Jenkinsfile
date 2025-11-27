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
                    url: 'https://github.com/dimmavr/myapp-ci-pipeline.git'
            }
        }

        stage('build') {
            steps {
                sh """
                if [ "${params.SKIP_TESTS}" = "true" ]; then
                    echo "Skipping tests"
                    mvn clean package -DskipTests
                else
                    echo "Running full build"
                    mvn clean test package
                fi
                """
            }
        }
    }
}
