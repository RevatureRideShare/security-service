pipeline {

    agent any
    
    triggers {
    	
    	pollSCM('') // Enabling being build on Push
  	}
  	
  	options{
  		disableConcurrentBuilds()
  		buildDiscarder(logRotator(numToKeepStr: '10'))
  	}

    stages {

        stage ('Build') {
            steps {
            
            deleteDir()
            checkout scm
            
                    // Run in non-interactive (batch) mode
                sh 'mvn -U -B -DskipTests clean package'
            }
        }


        stage ('Test') {
            steps {
            	sh 'mvn verify checkstyle:checkstyle'
                sh 'mvn verify sonar:sonar'
            }
        }
        
  		stage ('QualityGate') {
  			steps{
  				timeout(time: 1, unit: 'MINUTES'){
  					waitForQualityGate abortPipeline: true
  				}
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
