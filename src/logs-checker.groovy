def reader = new TailReader()

File file = new File('/tmp/foo.log');
reader.tail(file, { "terminal-notifier -message $it".execute() })