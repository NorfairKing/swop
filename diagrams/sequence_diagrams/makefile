OUT_EXT     = eps
OUT_FORMAT  = $(OUT_EXT)
PLANTCC     = plantuml
DOT_EXT     = dot


SEQUENCE_DIAGRAMS =	delegate_task.eps \
					delegated_task_status.eps \
					office_login.eps

sequence_diagrams: $(SEQUENCE_DIAGRAMS)

%.$(OUT_EXT): %.$(DOT_EXT)
	$(PLANTCC) -t$(OUT_FORMAT) $< $@

DIRTY_EXT = *.$(OUT_EXT) *.pdf *.svg *.png
	
clean:
	rm -f $(DIRTY_EXT)
