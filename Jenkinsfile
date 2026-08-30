pipeline {
    agent any

    environment {
        SONAR_URL = 'http://sonarqube:9000'
    }

    stages {
        stage('1. Checkout') {
            steps {
                checkout scm
            }
        }

        stage('2. Build') {
            agent { 
                // KORRIGIERT: 'FROM ' wurde entfernt
                docker { image 'maven:3.9.16-eclipse-temurin-25-alpine' } 
            }
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('3. Unit Tests') {
            agent { 
                // KORRIGIERT: 'FROM ' wurde entfernt
                docker { image 'maven:3.9.16-eclipse-temurin-25-alpine' } 
            }
            steps {
                sh 'mvn test'
            }
        }



        stage('5. Package') {
            steps {
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
                echo 'User Acceptance Tests...'
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
