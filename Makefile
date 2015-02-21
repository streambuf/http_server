all: compile
clean:
	@-rm -rf main
compile: clean
	@javac -d compile -classpath lib/jcommander-1.30.jar:lib/logback-classic-1.1.2.jar:lib/logback-core-1.1.2.jar:lib/slf4j-api-1.7.10.jar src/httpd/java/**/**/**/*.java src/httpd/java/**/**/*.java src/httpd/java/*.java
	@jar -cvfm httpd.jar MANIFEST.MF -C compile/ .
	@echo -e 'Success!'
