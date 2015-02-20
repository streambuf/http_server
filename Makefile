all: compile
clean:
	@-rm -rf main
compile: clean
	@javac -d . -classpath lib/jcommander-1.30.jar src/httpd/java/**/*.java src/httpd/java/*.java
	@jar -cvfm httpd.jar MANIFEST.MF *.class **/*.class
	@echo -e 'Success!'
