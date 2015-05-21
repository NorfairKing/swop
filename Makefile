NAME 		= swop

GROUP_NR 	= 02
GROUP 	 	= group$(GROUP_NR)

README 		= README.md

FINAL_ZIP 	= $(NAME).zip
FINAL_JAR   = system.jar
FINAL_README= README.txt

APP 		= swapp
POM_FILE 	= $(APP)/pom.xml

RESULT_DIR 	= $(GROUP)
RESULT_JAR	= $(RESULT_DIR)/system.jar
DOC_DIR 	= $(RESULT_DIR)/doc
DIAGRAM_DIR	= $(RESULT_DIR)/diagrams


all: package doc diagrams
	mkdir -p $(RESULT_DIR) $(DOC_DIR) $(DIAGRAM_DIR)
	cp swapp/target/*jar-with-dependencies.jar $(RESULT_DIR)/$(FINAL_JAR)
	cp -r swapp/src $(RESULT_DIR)
	cp -r swapp/target/site/apidocs/* $(DOC_DIR)
	find . -type f -name '*.eps' | cpio -pdm $(DIAGRAM_DIR)
	cp $(README) $(RESULT_DIR)/$(FINAL_README)
	zip -r $(FINAL_ZIP) $(RESULT_DIR)


package:
	mvn package --file $(POM_FILE) --define skipTests


doc:
	mvn javadoc:javadoc --file $(POM_FILE) --quiet --fail-never


.PHONY: diagrams
diagrams:
	$(MAKE) -C diagrams


clean:
	mvn clean -f $(POM_FILE)
	rm -rf $(RESULT_DIR) 
	rm -f $(FINAL_ZIP)
