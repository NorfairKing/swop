NAME 		= swop

GROUP_NR 	= 02
GROUP 	 	= group$(GROUP_NR)

FINAL_ZIP 	= $(NAME).zip

APP 		= swapp
POM_FILE 	= $(APP)/pom.xml

RESULT_DIR 	= $(GROUP)
RESULT_JAR	= $(RESULT_DIR)/system.jar
DOC_DIR 	= $(RESULT_DIR)/doc
DIAGRAM_DIR	= $(RESULT_DIR)/diagrams


all: package doc diagrams
	mkdir -p $(RESULT_DIR) $(DOC_DIR) $(DIAGRAM_DIR)
	cp swapp/target/*jar-with-dependencies.jar $(RESULT_DIR)
	cp -r swapp/src $(RESULT_DIR)
	cp -r swapp/target/site/apidocs/* $(DOC_DIR)
	cp -r diagrams/*.eps $(DIAGRAM_DIR)
	zip -r $(FINAL_ZIP) $(RESULT_DIR)


package:
	mvn package -f $(POM_FILE)


doc:
	mvn javadoc:javadoc -f $(POM_FILE)


.PHONY: diagrams
diagrams:
	$(MAKE) -C diagrams


clean:
	mvn clean -f $(POM_FILE)
	rm -rf $(RESULT_DIR) 
	rm -f $(FINAL_ZIP)
