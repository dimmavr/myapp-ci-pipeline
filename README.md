# MyApp CI/CD Pipelines – Jenkins, Maven, Linux Deployments
### Author: dimmavr

This repository contains all Jenkins Pipelines I created to automate the full CI/CD lifecycle of a Java Web Application.  
The whole system was designed and implemented by me as a real-world DevOps learning project.

---

##  Overview

I implemented a complete CI/CD system, which includes:

- **Continuous Integration (myapp-ci)**
- **Automatic Deployment to DEV (myapp-deploy-dev)**
- **Manual Approved Deployment to PROD (myapp-deploy-prod)**
- **Nightly Automated Testing (myapp-nightly-tests)**

The system is based on:

- Jenkins Declarative Pipelines  
- Maven Build Automation  
- Linux Deployments  
- Shell scripting for backups & rotation  
- Cron triggers for nightly builds  

---

#  myapp-ci – Continuous Integration Pipeline

Main pipeline performing:

###  Checkout  
###  Maven build with logic:
- SKIP_TESTS  
- RUN_FULL_BUILD  
- Custom APP_VERSION  

###  Artifact creation:
- myapp-<version>.war  
- myapp-<build-number>.war  

###  Archives artifacts  
###  Publishes artifacts to:
```
/opt/artifacts/myapp
```

###  Auto-triggers DEV deployment

---

#  myapp-deploy-dev – Automated DEV Deployment

Automatically triggered from CI.

Performs:

- Copy artifact from CI via copyArtifacts  
- Backup of existing DEV war:
  ```
  /opt/backups/dev/myapp-dev-<timestamp>.war
  ```
- Rotation (Keeps last 3)
- Deployment to:
  ```
  /opt/dev-apps/myapp.war
  ```

---

#  myapp-deploy-prod – Manual Production Deployment

Secure and manual release pipeline.

Includes:

###  Requires:
```
CONFIRM_PROD_DEPLOY = true
```

###  Retrieves correct artifact  
###  Backup of current PROD war:
```
/opt/backups/prod/myapp-prod-<timestamp>.war
```

###  Rotation: keep last 5  
###  Deployment to:
```
/opt/prod-apps/myapp.war
```

###  Logs version, build, timestamp

---

#  myapp-nightly-tests – Automated Nightly Testing

Triggered daily using:

```
H 2 * * *
```

Runs:

- Checkout develop  
- mvn clean test  
- Archive reports from:
  ```
  target/surefire-reports/**
  ```

---

#  Technologies Used

- Jenkins Pipeline  
- Maven  
- Git SCM  
- Linux Deployment  
- Shell scripting  
- Cron  
- Copy Artifact Plugin  
- Artifact fingerprinting  

---

#  Repository Structure

```
myapp-ci-pipeline/
 ci.Jenkinsfile
 deploy-dev.Jenkinsfile
 deploy-prod.Jenkinsfile
 nightly-tests.Jenkinsfile
```


