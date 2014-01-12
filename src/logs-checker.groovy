#!/usr/bin/env groovy

// load system variables
def env = System.getenv()

// load config from $HOME/.logs-checker directory
def config = new ConfigSlurper().parse(new File(env['HOME'] + '/.logs-checker/config.groovy').text)

// for each file in the config start up a tail thread
config.config.files.each() { file ->
    String exceptions = file.exceptions
    String command = "terminal-notifier -title log-checker -subtitle " + file.location + " -message Exception Found " +
            "-sound default"

    File logFile = new File(file.location)
    String [] blacklist = exceptions.split('\\|')
    def closure = {
        command.execute()
    }
    def runnable = {
        def reader

        try
        {
            reader = logFile.newReader()
            reader.skip(logFile.length())

            def line

            while (true)
            {
                line = reader.readLine()
                for (String word : blacklist)
                {
                    if (line?.contains(word))
                    {
                        closure.call(line)
                    }
                }
                Thread.currentThread().sleep(2000)
            }

        }
        finally
        {
            reader?.close()
        }
    } as Runnable

    def t = new Thread(runnable)
    t.start()
}
