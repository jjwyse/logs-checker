#!/usr/bin/env groovy

// load system variables
def env = System.getenv()

// load config from $HOME/.logs-checker directory
File configFileDirectory = new File(env['HOME'] + '/.logs-checker/')
File configFile = new File(configFileDirectory, 'config.groovy')
if (!configFile.exists())
{
    configFileDirectory.mkdirs()
    configFile = new File(configFileDirectory, 'config.groovy')
    configFile << "config {\n" +
            "  files = [\n" +
            "    [\n" +
            "      'location': '/tmp/bar.log', \n" +
            "      'exceptions': 'bar'\n" +
            "    ],\n" +
            "    [\n" +
            "      'location': '/tmp/foo.log',\n" +
            "      'exceptions': 'foo|bar'\n" +
            "    ]\n" +
            "  ]\n" +
            "}"
}

def config = new ConfigSlurper().parse(configFile.text)

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
                        String command = "terminal-notifier -title log-checker -subtitle " + logFile.path + " " +
                                "-message " + line
                        command.execute()
                    }
                }
                Thread.currentThread().sleep(2000)
            }

        }
        catch (FileNotFoundException fileNotFoundException) {
           System.out << "Did not find file $logFile.name\n"
        }
        finally
        {
            reader?.close()
        }
    } as Runnable

    def t = new Thread(runnable)
    t.start()
}
