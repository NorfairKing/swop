if [ "$#" -ne 1 ]
then
    shift
    for var in "$@"
    do
        ./script.sh $var
    done
    exit
fi


file=$1

base=$(basename "$file")
extension="${base##*.}"
filename="${base%.*}"

uppername="${filename^^}_${extension^^}"

text="$(cat $file)"

echo "
!ifndef $uppername
!define $uppername

$text

!endif
" > $file

