OUT_EXT     = eps
OUT_FORMAT  = $(OUT_EXT)
GRAPHVIZCC  = dot
DFS_SUFFIX  = fsm
DOT_EXT     = dot

FSMS = 	task_fsm.eps \
		project_fsm.eps

all: $(FSMS)

%_$(DFS_SUFFIX).$(OUT_EXT): %_$(DFS_SUFFIX).$(DOT_EXT)
	$(GRAPHVIZCC) -T$(OUT_FORMAT) $< -o $@

DIRTY_EXT = *.$(OUT_EXT) *.pdf
	
clean:
	rm -f $(DIRTY_EXT)
