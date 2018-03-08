
node {

  stage('Clone repository') {
    checkout scm
  }

  stage('Run unit tests') {
    sh 'lein test'
  }

}
