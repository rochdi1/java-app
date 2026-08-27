pipeline {
    agent any

    environment {
        SONAR_URL = '127.0.0.1:9000'
    }

    stages {
        stage('1. Checkout') {
            steps {
                checkout scm
            }
        }

        stage('2. Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('3. Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('4. SonarQube Analysis') {
            steps {
                echo "Sende Daten an SonarQube unter ${SONAR_URL}"
                sh "mvn sonar:sonar -Dsonar.host.url=${SONAR_URL}"
            }
        }

        stage('5. Package') {
            steps {
                sh 'mvn package -DskipTests'
                sh 'docker build -t java-app:${BUILD_NUMBER} .'
                sh 'docker tag java-app:${BUILD_NUMBER} java-app:latest'
            }
        }

        stage('6. Deploy to Test (Helm)') {
            steps {
                sh "helm upgrade --install java-app-test ./charts/java-app --set image.tag=${BUILD_NUMBER} --namespace test --create-namespace"
            }
        }

        stage('7. User Acceptance Tests') {
            steps {
                echo 'Simuliere Selenium/UAT Tests gegen die Testumgebung...'
               // sh 'curl -I http://java-app-test.test.svc.cluster.local || true'
            }
        }

        stage('8. Promote to Production (ArgoCD)') {
            steps {
                sh "sed -i 's/tag:.*/tag: ${BUILD_NUMBER}/g' charts/java-app/values.yaml"
                echo "Version in values.yaml auf ${BUILD_NUMBER} aktualisiert. Argo CD zieht sich nun die Änderungen automatisch!"
            }
        }
    }
}
