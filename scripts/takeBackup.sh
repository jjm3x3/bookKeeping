java -cp Downloads/h2-1.4.192.jar org.h2.tools.Script -url jdbc:h2:~/bookKeeping -user accountant -password secret -script bookKeepingdb.bak.zip -options compression zip
