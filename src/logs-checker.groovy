// load system variables
def env = System.getenv()

// load config
def config = new ConfigSlurper("files").parse(new File(env['HOME'] + '/.log-checker/config.groovy').toURI().toURL())

print config

// for each file in the config start up a tail thread
def reader = new TailReader()
reader.tail(new File('/tmp/foo.log'), { "terminal-notifier -title log-checker -subtitle foo.log -message $it".execute() })
reader.tail(new File('/tmp/bar.log'), { "terminal-notifier -title log-checker -subtitle bar.log -message $it".execute() })
