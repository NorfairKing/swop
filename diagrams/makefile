.PHONY: class_diagrams domain_model fsms sequence_diagrams

all:
	$(MAKE) -C class_diagrams
	$(MAKE) -C sequence_diagrams
	$(MAKE) -C domain_model
	$(MAKE) -C fsms

clean:
	$(MAKE) -C class_diagrams clean
	$(MAKE) -C sequence_diagrams clean
	$(MAKE) -C domain_model clean
	$(MAKE) -C fsms clean
	git clean -f
