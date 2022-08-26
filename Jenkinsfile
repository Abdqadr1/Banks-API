pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "MAVEN_HOME"
    }
    stages {
        stage('Build Maven Project') {
            steps {
                // Checkout code from github
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'jenkins', url: 'https://github.com/Abdqadr1/api-q']]])
                // clean install
                bat "mvn clean install"
            }
        }
        stage("Build Docker Image"){
            steps{
                // script for building docker image
                script{
                    bat "docker build -t abdqadr/banks_api ."
                }
            }
        }
        stage("Push Image to DockerHub"){
            steps{
                script{
                    withCredentials([string(credentialsId: 'dockerhubpwd', variable: 'dockerhubcredentials')]) {
                        // login to docker
                        bat "docker login -u abdqadr -p ${dockerhubcredentials}"

                        //push image to dockerhub
                        bat "docker push abdqadr/banks_api"
                    }
                }
            }

        }
    }
}
