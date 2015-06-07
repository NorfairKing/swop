.PHONY: asset

OUTPUT_NAME = iteration3
MAIN_NAME = iteration3

all: text

text: graphics $(OUTPUT_NAME).pdf

graphics:
	./generate.sh



$(OUTPUT_NAME).pdf: individuals $(MAIN_NAME).tex
	latexmk -pdf -pdflatex="pdflatex -shell-escape -halt-on-error -enable-pipes -enable-write18" -use-make $(MAIN_NAME).tex

individuals: *.tex
