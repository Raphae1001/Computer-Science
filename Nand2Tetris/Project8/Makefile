EXEC = VMtranslator
MAIN = Main.py

all: $(EXEC)

$(EXEC): $(MAIN)
	@echo "#!/bin/bash" > $(EXEC)
	@echo "python3 $(MAIN) $$1" >> $(EXEC)
	chmod +x $(EXEC)

clean:
	rm -f $(EXEC)