JFLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = / risingCity.java / RedBlackTree.java / BuildingPropertiesNode.java / BuildingHeap.java / IRedBlackTree.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class