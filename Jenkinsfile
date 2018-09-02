def label = "worker-${UUID.randomUUID().toString()}"

podTemplate(label: label,
  serviceAccount: 'jenkins-master',
  containers: [
      containerTemplate(name: 'clojure', image: 'clojure', ttyEnabled: true),
],
volumes: [
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]) {
  node(label) {
    def myRepo = checkout scm
    def gitCommit = myRepo.GIT_COMMIT

    stage('Run unit tests') {
      container('clojure') {
          sh "lein test"
      }
    }
  }
}
