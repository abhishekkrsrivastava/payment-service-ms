pipeline{

    agent any

    tools{
        maven "maven"
    }
    environment{
         APP_NAME= "payment-service"
         RELEASE= "1.0.0"
         DOCKER_USER= "abhishekvanaras"
         IMAGE_NAME= "${DOCKER_USER}"+"/"+"${APP_NAME}"
         IMAGE_TAG= "${RELEASE}-${BUILD_NUMBER}"
    }
    stages{

        stage("SCM Checkout"){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/abhishekkrsrivastava/payment-service-ms.git']])
            }

        }
        stage("Build Stage Maven"){
            steps{
                script{
                    bat 'mvn clean install -Dmaven.test.skip=true'
                }
            }
        }
        stage("Build Docker Image"){
            steps{
                script{
                    bat 'docker build -t %APP_NAME%:%IMAGE_TAG% .'
                }
            }
        }
        stage("Tag the Image"){
            steps{
                script{
                    bat 'docker tag %APP_NAME%:%IMAGE_TAG% %DOCKER_USER%/%APP_NAME%:%IMAGE_TAG%'
                }
            }
        }
        stage("Docker Login"){
            steps{
                script{
                   withCredentials([string(credentialsId: 'dockerpassword', variable: 'dockerpassword')]) {
                   bat 'docker login -u abhishekvanaras -p %dockerpassword%'
                   }
                }
            }
        }
        stage("Docker Push"){
            steps{
                script{
                    bat 'docker push %DOCKER_USER%/%APP_NAME%:%IMAGE_TAG%'
                }
            }
        }

    }

    post{
        always{
           emailext attachLog: true, body: '''<html>
<body>
<p> Build Status: %BUILD_STATUS%</p>
<p> Build Number: %BUILD_NUMBER%</p>
<p> Check the <a href="%BUILD_URL%"> Console Output</a></p>
</html>
</body>''', mimeType: 'text/html', replyTo: 'abhishekvanaras@gmail.com', subject: 'PipeLine Status: %BUILD_NUMBER%', to: 'abhishekvanaras@gmail.com'
        }
    }
}


// SCM CHECKOUT, BUILD , DEPLOY, NOTIFICATION