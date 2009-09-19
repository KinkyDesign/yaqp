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
#
#
# 1
#
echo 'Get the arff representation of a dataset... '
echo 'Press Enter to proceed'
read ans
curl -H "Accept:dataset/arff" http://opentox.ntua.gr:3000/OpenToxServices/dataset/dataSet-35
echo ''
#
#
# 2
#
echo 'Get the xrff representation of a dataset... '
echo 'Press Enter to preceed...'
read ans
curl -H "Accept:dataset/xrff" http://opentox.ntua.gr:3000/OpenToxServices/dataset/dataSet-35
echo ''
#
#
# 3
#
echo 'Get the dsd representation of a dataset... '
echo 'Press Enter to preceed...'
read ans
curl -H "Accept:dataset/dsd" http://opentox.ntua.gr:3000/OpenToxServices/dataset/dataSet-35
echo ''
#
#
# 4
#
echo 'Get the Data Dictionary of a dataset...' 
echo 'Press Enter to proceed...'
read ans
curl -H "Accept:dataset/meta" http://opentox.ntua.gr:3000/OpenToxServices/dataset/dataSet-35
echo ''
#
#
# 5
#
echo 'Retrieve the specifications for the attribute selection algorithm: infoGainAttributeEvaluation'
echo 'Press Enter to proceed...'
read ans
curl -v -X GET -H "Accept:text/xml" http://opentox.ntua.gr:3000/OpenToxServices/algorithm/preprocessing/featureselection/infoGainAttributeEvaluation
echo ''
#
#
# 6
#
echo 'Create a new dataset applying the feature selection algorithm infoGainAttrEval...'
echo 'The initial dataset contains 8 descriptors and the produced dataset will contain only 4'
echo 'Press Enter to proceed...'
read ans
curl -v -X POST -d "dataId=35&numToSel=4&threshold=-1000" http://opentox.ntua.gr:3000/OpenToxServices/algorithm/preprocessing/featureselection/infoGainAttributeEvaluation
echo ''
#
#
# 7
#
echo 'Get a list of all available algorithms...'
echo 'Press Enter to proceed...'
read ans
curl -H "Accept:text/uri-list" http://opentox.ntua.gr:3000/OpenToxServices/algorithm
echo ''
#
#
# 8
#
echo 'Get a list of all available learning algorithms...'
echo 'Press Enter to proceed...'
read ans
curl -H "Accept:text/uri-list" http://opentox.ntua.gr:3000/OpenToxServices/algorithm/learning
echo ''
#
# 
# 9
#
echo 'Get a list of all available classification algorithms...'
echo 'Press Enter to proceed...'
read ans
curl -H "Accept:text/uri-list" http://opentox.ntua.gr:3000/OpenToxServices/algorithm/learning/classification
echo ''
#
#
# 10
#
echo 'Get a list of all available regression algorithms...'
echo 'Press Enter to proceed...'
read ans
curl -H "Accept:text/uri-list" http://opentox.ntua.gr:3000/OpenToxServices/algorithm/learning/regression
echo ''
#
#
# 11
#
echo 'Get the XML representation for the classification algorithm SVC...'
echo 'Press Enter to proceed...'
read ans
curl -H "Accept:text/xml" http://opentox.ntua.gr:3000/OpenToxServices/algorithm/learning/classification/svc
echo ''
#
#
# 12
#
echo 'Build a SVM model providing the training data set id and some parameters...'
echo 'Press Enter to proceed...'
read ans
curl -X POST -d "dataId=35&kernel=rbf&gamma=1.35&cost=1000&tolerance=0.000001" http://opentox.ntua.gr:3000/OpenToxServices/algorithm/learning/classification/svc
echo ''
#
#
# 13
#
echo 'View the produced model...'
echo 'Press Enter to proceed...'
read ans
curl http://opentox.ntua.gr:3000/OpenToxServices/model/classification/svc/model-35-46
echo ''
#
#
# 14
#
echo 'Train an SVM regression model...'
echo 'Press Enter to proceed...'
read ans
curl -X POST -d "dataId=8&kernel=rbf&gamma=1.23&cost=850&tolerance=0.0001" http://opentox.ntua.gr:3000/OpenToxServices/algorithm/learning/regression/svm
echo ''
#
#
# 15
#
echo 'Train a Multiple Linear Regression model...'
echo 'Press Enter to proceed...'
read ans
curl -X POST -d  "dataId=8" http://opentox.ntua.gr:3000/OpenToxServices/algorithm/learning/regression/mlr
echo ''
#
#
# 16
#
echo 'Get the produced model in PMML format...'
echo 'Press Enter to proceed...'
read ans
curl -H "Accept:text/xml" http://opentox.ntua.gr:3000/OpenToxServices/model/regression/mlr/model-8 
echo ''
#
#
# 17
#
echo 'Validate an MLR model...'
echo 'Press Enter to proceed...'
read ans
curl -X POST -d "dataid=8&modelid=8" http://opentox.ntua.gr:3000/OpenToxServices/validation/test_set_validation/mlr
echo ''
#
#
#
#
echo 'Validate an SVM classification model...'
echo 'Press Enter to proceed...'
read ans
curl -X POST -d "dataid=35&modelid=35-18" http://opentox.ntua.gr:3000/OpenToxServices/validation/test_set_validation/svc
echo ''
#
#
################### End of shell script #################
#							#
#							#
#                    Thank you ! ! !			#
#							#
#							#
#########################################################



