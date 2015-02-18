.PHONY: diagrams

all:
	mkdir -p pkg/
	mvn package -f swapp/
	cp swapp/target/*.jar pkg/system.jar
	cp -r swapp/src pkg/
	mvn javadoc:javadoc -f swapp/
	mkdir -p pkg/doc
	cp -r swapp/target/site/apidocs/* pkg/doc
	mkdir -p pkg/diagrams
	dot -Teps diagrams/task_dfs.dot -o diagrams/task_dfs.eps
	cp diagrams/*.eps pkg/diagrams/
	ln -s pkg group02
	zip -r swop.zip group02/
	rm group02
clean:
	mvn clean -f swapp/
	rm pkg -rf
	rm -f diagrams/*.eps
	rm -f swop.zip
