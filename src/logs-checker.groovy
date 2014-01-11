def reader = new TailReader()

reader.tail(new File('/tmp/foo.log'), { "terminal-notifier -title log-checker -subtitle foo.log -message $it".execute() })
reader.tail(new File('/tmp/bar.log'), { "terminal-notifier -title log-checker -subtitle bar.log -message $it".execute() })
