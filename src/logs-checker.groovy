#!/usr/bin/env groovy

// load system variables
def env = System.getenv()

// load config from $HOME/.logs-checker directory
def config = new ConfigSlurper().parse(new File(env['HOME'] + '/.logs-checker/config.groovy').text)

// for each file in the config start up a tail thread
config.config.files.each() { file ->

    // load config values
    String exceptions = file.exceptions
    File logFile = new File(file.location)

    def runnable = {
        def reader

        try
        {
            reader = logFile.newReader()
            reader.skip(logFile.length())

            while (true)
            {
                def line = reader.readLine()
                for (String word : exceptions.split('\\|'))
                {
                    if (line?.contains(word))
                    {
                        String command = "terminal-notifier -title log-checker -subtitle " + logFile.path + " -message " + line
                        command.execute()
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
