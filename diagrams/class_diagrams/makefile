OUT_EXT     = eps
OUT_FORMAT  = $(OUT_EXT)
PLANTCC     = plantuml
UML_SUFFIX  = uml
DOT_EXT     = dot


CLASS_DIAGRAMS = entire_domain_uml.eps

all: $(CLASS_DIAGRAMS)

%_$(UML_SUFFIX).$(OUT_EXT): %_$(UML_SUFFIX).$(DOT_EXT)
	$(PLANTCC) -t$(OUT_FORMAT) $< $@

DIRTY_EXT   = *.$(OUT_EXT)
	
clean:
	rm -f $(DIRTY_EXT)
