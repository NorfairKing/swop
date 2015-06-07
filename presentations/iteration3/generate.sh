
OUT="graphics.tex"
rm -f $OUT

graphic () {

    file="$1"
    echo "
    \\begin{frame}
    \\begin{center}
    \\includegraphics[width=\\textwidth,height=0.8\\textheight,keepaspectratio]{$file}
    \\end{center}
    \\end{frame}
    " >> $OUT
}

for f in ../../diagrams/*/*.eps
do
    graphic "$f"
done
