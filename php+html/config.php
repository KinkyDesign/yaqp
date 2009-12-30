<?php

    $baseurl = "http://opentox.ntua.gr/";
    $servicesurl = "http://opentox.ntua.gr:3000";
    $algorithmurl= $servicesurl."/algorithm";

 $learningurl = $algorithmurl."DEPRECATED!!!!"; 
 $classificationurl = $learningurl."DEPRECATED!!!!";
 $regressionurl = $learningurl."DEPRECATED!!!!";

    $svctrainurl = $algorithmurl."/svc";
    $svmtrainurl = $algorithmurl."/svm";
    $mlrtrainurl = $algorithmurl."/mlr";

    $modelurl = $servicesurl."/model"; 

    $svcmodelurl = $modelurl."?searchAlgorithm=svc";
    $svmmodelurl = $modelurl."?searchAlgorithm=svm";
    $mlrmodelurl = $modelurl."?searchAlgorithm=mlr";

    $github = "http://github.com/sopasakis/yaqp";

    $pages_size = 8;

    $pages[0][0] = "home";
    $pages[0][1] = "home.php";

    $pages[1][0] = "webservices";
    $pages[1][1] = "webservices.php";

    $pages[2][0] = "guide";
    $pages[2][1] = "guide.php";

    $pages[3][0] = "links";
    $pages[3][1] = "links.php";

    $pages[4][0] = "interface";
    $pages[4][1] = "interface.php";

    $pages[5][0] = "trainsvc";
    $pages[5][1] = "trainsvc.php";
 
    $pages[6][0] = "trainsvm";
    $pages[6][1] = "trainsvm.php";

    $pages[7][0] = "trainmlr";
    $pages[7][1] = "trainmlr.php";
?>

