.PHONY: diagrams

all:
	mkdir -p pkg/
	mvn package -f swapp/
	cp swapp/target/*.jar pkg/system.jar
	cp -r swapp/src pkg/
	mvn javadoc:javadoc -f swapp/
	cp -r swapp/target/site/apidocs pkg/doc
	mkdir -p pkg/diagrams
	make diagrams
	cp diagrams/* pkg/diagrams
	ln -s pkg group02
	zip -r swop.zip group02/
	rm group02
clean:
	mvn clean -f swapp/
	rm pkg -rf
	rm -f swop.zip
