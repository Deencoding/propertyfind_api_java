pipeline {
    environment {
        // Application specific names
        APP_NAME              = 'propertyfind'
        ECR_REPO_NAME         = 'propertyfind-backend' 
        
        // AWS specific configuration
        AWS_REGION            = 'eu-north-1' 
        AWS_ACCOUNT_ID        = '355353496029' 
        
        // ECS specific configuration
        ECS_CLUSTER           = 'propertyfind-cluster'
        ECS_SERVICE           = 'propertyfind-service'
        
        DOCKER_BUILDKIT       = '1'

        // AWS Credentials pulled securely from Jenkins credentials store
        AWS_ACCESS_KEY_ID     = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
    }

    // Keep this custom Docker agent so Maven, AWS CLI, and Docker are perfectly installed!
    agent {
        dockerfile {
            dir 'jenkins/'
            args '--user root -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    stages {
        stage('Initialize env vars') {
            steps {
                script {
                    env.GIT_SHORT_SHA = sh(script: "echo ${GIT_COMMIT} | cut -c -7", returnStdout: true).trim()
                    env.IMAGE_TAG = "${GIT_SHORT_SHA}-${BUILD_NUMBER}"
                    echo "Branch: ${env.BRANCH_NAME} | Tag: ${env.IMAGE_TAG}"
                }
            }
        }

        stage('Checkout Code') {
            steps {
                cleanWs()
                checkout scm 
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${ECR_REPO_NAME}:${IMAGE_TAG} ."
            }
        }

        stage('Login to AWS ECR') {
            steps {
                sh """
                aws ecr get-login-password --region ${AWS_REGION} \
                | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
                """
            }
        }

        stage('Tag & Push Image') {
            steps {
                sh """
                    docker tag ${ECR_REPO_NAME}:${IMAGE_TAG} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO_NAME}:latest
                    docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO_NAME}:latest
                """
            }
        }

        stage('Deploy to ECS') {
            steps {
                sh """
                aws ecs update-service \
                --cluster ${ECS_CLUSTER} \
                --service ${ECS_SERVICE} \
                --force-new-deployment \
                --region ${AWS_REGION}
                """
            }
        }
    }

    post {
        success {
            echo "Build ${BUILD_NUMBER} succeeded! Deployed ${IMAGE_TAG} to ECS."
        }
        failure {
            echo "Build failed"
        }
        always {
            script {
                if (env.WORKSPACE != null) {
                    cleanWs()
                } else {
                    echo "No workspace to clean."
                }
            }
        }
    }
}
