OUT_EXT     = eps
OUT_FORMAT  = $(OUT_EXT)
PLANTCC     = plantuml
UML_SUFFIX  = uml
DOT_EXT     = dot


CLASS_DIAGRAMS = 	entire_domain_uml.eps \
					task_planning_uml.eps \
					task_uml.eps

class_diagrams: $(CLASS_DIAGRAMS)

classes:
	./compileraw.sh class *.cl

associations:
	./compileraw.sh asso *.ass

all: classes associations class_diagrams

%_$(UML_SUFFIX).$(OUT_EXT): %_$(UML_SUFFIX).$(DOT_EXT)
	$(PLANTCC) -t$(OUT_FORMAT) $< $@

DIRTY_EXT = *.$(OUT_EXT) *.pdf *.svg *.png
	
clean:
	rm -f $(DIRTY_EXT)
