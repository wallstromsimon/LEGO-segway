cd C:\Users\simon\Documents\GitHub\LEGO-segway\java_segway\src

call nxjc java_segway/*.java
call nxjlink -o HelloWorld.nxj java_segway/helloworld
call nxjupload -r HelloWorld.nxj
