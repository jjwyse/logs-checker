/**
 * Tail reader which will take in a file and a closure and execute the closure each time a new line is discovered
 * in the file.
 *
 * @version %I%, %G%
 * @author jjwyse
 */
class TailReader
{
    boolean stop = false

    public void stop()
    {
        stop = true
    }

    public void tail(File file, def closure)
    {
        def runnable = {
            def reader

            try
            {
                reader = file.newReader()
                reader.skip(file.length())

                def line

                while (!stop)
                {
                    line = reader.readLine()
                    if (line)
                    {
                        closure.call(line)
                    }
                    else
                    {
                        Thread.currentThread().sleep(5000)
                    }
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
