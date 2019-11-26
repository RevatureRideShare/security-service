pipeline {

    agent any
    
    triggers {
    	
    	pollSCM('') // Enabling being build on Push
  	}
  	
  	options{
  		disableConcurrentBuilds()
  		buildDiscarder(logRotator(numToKeepStr: '3'))
  	}

    stages {

        stage ('Build') {
            steps {
            
            deleteDir()
            checkout scm
            sh 'mvn update'
            
                    // Run in non-interactive (batch) mode
                sh 'mvn -B -DskipTests clean package'
            }
        }


        stage ('Test') {
            steps {
            	sh 'mvn verify checkstyle:checkstyle'
            	sh 'mvn verify checkstyle:check'
                sh 'mvn verify sonar:sonar'
                sh 'mvn test'
            }
        }
        
        
        stage ('Deploy') {
            steps {
                withCredentials([[$class          : 'UsernamePasswordMultiBinding',
                                  credentialsId   : 'PCF_LOGIN',
                                  usernameVariable: 'USERNAME',
                                  passwordVariable: 'PASSWORD']]) {
                    sh 'cf events rideshare-security-service'
					sh 'cf logs rideshare-security-service --recent'
                    sh 'cf login -a http://api.run.pivotal.io -u $USERNAME -p $PASSWORD \
                    -o "Revature Training" -s development'
                    sh 'cf push'
                    
                }
            }
        }
    }
        
     post{
       	always{
        	deleteDir()
        }
    }
}
