OUT_FORMAT  = eps
OUT_EXT     = eps

GRAPHVIZCC  = dot
PLANTCC     = plantuml

DFS_SUFFIX  = dfs
UML_SUFFIX  = uml

DOT_EXT     = dot

PLANT       = 	diagram_uml.eps \
				high_level_uml.eps \
				sequence_createProject_uml.eps \
				sequence_createProjectPrime_uml.eps
GRAPHVIZ    = task_dfs.eps

all: plantuml_illustrations graphviz_illustrations

plantuml_illustrations: $(PLANT)
graphviz_illustrations: $(GRAPHVIZ)

%_dfs.eps: %.dot
	dot -Teps %< -o $@

%_$(DFS_SUFFIX).$(OUT_EXT): %_$(DFS_SUFFIX).$(DOT_EXT)
	$(GRAPHVIZCC) -T$(OUT_FORMAT) $< -o $@

%_$(UML_SUFFIX).$(OUT_EXT): %_$(UML_SUFFIX).$(DOT_EXT)
	$(PLANTCC) -t$(OUT_FORMAT) $< $@
