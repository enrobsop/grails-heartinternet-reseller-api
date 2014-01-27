grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {

    inherits("global")
    log "warn"

    repositories {
        grailsCentral()
        mavenCentral()
	    mavenRepo "http://repo.grails.org/grails/libs-releases/"
	    mavenRepo "http://m2repo.spockframework.org/ext/"
	    mavenRepo "http://m2repo.spockframework.org/snapshots/"
    }

    dependencies {
	    test('org.spockframework:spock-grails-support:0.7-groovy-2.0') {
		    export = false
	    }
    }

    plugins {

        build(":release:2.2.1",
              ":rest-client-builder:1.0.3") {
            export = false
        }

	    test(':spock:0.7') {
		    export = false
		    exclude 'spock-grails-support'
	    }

	    compile ":joda-time:1.4"

    }

}
