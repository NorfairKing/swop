
if [ "$#" -ne 1 ]
then
    echo "Compiling $# diagrams"
    counter=1
    for var in "$@"
    do
        ./compileraw.sh $var
        echo "($counter/$#): Done compiling $var"
        counter=$((counter+1))
    done
    exit
fi

file="$1"

base=$(basename "$file")
extension="${base##*.}"
filename="${base%.*}"

out_ext=eps
input_file=$base
output_file=$filename.$out_ext

start="@startuml"
end="@enduml"

text="$start\n$(cat $input_file)\n$end"
echo -e $text | plantuml -failfast -nbthread auto -p -t$out_ext > $output_file


