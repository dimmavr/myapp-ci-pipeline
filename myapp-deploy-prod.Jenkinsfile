pipeline{
    agent any

    parameters{
        string(name:'APP_VERSION',defaultValue:'1.0.0',description:'App version to deploy to PROD')
        booleanParam(name:'CONFIRM_PROD_DEPLOY',defaultValue:false,description:'Must be true to allow PROD deploy')
        string(name:'SOURCE_BUILD',defaultValue:'manual',description:'Info about source build/branch (e.g. myapp-ci #15, main)')
    }
    stages{
        stage('validate-approval') {
         steps {
          sh """
            if [ "${params.CONFIRM_PROD_DEPLOY}" != "true" ]; then
               echo "Prod deploy not confirmed"
               exit 1
            else
                echo "Prod deploy confirmed"
            fi
            """
            }
        }
        stage('download-artifact-from-ci'){
            steps{
                sh """
                echo "target/myapp-${params.APP_VERSION}.war from myapp-ci"
                """
                copyArtifacts(
                    projectName: 'myapp-ci',
                    filter:"target/myapp-${params.APP_VERSION}.war"
                )

                sh """
                ls -l target/myapp-*
                """
            }
        }

        stage('backup-current-prod'){
            steps{
                sh"""
                archive="/opt/prod-apps/myapp.war"
                backup_dir="/opt/backups/prod"

                if [ ! -f "\$archive" ]; then
                echo "No existing PROD myapp.war to backup, skipping"
                exit 0
                fi

                mkdir -p "\$backup_dir"
                timestamp=\$(date +%Y%m%d-%H%M%S)
                backup_files="\$backup_dir/myapp-prod-\${timestamp}.war"

                cp "\$archive" "\$backup_files" 

                echo "Created PROD backup: \${backup_files}"

                backups_to_delete=\$(ls -1t \${backup_dir}/myapp-prod-*.war 2>/dev/null | tail -n +6)

                if [ -n "\$backups_to_delete" ]; then
                echo "Removing old backups:"
                echo "\$backups_to_delete"
                echo "\$backups_to_delete" | xargs rm -f
                fi

                """
            }
        }



        stage('deploy-to-prod'){
            steps{
                sh"""
                archive="target/myapp-${params.APP_VERSION}.war"
                target_dir="/opt/prod-apps"
                target="\$target_dir/myapp.war"

                mkdir -p "\$target_dir"

                if [ ! -f "\$archive" ]; then
                echo "Artifact not found: \$archive"
                exit 1
                fi

                cp "\$archive" "\$target" 
                echo "Deployed APP_VERSION=${params.APP_VERSION} from SOURCE_BUILD=${params.SOURCE_BUILD} to \$target"

                """
              
            }
        }
    }
}