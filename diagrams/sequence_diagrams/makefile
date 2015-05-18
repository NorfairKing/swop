OUT_EXT     		= eps
OUT_FORMAT  		= $(OUT_EXT)
OUT_EXT_EXTRA 		= png
OUT_FORMAT_EXTRA  	= $(OUT_EXT_EXTRA)

PLANTCC     		= plantuml

DOT_EXT     		= dot


SEQUENCE_DIAGRAMS =	\
	delegate_task.eps \
	delegated_task_status.eps \
	office_login.eps \
	simulation.eps

SEQUENCE_DIAGRAM_PNGS = \
	delegate_task.png \
	delegated_task_status.png \
	office_login.png \
	simulation.png

sequence_diagrams: $(SEQUENCE_DIAGRAMS)

extra: $(SEQUENCE_DIAGRAM_PNGS)

all: sequence_diagrams extra

%.$(OUT_EXT): %.$(DOT_EXT)
	$(PLANTCC) -t$(OUT_FORMAT) $< $@

%.$(OUT_EXT_EXTRA): %.$(DOT_EXT)
	$(PLANTCC) -t$(OUT_FORMAT_EXTRA) $< $@

DIRTY_EXT = *.$(OUT_EXT) *.$(OUT_FORMAT_EXTRA) *.pdf *.svg 
	
clean:
	rm -f $(DIRTY_EXT)
