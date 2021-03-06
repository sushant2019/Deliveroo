# Deliveroo Technical Task
## CronInfo - A command-line utility

This utility program prints the details of a basic standard **cron** command string. An example is shown below:

```
~$ CronInfo ＂*/15 0 1,15 * 1-5 /usr/bin/find＂
minute        0 15 30 45
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find
```

## Steps to use CronInfo on Mac/Linux

### Method 1

1. Clone this repo:
`https://github.com/sushant2019/Deliveroo.git`
2. The top-level directory **Deliveroo** contains the following files:
```
Deliveroo/
├── Deliveroo.iml
├── LICENSE
├── README
├── README.md
├── out
│   └── artifacts
│       └── Deliveroo
│           ├── Deliveroo-1.0.dmg
│           └── Deliveroo.jar
├── pom.xml
└── src
    ├── main
    │   └── java
    │       ├── META-INF
    │       │   └── MANIFEST.MF
    │       └── com
    │           └── deliveroo
    │               ├── Cron.java
    │               └── Crontab.java
    └── test
        └── java
            └── com
                └── deliveroo
                    └── CronTest.java
```
3. Open the directory **Deliveroo** in a terminal and execute the following command:
```
~$ alias CronInfo='java -jar $PWD/out/artifacts/Deliveroo/Deliveroo.jar'
```
4. Now you are ready to use the **CronInfo** utility. Another example is given below:
```
~$ CronInfo "*/15,11,19,37 5-8,0,2,*/6 21,13-15,29-31 */4 4-5,2,1 /usr/bin/find"
minute        0 11 15 19 30 37 45
hour          0 2 5 6 7 8 12 18
day of month  13 14 15 21 29 30 31
month         1 5 9
day of week   1 2 4 5
command       /usr/bin/find
```

### Method 2 - Only for Mac OS

1. Install the app from the .dmg file:
```
Deliveroo/out/artifacts/Deliveroo/Deliveroo-1.0.dmg`
```
2. Open top-level *Deliveroo* directory in a terminal and execute the command shown below:
```
~$ alias CronInfo='java -jar /Applications/Deliveroo.app/Contents/app/Deliveroo.jar'
```
3. **CronInfo** is ready to be used.
4. The alias command shown above can be put in **.bashrc** or **bash_profile** files of the user to make the **CronInfo** alias permanent so that it can be used in new instances of the terminal.

## Main Code Files

The code can be accessed at the git repo: https://github.com/sushant2019/Deliveroo.git

1. **Cron.java** - The Class that implements the parsing logic to parse the cron string command.
2. **Crontab.java** - The Class that contains the **main** function, the entry point of the application, that accepts the cron string and uses the Cron class to parse and print the result.
3. **CronTest.java** - The Cron class's JUnit tests file, that covers 100% methods and 100% lines of code.
