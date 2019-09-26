	How to excute it by using cmd.


First step is to use "cd" command to enter the foder:"17"	, don't enter the "application".

Second step is to input those command.

the command for bulitting is :
javac -cp .;<path of sqlite-jdbc-3.16.1.jar> application/Main.java

the cmmand to execute is:
java -cp .;<path of sqlite-jdbc-3.16.1.jar> application.Main

the jar package is in "controllers". pay attention to the space ,semicolon, and dot.

This is my example.

javac -cp .;C:\Users\hya\Desktop\17\cotrollers\sqlite-jdbc-3.16.1.jar application/Main.java

java -cp .;C:\Users\hya\Desktop\17\cotrollers\sqlite-jdbc-3.16.1.jar application.Main

You can choose using csv file or sqlite, just need to modify the file: properties.cnf. Change model either db or csv and change the file of resouce(.sqlite or .csv). Attension, if you wrrite *.csv and model db, it will be error.

You also can write those command into a .bat file, and put the file in the root dictory of folder "17".