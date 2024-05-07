pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                // Build code using Maven
                sh 'mvn clean install'
            }
        }

        stage('Unit and Integration Tests') {
            steps {
                // Run unit tests using JUnit
                sh 'mvn test'

                // Run integration tests using Selenium
                sh 'mvn integration-test'
            }
        }

        stage('Code Analysis') {
            steps {
                // Run code analysis using SonarQube
                // Assumes SonarQube server is set up
                sh 'sonar-scanner'
            }
        }

        stage('Security Scan') {
            steps {
                // Perform security scan using OWASP ZAP
                // Assumes OWASP ZAP is installed
                sh 'zap-cli --quick-scan ec2-xx-xxx-xxx-xxx.compute-1.amazonaws.com'
            }
        }

        stage('Deploy to Staging') {
            steps {
                // Deploy to AWS EC2 using AWS CodeDeploy
                sh 'aws deploy create-deployment --application-name MyApp --deployment-group-name Staging --s3-location my-s3-bucket/my-app.zip'
            }
        }

        stage('Integration Tests on Staging') {
            steps {
                // Run integration tests on staging environment
                sh 'mvn integration-test'
            }
        }

        stage('Deploy to Production') {
            steps {
                // Deploy to AWS EC2 production
                sh 'aws deploy create-deployment --application-name MyApp --deployment-group-name Production --s3-location my-s3-bucket/my-app.zip'
            }
        }
    }

    post {
        success {
            emailext body: "Pipeline execution successful. Congratulations!",
                    subject: "Pipeline Success Notification",
                    to: "nairsrilakshmi27@gmail.com"
        }
        failure {
            emailext body: "Pipeline execution failed. Please check logs for details.",
                    subject: "Pipeline Failure Notification",
                    to: "nairsrilakshmi27@gmail.com"
        }
    }
}

