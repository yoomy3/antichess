#!/bin/bash

make

if [ $# -ne 1 ]
then
    echo "usage wrong, we expect only 1 argument, either WHITE or BLACK"
else
        java antiChess.AntiChess "$1"
fi
