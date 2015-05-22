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

all: classes associations class_diagrams

class_diagrams: $(CLASS_DIAGRAMS)

classes:
	./compileraw.sh class *.cl

associations:
	./compileraw.sh asso *.ass


%_$(UML_SUFFIX).$(OUT_EXT): %_$(UML_SUFFIX).$(DOT_EXT)
	./makeplant.sh $<


entire_domain_uml.dot:
	./build_entire_uml.sh > entire_domain_uml.dot

DIRTY_EXT = *.$(OUT_EXT) *.pdf *.svg *.png
	
clean:
	rm -f $(DIRTY_EXT)
