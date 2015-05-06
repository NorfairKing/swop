OUT_FORMAT  = eps
OUT_EXT     = eps

GRAPHVIZCC  = dot
PLANTCC     = plantuml

DFS_SUFFIX  = dfs
UML_SUFFIX  = uml

DOT_EXT     = dot

PLANT       = 	facade_uml.eps \
				domain_model_uml.eps \
				high_level_uml.eps \
				sequence_createTask_uml.eps \
				sequence_createTask_simple_uml.eps \
				sequence_update_task_status_uml.eps \
				sequence_update_task_status_simple_uml.eps \
				sequence_planTask_uml.eps \
				sequence_planTask_simple_uml.eps
GRAPHVIZ    = 	task_dfs.eps \
				project_dfs.eps

all: plantuml_illustrations graphviz_illustrations

plantuml_illustrations: $(PLANT)

graphviz_illustrations: $(GRAPHVIZ)

%_$(DFS_SUFFIX).$(OUT_EXT): %_$(DFS_SUFFIX).$(DOT_EXT)
	$(GRAPHVIZCC) -T$(OUT_FORMAT) $< -o $@

%_$(UML_SUFFIX).$(OUT_EXT): %_$(UML_SUFFIX).$(DOT_EXT)
	$(PLANTCC) -t$(OUT_FORMAT) $< $@
