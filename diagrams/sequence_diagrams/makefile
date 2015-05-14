OUT_EXT     = eps
OUT_FORMAT  = $(OUT_EXT)
PLANTCC     = plantuml
UML_SUFFIX  = uml
DOT_EXT     = dot


SEQUENCE_DIAGRAMS =	delegate_task_uml.eps \
					delegated_task_status_uml.eps

sequence_diagrams: $(SEQUENCE_DIAGRAMS)

%_$(UML_SUFFIX).$(OUT_EXT): %_$(UML_SUFFIX).$(DOT_EXT)
	$(PLANTCC) -t$(OUT_FORMAT) $< $@

DIRTY_EXT = *.$(OUT_EXT) *.pdf *.svg *.png
	
clean:
	rm -f $(DIRTY_EXT)
