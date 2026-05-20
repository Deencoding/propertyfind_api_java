pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk'
    }

    environment {
        APP_NAME     = 'java-app'
        JAR_FILE     = "target/${APP_NAME}-*.jar"
        JOB_NAME     = "${env.JOB_NAME}"
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
        BUILD_URL    = "${env.BUILD_URL}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Code Quality') {
            steps {
                echo 'Running code quality checks...'
                sh 'mvn checkstyle:check'
            }
        }

        stage('Package') {
            steps {
                echo 'Packaging the application...'
                sh 'mvn package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying...'
                sh """
                    echo 'Deploying ${APP_NAME}...'
                    java -jar ${JAR_FILE}
                """
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            script {
                try { cleanWs() } catch (e) { echo 'Workspace cleanup skipped: ' + e.message }
            }
        }
    }
}
