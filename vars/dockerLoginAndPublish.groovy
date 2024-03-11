// File: vars/dockerUtils.groovy

def loginAndPushDockerImage(String repository, String imageName, String credentialsId) {
    // Docker login using credentials
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
        sh "docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD"

        // Generate unique build ID
        def buildId = env.BUILD_ID
        
        // Tag and push the image with build ID
        def imageNameWithTag = "$repository/$imageName:$buildId"
        sh "docker tag $imageName $imageNameWithTag"
        sh "docker push $imageNameWithTag"

        // Tag and push the image with 'latest'
        def imageNameWithLatest = "$repository/$imageName:latest"
        sh "docker tag $imageName $imageNameWithLatest"
        sh "docker push $imageNameWithLatest"
    }
}
/*
Sample code to execute this 


// Jenkinsfile

pipeline {
    agent any
    
    parameters {
        string(name: 'DOCKER_REPO', defaultValue: 'bdocker', description: 'Docker repository')
        string(name: 'IMAGE_NAME', defaultValue: 'lab-service', description: 'Docker image name')
        string(name: 'DOCKER_CREDENTIALS_ID', defaultValue: 'bkdockerid', description: 'Docker credentials ID')
    }

    stages {
        stage('Build and Push Docker Image') {
            steps {
                script {
                    // Call the shared library function to login and push Docker image
                    dockerLoginAndPublish.loginAndPushDockerImage([
                        DOCKER_REPO: params.DOCKER_REPO,
                        IMAGE_NAME: params.IMAGE_NAME,
                        DOCKER_CREDENTIALS_ID: params.DOCKER_CREDENTIALS_ID
                    ])
                }
            }
        }
    }
}
*/
