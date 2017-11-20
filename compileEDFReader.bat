set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.8.0_151
set PATH=C:\MyTemp\softa\jre1.8.0_151\bin;%PATH%
::javac -source 6 -target 6 timo\jyu\TRXReader.java && java -Xmx1624M timo.jyu.TRXReader "02-11-17 1300.dat"
::Downloaded commons-math3-3.6.1.jar from https://mvnrepository.com/artifact/org.apache.commons/commons-math3/3.6.1
::Downloaded jama-1.0.3.jar from https://mvnrepository.com/artifact/gov.nist.math/jama/1.0.3
javac -cp ".;src" -source 6 -target 6 -d classes src\timo\jyu\EDFReader.java
jar cf EDFReader.jar src
jar uf EDFReader.jar -C classes .
::copy lib ..\..\lib /Y
::copy lib ..\..\..\Analysis\Hookie\lib /Y
java -Xmx2000M -cp "EDFReader.jar" timo.jyu.EDFReader "./" "12-10-08.EDF"
pause