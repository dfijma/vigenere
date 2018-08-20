# Classic weakness in the Vigenère cipher
## Let's encrypt, decrypt and analyse

This is a toy implementation, to demonstrate that the weakness as described on [wikipedia](https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher) actually works. 
My daughter's math teacher did not believe it.

```
$ ./gradlew installDist
$ ./build/install/vigenere/bin/vigenere 
usage: vigenere
 -a         analyse
 -d         decrypt
 -e         encrypt
 -k <key>   key
$ ./build/install/vigenere/bin/vigenere -e -k VERYSECRETKEY <example-text/uitvreter.txt >encrypted.txt
encrypting using key: VERYSECRETKEY
$ ./build/install/vigenere/bin/vigenere -a <encrypted.txt 
analysing:
possible key length: 13, key: VERYSECRETKEY
```

TADA!
