echo "@startuml"
echo
echo "title \"Domain Class Diagram\""
echo
for f in *.ass
do
    echo "!include $f"
done
echo
echo "@enduml"
