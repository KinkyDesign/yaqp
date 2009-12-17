#!/bin/sh
SHA=sha512sum;
MD5=md5sum;
MD5FILE=md5sums;
SHAFILE=sha512sums;

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

for file in  `find . -type f`
 do
   if [ ! -d $file ]
   then
    $MD5 $file >> $MD5FILE;
    $SHA $file >> $SHAFILE
   fi
 done


