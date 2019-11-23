pipeline {

    agent any
    
    triggers {
    	pollSCM('') // Enabling being build on Push
  	}

    stages {

        stage ('Build') {
            steps {
                //withMaven(maven: 'maven_3_6_2') {
                    // Run in non-interactive (batch) mode
                	sh 'mvn -B -DskipTests clean package'
             //   }
            }
        }


        stage ('Test') {
            steps {
                //withMaven(maven: 'maven_3_6_2') {
                	//sh 'mvn verify sonar:sonar'
                	sh 'mvn test'
                //}
            }
        }
        
        
        stage ('Deploy') {
            steps {
            //echo 'Deploy'
                withCredentials([[$class          : 'UsernamePasswordMultiBinding',
                                  credentialsId   : 'PCF_LOGIN',
                                  usernameVariable: 'USERNAME',
                                  passwordVariable: 'PASSWORD']]) {
                    sh 'cf events rideshare-security-service --recent'
					sh 'cf logs rideshare-security-service --recent'
                    sh 'cf login -a http://api.run.pivotal.io -u $USERNAME -p $PASSWORD \
                    -o "Revature Training" -s development'
                    //sh 'cf push'
                    
                }
            }
        }

    }
    
}