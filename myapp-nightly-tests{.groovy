pipeline{
    agent any

    tools{
        maven 'MAVEN3.9'
    }

    parameters{
        string(name:'GIT_BRANCH',defaultValue:'atom',description:'atom branch') 
    }

    triggers{
        cron('H 2 * * *')
    }

    stages{
        stage('checkout'){
        steps{
           
            echo "Nightly tests – checkout from branch ${params.GIT_BRANCH}"

             git branch: params.GIT_BRANCH,
             url:'https://github.com/hkhcoder/vprofile-project.git'
 
        }
      }

      stage('tests-only'){
        steps{
            echo "Here I will run mvn clean test for nightly tests"
            sh"""
            mvn clean test
            """
        }
      }


      stage('archive-test-reports'){
        steps{
          echo"Archiving test reports from target/surefire-reports"
           archiveArtifacts artifacts: 'target/surefire-reports/**',allowEmptyArchive: true
        }
      }


    }
}