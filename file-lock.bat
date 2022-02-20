@echo off
echo Java application to lock or unlock files...
:: echo  Usage:lock ( without argument ) list the status of locks.
:: echo  Lock lock [index] Lock  a file in directory A
:: echo  Lock unlock [index] Unlock  a file directory A
java -cp testapp3.jar;./lib/*;   test.LockFile %*

