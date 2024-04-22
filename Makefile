CC = javac

PROGS = $(CLIENT) $(SERVER)
MISP.class: InstructionArray.class PairLable.class OpcodeConverter.class
	$(CC) MIPS.java
InstructionArray.class:
	$(CC) InstructionArray.java
PairLable.class:
	$(CC) PairLable.java
OpcodeConverter.class:
	$(CC) OpcodeConverter.java

clean :
	rm *.class