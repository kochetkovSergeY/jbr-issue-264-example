set JAVA_HOME=%cd%\jdk\jbr-17.0.9-windows-x64-b1087.7
%cd%\jdk\jbr-17.0.9-windows-x64-b1087.7\bin\java -Xmx256m -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8787 -XX:+AllowEnhancedClassRedefinition -XX:HotswapAgent=fatjar -XX:+UseSerialGC -XX:-ClassUnloading -jar "hotswap-app-to-update-0.0.1-SNAPSHOT.jar"
