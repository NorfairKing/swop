OUT_EXT     		= eps
OUT_FORMAT  		= $(OUT_EXT)
OUT_EXT_EXTRA 		= png
OUT_FORMAT_EXTRA 	= $(OUT_EXT_EXTRA)

UML_SUFFIX  		= uml
DOT_EXT     		= dot

PLANTCC     		= plantuml


CLASS_DIAGRAMS = \
	entire_domain_uml.eps \
	task_planning_uml.eps \
	delegate_task_uml.eps \
	task_status_uml.eps \
	simulation_uml.eps \
	top_level_uml.eps \
	office_login_uml.eps \
	reservables_uml.eps

CLASS_DIAGRAM_PNGS = \
	entire_domain_uml.png \
	task_planning_uml.png \
	delegate_task_uml.png \
	task_status_uml.png \
	simulation_uml.png \
	top_level_uml.png \
	office_login_uml.png \
	reservables_uml.png


class_diagrams: $(CLASS_DIAGRAMS)

classes:
	./compileraw.sh class *.cl

associations:
	./compileraw.sh asso *.ass

extra: $(CLASS_DIAGRAM_PNGS)

all: classes associations class_diagrams extra

%_$(UML_SUFFIX).$(OUT_EXT): %_$(UML_SUFFIX).$(DOT_EXT)
	$(PLANTCC) -t$(OUT_FORMAT) $< $@

%_$(UML_SUFFIX).png: %_$(UML_SUFFIX).$(DOT_EXT)
	$(PLANTCC) -tpng $< $@

DIRTY_EXT = *.$(OUT_EXT) *.pdf *.svg *.png
	
clean:
	rm -f $(DIRTY_EXT)
