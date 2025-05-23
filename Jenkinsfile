pipeline {
    agent any
    stages {
        stage('Build Backend') {
            steps {
                bat 'mvn clean package -DskipTests=true'
            }
        }
        stage('Unit tests') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Sonar Analysis') {
            environment {
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL'){
                    bat "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://localhost:9000 -Dsonar.login=f8a981db5b06c68f59938daf0349f57e761d1657 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**Application.java"
                }
            }
        }
//        stage('Quality Gate') {
//            steps {
//                sleep(10)
//                timeout(time: 1, unit: 'MINUTES') {
//                    waitForQualityGate abortPipeline: true
//                }
//            }
//        }
        stage('Deploy Backend'){
            steps{
                deploy adapters: [tomcat8(credentialsId: 'Tomcat_login', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
            }
        }
        stage('API Test'){
            steps{
                dir('api-test'){
//                    git branch: 'main', credentialsId: 'github_login', url: 'https://github.com/campospadilhaa/tasks-api-test'
                    git branch: 'main', url: 'https://github.com/campospadilhaa/tasks-api-test'
                    bat 'mvn test'
                }
            }
        }
        stage('Deploy Frontend'){
            steps{
                dir('frontend'){
//                    git branch: 'master', credentialsId: 'github_login', url: 'https://github.com/campospadilhaa/tasks-frontend'
                    git branch: 'master', url: 'https://github.com/campospadilhaa/tasks-frontend'
                    bat 'mvn clean package'
                    deploy adapters: [tomcat8(credentialsId: 'Tomcat_login', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks', war: 'target/tasks.war'
                }
            }
        }
//        stage('Functional Test'){
//            steps{
//                dir('functional-test'){
////                    git branch: 'main', credentialsId: 'github_login', url: 'https://github.com/campospadilhaa/tasks-functional-tests'
//                    git branch: 'main', url: 'https://github.com/campospadilhaa/tasks-functional-tests'
//                    bat 'mvn test'
//                }
//            }
//        }
        stage('Deploy Prod'){
            steps{
                bat 'docker-compose build'
                bat 'docker-compose up -d'
            }
        }
//         stage('Health Check'){
//            steps{
//                // interrompe o fluxo por 10 segundos para dar tempo do 'Deploy Prod' subir
//                sleep(10)
//                dir('functional-test'){
//                    bat 'mvn verify -Dskip.surefire.tests'
//                }
//            }
//        }
//   }
   post {
        always {
//            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml, functional-test/target/surefire-reports/*.xml, functional-test/target/failsafe-reports/*.xml'
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/tasks-backend.war, frontend/target/tasks.war', onlyIfSuccessful: true
        }
        unsuccessful (
            emailext attachLog: true, body: 'Verifique o log em anexo', subject: 'Build ${BUILD_NUMBER} falhou', to: 'campospadilha.a@gmail.com'
        )
        fixed (
            emailext attachLog: true, body: 'Verifique o log em anexo', subject: 'Build realizado!', to: 'campospadilha.a@gmail.com'
        )
   }
}