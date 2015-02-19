all: compile
clean:
	@-rm -rf main
compile: clean
	@javac -d compile -classpath lib/jcommander-1.30.jar src/httpd/java/**/*.java src/httpd/java/*.java
	@echo -e 'Success!'
