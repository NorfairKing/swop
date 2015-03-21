LATEX = ./makepdf.sh

all:
	make -C ../../diagrams/
	make text

text: iteration1.pdf

%.pdf: %.tex
	$(LATEX) $<


