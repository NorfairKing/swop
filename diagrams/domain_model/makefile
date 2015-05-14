OUT_EXT     = eps
OUT_FORMAT  = $(OUT_EXT)
PLANTCC     = plantuml
UML_SUFFIX  = uml
DOT_EXT     = dot

DOMAIN_MODEL = domain_model_uml.eps

all: $(DOMAIN_MODEL)

%_$(UML_SUFFIX).$(OUT_EXT): %_$(UML_SUFFIX).$(DOT_EXT)
	$(PLANTCC) -t$(OUT_FORMAT) $< $@

DIRTY_EXT = *.$(OUT_EXT) *.pdf
	
clean:
	rm -f $(DIRTY_EXT)
