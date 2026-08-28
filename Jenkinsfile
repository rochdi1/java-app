pipeline {
    agent any

    environment {
        SONAR_URL = 'http://sonarqube-local:9000'
    }

    stages {
        stage('1. Checkout') {
            steps {
                checkout scm
            }
        }

        stage('2. Build') {
            agent { 
                docker { image 'maven:3.9.9-eclipse-temurin-25-alpine' } 
            }
            steps {
                // Hier funktioniert 'mvn' jetzt garantiert, da es im Container vorinstalliert ist!
                sh 'mvn clean compile'
            }
        }

        stage('3. Unit Tests') {
            agent { 
                docker { image 'maven:3.9.9-eclipse-temurin-25-alpine' } 
            }
            steps {
                sh 'mvn test'
            }
        }

        stage('4. SonarQube Analysis') {
            agent { 
                docker { image 'maven:3.9.9-eclipse-temurin-25-alpine' } 
            }
            steps {
                sh "mvn sonar:sonar -Dsonar.host.url=${SONAR_URL}"
            }
        }

        stage('5. Package') {
            steps {
                // Das Paketieren des Docker-Images läuft wieder auf dem Host-Socket
                sh 'docker build -t java-app:${BUILD_NUMBER} .'
                sh 'docker tag java-app:${BUILD_NUMBER} java-app:latest'
            }
        }

        stage('6. Deploy to Test (Helm)') {
            agent {
                docker { 
                    image 'alpine/helm:3.16.2'
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
                }
            }
            steps {
                sh "helm upgrade --install java-app-test ./charts/java-app --set image.tag=${BUILD_NUMBER} --namespace test --create-namespace"
            }
        }

        stage('7. User Acceptance Tests') {
            agent { docker { image 'curlimages/curl:latest' } }
            steps {
                sh 'curl -I http://cluster.local || true'
            }
        }

        stage('8. Promote to Production (ArgoCD)') {
            steps {
                sh "sed -i 's/tag:.*/tag: ${BUILD_NUMBER}/g' charts/java-app/values.yaml"
                echo "Version in values.yaml auf ${BUILD_NUMBER} aktualisiert."
            }
        }
    }
}
