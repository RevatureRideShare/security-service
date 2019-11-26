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
            
            echo env.BRANCHNAME
            
                    // Run in non-interactive (batch) mode
                sh 'mvn -B -DskipTests clean package'
            }
        }


        stage ('Test') {
            steps {
            	sh 'mvn test'
                sh 'mvn verify sonar:sonar'
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
        	echo env.BRANCH_NAME
        }
    }
}
