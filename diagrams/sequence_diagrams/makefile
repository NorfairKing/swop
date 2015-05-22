OUT_EXT     		= eps
OUT_FORMAT  		= $(OUT_EXT)
OUT_EXT_EXTRA 		= png
OUT_FORMAT_EXTRA  	= $(OUT_EXT_EXTRA)
DOT_EXT     		= dot


SEQUENCE_DIAGRAMS =	\
	delegate_task.eps \
	delegated_task_status.eps \
	office_login.eps \
	simulation.eps \
	commit_delegation.eps \
	status_update.eps

all: sequence_diagrams

sequence_diagrams: $(SEQUENCE_DIAGRAMS)

%.$(OUT_EXT): %.$(DOT_EXT)
	./makeplant.sh $<

DIRTY_EXT = *.$(OUT_EXT) *.$(OUT_FORMAT_EXTRA) *.pdf *.svg 
	
clean:
	rm -f $(DIRTY_EXT)
