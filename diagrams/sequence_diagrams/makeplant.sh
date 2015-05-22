input_file="$1"
base=$(basename "$input_file")
extension="${base##*.}"
filename="${base%.*}"

plantuml -teps "$filename.dot" "$filename.eps"
plantuml -tpng "$filename.dot" "$filename.png"
