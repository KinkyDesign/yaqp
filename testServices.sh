#############################################################
#                                                           #
#                   					    #
# Sopasakis Pantelis					    #
# The purpose of this shell script is to test the           #
# RESTful web services at http://opentox.ntua.gr:3000/.	    #
# HTTP requests are performed using cURL.		    #
#							    #
#							    #
#############################################################

#
#
#
echo ''
echo '==================================================='
echo 'Testing WebServices at http://opentox.ntua.gr:3000/'
echo '==================================================='
echo ''


# Ping the server...
echo 'Pinging the server at : http://opentox.ntua.gr/';
ping opentox.ntua.gr -c 3 -v;

curl -H 'Accept:text/uri-list' http://opentox.ntua.gr:3000/algorithm;
curl -H 'Accept:application/rdf+xml' http://opentox.ntua.gr:3000/algorithm/svm;
curl -H 'Accept:application/x-turtle' http://opentox.ntua.gr:3000/algorithm/mlr;
curl -H 'Accept:text/rdf+n3' http://opentox.ntua.gr:3000/algorithm/svm;
curl -H 'Accept:text/x-triple' http://opentox.ntua.gr:3000/algorithm/svc;
curl -H 'Accept:application/json' http://opentox.ntua.gr:3000/algorithm/mlr;
curl -H 'Accept:text/x-yaml' http://opentox.ntua.gr:3000/algorithm/mlr;
curl -H 'Accept:text/xml' http://opentox.ntua.gr:3000/algorithm/mlr;



################### End of shell script #################
#							#
#							#
#                    Thank you ! ! !			#
#							#
#							#
#########################################################



