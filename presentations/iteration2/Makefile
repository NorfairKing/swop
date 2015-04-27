LATEX = ./makepdf.sh

all:
	make -C ../../diagrams/
	make text

text: iteration2.pdf

%.pdf: %.tex
	$(LATEX) $<


