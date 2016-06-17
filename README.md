# char-check
Command line until to check that a given file has a given set of characters.

Command line options include:
* `-u` check for the existence of all uppercase characters [A-Z]
* `-l` check for the existence of all lowercase characters [a-z]
* `-n` check for the existence of all number characters [0-9]
* `-f` (required) path to the file needing to be checked

Please note that these can be combined... to check for existence of all classes in a file named `test.txt` the command line should look something like:
```
java -jar char-check.jar -uln -f test.txt
```
