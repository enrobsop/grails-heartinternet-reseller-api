class HeartInternetResellerApiGrailsPlugin {

    def version = "0.1.2"
    def grailsVersion = "2.0 > *"
	def pluginExcludes = [
			"grails-app/controllers/**",
			"grails-app/domain/**",
			"grails-app/i18n/**",
			"grails-app/views/**/*",
			"web-app/**"
	]

    def title = "Heart Internet Reseller API Plugin"
    def author = "Paul Osborne"
    def authorEmail = "hello@paulosborne.me.uk"
    def description = '''\
Grails Plugin providing integration with the Heart Internet Reseller API.
'''

    def documentation = "https://github.com/enrobsop/grails-heartinternet-reseller-api/blob/master/README.md"
    def license = "APACHE"
	def organization = [ name: "Paul Osborne", url: "http://www.paulosborne.me.uk/" ]
	def issueManagement = [ system: "GitHub", url: "https://github.com/enrobsop/grails-heartinternet-reseller-api/issues" ]
	def scm = [ url: "https://github.com/enrobsop/grails-heartinternet-reseller-api" ]

}
