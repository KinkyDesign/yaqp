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
echo '';
echo -e  "\033[31m===================================================\033[0m";
echo -e '\033[32mTesting WebServices at http://opentox.ntua.gr:3000\033[0m';
echo -e "\033[31m===================================================\033[0m"
echo ''


pingServer(){
# Ping the server...
echo 'Pinging the server at : http://opentox.ntua.gr/';
ping opentox.ntua.gr -c 3 -v;
echo '';
}

testNtuaServices(){
echo -e '\033[31mTESTING ALL GETs........................................\033[0m'; 
echo -e '\033[32m### GET text/uri list of all algorithms on the server...\033[0m';
curl -v -i -H 'Accept:text/uri-list' http://opentox.ntua.gr:3000/algorithm;
echo '';

echo -e '\033[32m### Get application/rdf+xml from svm....................\033[0m';
curl -v -i -H 'Accept:application/rdf+xml' http://opentox.ntua.gr:3000/algorithm/svm;
echo '';

echo -e '\033[32m### Get application/x-turtle from mlr...................\033[0m';
curl -v -i -H 'Accept:application/x-turtle' http://opentox.ntua.gr:3000/algorithm/mlr;
echo '';

echo -e '\033[32m### Get text/rdf+n3 from svm............................\033[0m';
curl -v -i -H 'Accept:text/rdf+n3' http://opentox.ntua.gr:3000/algorithm/svm;
echo '';

echo -e '\033[32m### Get text/x-triple from svc..........................\033[0m';
curl -v -i -H 'Accept:text/x-triple' http://opentox.ntua.gr:3000/algorithm/svc;
echo '';

echo -e '\033[32m### Get application/json from mlr.......................\033[0m';
curl -v -i -H 'Accept:application/json' http://opentox.ntua.gr:3000/algorithm/mlr;
echo '';

echo -e '\033[32m### Get text/x-yaml from mlr............................\033[0m';
curl -v -i -H 'Accept:text/x-yaml' http://opentox.ntua.gr:3000/algorithm/mlr;
echo '';

echo -e '\033[32m### Get text/xml from mlr...............................\033[0m';
curl -v -i -H 'Accept:text/xml' http://opentox.ntua.gr:3000/algorithm/mlr;
echo '';

echo -e '\033[32m### Get a text/uri-list of all available models on the server...\033[0m';
curl -v -i -H 'Accept:text/uri-list' http://opentox.ntua.gr:3000/model;
echo '';

}


pingServer;
testNtuaServices;

################### End of shell script #################
#							#
#							#
#                    Thank you ! ! !			#
#							#
#							#
#########################################################



