#!/bin/sh

#
# A very simple but useful script to generate MD5 and SHA512 sums
# for all the files in your project. Copy this file into the folder
# which contains the files for which you need the MD5 and SHA512 sums
# and this script will do all the hard work for you! 
#
# What it does:
#
# It iterates over all files in the folder where it is placed, including
# all files in all subfolders, calculates the MD5 and SHA512 sums and
# stores them in the files 'md5sum' and 'sha512sums'. Then include these 
# files in your project's distribution. Users of your project can check
# the integrity of the downloaded files by typing the commands:
#
#     md5sum -c md5sums | grep FAIL
#     sha512sum -c sha512sums | grep FAIL
#
#
# What it does not do:
#
# * Dish Washing, Ironing, etc...
# * Take the dog out for a walk.
# * Helps you get through your hangover.
#
# KEEP OUT OF REACH OF CHILDREN!!! IN CASE OF SWALLOWING SEEK
# FOR MEDICAL ADVICE!!!
#
# I hope you'll find that useful too!

SHA=sha512sum;
MD5=md5sum;
MD5FILE=md5sums;
SHAFILE=sha512sums;
BASE=..

if [ -e $MD5FILE ]
then
echo "Removing old file: $MD5FILE";
rm $MD5FILE;
fi

if [ -e $SHAFILE ]
then
echo "Removing the old file: $SHAFILE"
rm $SHAFILE;
fi

for file in  `find $BASE -type f`
 do
   if [ ! -d $file ]
   then
    
    $SHA $file >> $SHAFILE;
    $MD5 $file >> $MD5FILE;
   fi
 done


