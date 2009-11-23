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

echo 'URI LIST OF ALL MODELS....';
curl -X GET -H 'Accept:text/uri-list' http://opentox.ntua.gr:3000/model;
echo 'URI LIST OF ALL MLR MODELS....';
curl -X GET -H 'Accept:text/uri-list' http://opentox.ntua.gr:3000/model?searchAlgorithm=mlr;
echo 'URI LIST OF ALL SVM REGRESSION MODELS....';
curl -X GET -H 'Accept:text/uri-list' http://opentox.ntua.gr:3000/model?searchAlgorithm=svm;
echo 'URI LIST OF ALL CLASSIFICATION MODELS....';
curl -X GET -H 'Accept:text/uri-list' http://opentox.ntua.gr:3000/model?searchAlgorithm=classification;
echo 'URI LIST OF ALL REGRESSION MODELS....';
curl -X GET -H 'Accept:text/uri-list' http://opentox.ntua.gr:3000/model?searchAlgorithm=regression;
echo 'XML REPRESENTATION OF THE MLR TRAINING ALGORITHM....';
curl -X GET -H 'Accept:text/xml' http://opentox.ntua.gr:3000/algorithm/learning/regression/mlr;
echo 'JSON REPRESENTATION OF THE MLR TRAINING ALGORITHM....';
curl -X GET -H 'Accept:application/json' http://opentox.ntua.gr:3000/algorithm/learning/regression/mlr;
echo 'YAML REPRESENTATION OF THE SVM TRAINING ALGORITHM....';
curl -X GET -H 'Accept:text/x-yaml' http://opentox.ntua.gr:3000/algorithm/learning/regression/svm;



################### End of shell script #################
#							#
#							#
#                    Thank you ! ! !			#
#							#
#							#
#########################################################



