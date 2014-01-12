logs-checker
==============
Mac desktop application that will monitor a given set of files for a given set of strings and when one of those
strings appears, it will send a desktop notification

setup
==============
* If you don't have the command-line tool 'terminal-notifier' installed, do so <a href="https://github.com/alloy/terminal-notifier" target="_blank">here</a>
* Create the logs-checker config file here $HOME/.logs-checker/config.groovy - below is an example

```
config {
  files = [
    [
      'location': '/tmp/bar.log',
      'exceptions': 'bar|foo'
    ],
    [
      'location': '/tmp/foo.log',
      'exceptions': 'bar|foo|exception|NullPointerException'
    ]
  ]
}

```
