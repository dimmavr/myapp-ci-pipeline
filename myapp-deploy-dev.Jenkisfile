pipeline{
    agent any 
    
    tools{
        maven 'MAVEN3.9'
    }

    parameters{
        string(name:'APP_VERSION',defaultValue:'1.0.0',description:'app version')
        booleanParam(name:'KEEP_BACKUPS',defaultValue:true,description:'Keep multiple backups (true) or only latest (false)')
    }
    stages{
        stage('download-artifact-from-ci'){
            steps{
                copyArtifacts(
                    projectName:'myapp-ci',
                    filter:"target/myapp-${APP_VERSION}.war"
                )
                sh 'ls -l target/myapp-*'
                sh '''
                echo "myapp-$APP_VERSION.war"

                '''
            }
        }

       stage('backup-current-dev') {
    steps {
        sh '''
        archive="/opt/dev-apps/myapp.war"
        backup_dir="/opt/backups/dev"

        echo "Backup /opt/dev-apps/myapp.war to /opt/backups/dev"

        if [ ! -f "$archive" ]; then
            echo "No existing myapp.war to backup"
            exit 0
        fi

        mkdir -p "$backup_dir"

        timestamp=$(date +%Y%m%d-%H%M%S)
        backup_file="$backup_dir/myapp-dev-${timestamp}.war"

        cp "$archive" "$backup_file"
        echo "Created backup: $backup_file"

        # Κράτα μόνο τα 3 πιο καινούργια backups, σβήσε τα παλιότερα
        backups=$(ls -1t "$backup_dir"/myapp-dev-*.war 2>/dev/null || true)

        echo "$backups" | tail -n +4 | xargs -r rm --

        echo "Backup rotation completed."
        '''
    }
}


        stage('deploy-to-dev'){
            steps{
                sh """
                archive="target/myapp-${params.APP_VERSION}.war"
                target_dir="/opt/dev-apps"
                target="\$target_dir/myapp.war"

                mkdir -p "\$target_dir"

                if [ ! -f "\$archive" ]; then
                echo "Artifact not found: \$archive"
                exit 1
                fi

                cp "\$archive" "\$target"
                echo "Deployed myapp-${params.APP_VERSION}.war to /opt/dev-apps/myapp.war"

                """
            }
        }
    }
}