logs-checker
==============
Groovy script that will monitor a given set of files for a given set of strings and when one of those
strings appears, it will send a desktop notification

setup
==============
* If you don't have the command-line tool 'terminal-notifier' installed, do so <a href="https://github.com/alloy/terminal-notifier" target="_blank">here</a>
* Make sure the 'terminal-notifier' tool is on your $PATH
* Create the logs-checker config file here $HOME/.logs-checker/config.groovy - below is an example
  * Note: if you do not create this directory and/or file, then the first time the script is run it will create a default configuration for you

```
config {
  files = [
    [
      'location': '/tmp/bar.log',
      'exceptions': 'bar'
    ],
    [
      'location': '/tmp/foo.log',
      'exceptions': 'foo|bar'
    ]
  ]
}
```
