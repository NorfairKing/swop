LATEX = ./makepdf.sh

all:
	make -C ../../diagrams/
	make text

text: iteration3.pdf

%.pdf: %.tex
	$(LATEX) $<


