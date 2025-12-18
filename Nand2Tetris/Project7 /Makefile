# Variables
JAVAC=javac
JAVACFLAGS=-encoding ISO-8859-1
SRCS=*.java
EXEC=VMtranslator

# Règle par défaut
all: compile

# Règle de compilation
compile:
	$(JAVAC) $(JAVACFLAGS) $(SRCS)
	chmod +x $(EXEC)

# Nettoyage (optionnel)
clean:
	rm -f *.class