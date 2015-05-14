if [ "$#" -ne 1 ]
then
    shift
    for var in "$@"
    do
        ./script.sh $suffix $var
    done
    exit
fi


file=$1

base=$(basename "$file")
extension="${base##*.}"
filename="${base%.*}"

uppername=${filename^^}

text="$(cat $file)"

echo $uppername

echo "
!ifndef $uppername
!define $uppername

$text

!endif
" #> $file

