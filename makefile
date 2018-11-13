JFLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	DriverProgram.java \
	Administrator.java \
	Passenger.java \
	Driver.java \
	User.java 

MAIN = DriverProgram
default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

