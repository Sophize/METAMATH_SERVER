#!/bin/bash
# run:  ./extract_typesetting.sh set iset nf
for arg in "$@"
do
    echo "$arg"
done

for DATASET in "$@"
do
    INPUT=$DATASET".mm"
    OUTPUT=$DATASET"_typesetting.mm"
    echo "Reading from $INPUT ..."
    STARTLINE="$(grep "\$t" -n $INPUT | cut -f1 -d:)"
    echo $STARTLINE
    ENDLINE="$(grep "End of type" -n $INPUT | cut -f1 -d:)"
    if [ -z "$ENDLINE" ] # isEmpty
    then
        ENDLINE="\$"
    else
        ENDLINE=$(($ENDLINE+1))
    fi
    echo "Snipping lines $STARTLINE to $ENDLINE"
    echo -e "Writing to $OUTPUT\n\n"
    sed -n "$STARTLINE,$ENDLINE p" $INPUT > $OUTPUT
done