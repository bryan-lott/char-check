# char-check

## Requirements
* Java v1.7+

## Usage
```
Usage: java -jar char-check.jar [OPTION]... [FILE]
Check for the existence of character classes in FILE or standard input.

  -u, --upper        Check for uppercase letters [A-Z]
  -l, --lower        Check for lowercase letters [a-z]
  -n, --number       Check for numbers [0-9]
  -6, --hex          Check for hexidecimal numbers [0-9a-f]
  -p, --punctuation  Check for common punctuation [.,?!&-'";:]
  -s, --symbol       Check for symbols [`~!@#$%^&_-+*/=(){}[]|\:;"'<,>.?}]
  -h, --help
With no FILE, read standard input.

Examples:
    java -jar char-check.jar -l test_file.txt
    echo "abcdefghijklmnopqrstuvwxy" | java -jar char-check.jar -l
```

## Examples
Please note that these can be combined... to check for existence of upper and lowercase letters and digits in a file named `testdata.txt` the command line should look something like:
```bash
java -jar char-check.jar -uln testdata.txt
```

Alternatively for streaming from stdin:
```bash
cat testdata.txt | java -jar char-check.jar -uln
```
