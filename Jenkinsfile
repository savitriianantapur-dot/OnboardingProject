pipeline {
    agent any

    options {
        // keep logs reasonable; change as you like
        timestamps()
    }

    environment {
        // service folders (adjust names if your folders differ)
        SERVICE_1 = "Eureka-Service"
        SERVICE_2 = "Auth-Service"
        SERVICE_3 = "ApiGateway-Service"
        // image names used in docker-compose.yml
        IMAGE_1 = "onboarding/eureka-service:latest"
        IMAGE_2 = "onboarding/auth-service:latest"
        IMAGE_3 = "onboarding/api-gateway-service:latest"
        COMPOSE_FILE = "docker-compose.yml"
    }

    stages {

        stage('Pre-checks & Prepare') {
            steps {
                echo "Workspace and disk checks"
                sh 'df -h || true'
                echo "Ensure mvnw is executable for each service (safe if already set)"
                sh "chmod +x ${SERVICE_1}/mvnw || true"
                sh "chmod +x ${SERVICE_2}/mvnw || true"
                sh "chmod +x ${SERVICE_3}/mvnw || true"
            }
        }

        stage('Build JARs (Maven)') {
            steps {
                script {
                    echo "Building ${SERVICE_1}"
                    dir("${SERVICE_1}") { sh "./mvnw -B -DskipTests clean package" }

                    echo "Building ${SERVICE_2}"
                    dir("${SERVICE_2}") { sh "./mvnw -B -DskipTests clean package" }

                    echo "Building ${SERVICE_3}"
                    dir("${SERVICE_3}") { sh "./mvnw -B -DskipTests clean package" }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    echo "Building Docker image ${IMAGE_1}"
                    sh "docker build -t ${IMAGE_1} ${SERVICE_1}"

                    echo "Building Docker image ${IMAGE_2}"
                    sh "docker build -t ${IMAGE_2} ${SERVICE_2}"

                    echo "Building Docker image ${IMAGE_3}"
                    sh "docker build -t ${IMAGE_3} ${SERVICE_3}"
                }
            }
        }

        stage('Deploy (docker-compose)') {
            steps {
                echo "Bringing down previous compose stack (if any) and starting up fresh"
                // attempt to stop previous stack, ignore errors
                sh "docker compose -f ${COMPOSE_FILE} down || true"
                // build images from local tags and start
                sh "docker compose -f ${COMPOSE_FILE} up -d --build"
            }
        }

        stage('Post-deploy verification') {
            steps {
                echo "Show running containers"
                sh "docker ps --format 'table {{.Names}}\\t{{.Image}}\\t{{.Ports}}' || true"
                echo "Show compose status"
                sh "docker compose -f ${COMPOSE_FILE} ps || true"
            }
        }
    }

    post {
        success {
            echo "All services built and deployed successfully."
        }
        failure {
            echo "Build or deployment failed. Inspect the console log above for errors."
        }
    }
}
