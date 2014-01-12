#!/usr/bin/env groovy

// load system variables
def env = System.getenv()

// load config from $HOME/.logs-checker directory
def config = new ConfigSlurper().parse(new File(env['HOME'] + '/.log-checker/config.groovy').text)

// for each file in the config start up a tail thread
def reader = new TailReader()
config.config.files.each() { file ->
    String exceptions = file.exceptions
    String command = "terminal-notifier -title log-checker -subtitle " + file.location + " -message Exception Found"
    def closure = {
        command.execute()
    }
    reader.tail(new File(file.location), exceptions.split("\\|"), closure)
}
