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
    def closure = {
        command.execute()
    }
    new TailReader().tail(new File(file.location), exceptions.split("\\|"), closure)
}

class TailReader
{
    public void tail(File file, String[] blacklist, def closure)
    {
        def runnable = {
            def reader

            try
            {
                reader = file.newReader()
                reader.skip(file.length())

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
}
