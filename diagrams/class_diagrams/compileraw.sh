out_ext=eps

suffix=$1
if [ "$#" -ne 2 ]
then
    total=$(($#-1))
    echo "Compiling $total diagrams"
    counter=1
    shift
    for var in "$@"
    do
        ./compileraw.sh $suffix $var
        echo "($counter/$#): Done compiling $var"
        counter=$((counter+1))
    done
    exit
fi

file="$2"

base=$(basename "$file")
extension="${base##*.}"
filename="${base%.*}"

input_file=$base
output_file1="${filename}_$suffix.$out_ext"
output_file2="${filename}_$suffix.png"

start="@startuml"
end="@enduml"

empty_comment="' empty line of comment"
#echo "$input_file -> $output_file"

echo -e "$start \n\n $(cat $input_file)\n$empty_comment \n\n $end" | plantuml -failfast2 -nbthread auto -p -t$out_ext > $output_file1
echo -e "$start \n\n $(cat $input_file)\n$empty_comment \n\n $end" | plantuml -failfast2 -nbthread auto -p -tpng > $output_file2
