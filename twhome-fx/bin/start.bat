@echo off
set JAVA_HOME=C:\dev\java\jdk-17.0.1
set JAVAFX_HOME=C:\dev\java\javafx-sdk-17.0.1

%JAVA_HOME%\bin\java -version
%JAVA_HOME%\bin\java.exe --module-path=%JAVAFX_HOME%\lib --add-modules=javafx.controls -jar ../target/twhome-fx.jar