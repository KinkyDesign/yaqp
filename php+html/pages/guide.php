 <div id="content">
        <!--
Main Body of the page
-->
        <div id="page">
          <div id="intro">
            <h2>
               Complete Guide to the Services 
            </h2>
            <p align="justify">
               A detailed guide to the services is provided. This is intended to help both experienced and begineer users. For all implemented GET methods over a URI we provide the supported mediatypes (e.g. HTML, XML). For all implemented POST methods we provide the set of parameters and their default values. Delete methods always assume administrative skills. The guide consists of seven parts: 
            </p>
            <ol>
              <li>
                <a href="#sec1">Classification Models</a>
              </li>
              <li>
                <a href="#sec2">Regression Models</a>
              </li>
              <li>
                <a href="#sec3">Operations involving Models</a>
              </li>
            </ol>
            <br/>
            <br/>
            <br/>
            <br/>
          </div>
          <div id="sec1" >
            <h2>
              <!-- Section : 3 - Classification Models
-->1. Classification Models 
            </h2>
            <p>
               List of involved URIs: 
            </p>
            <ol>
              <li>
                 <? echo($algorithmurl) ?>
              </li>
              <li>
                 <? echo($classificationurl) ?>
              </li>
              <li>
                 <? echo($svctrainurl) ?>
              </li>
            </ol>
            <br/>
            <br/>
            <h3>
               cURL commands 
            </h3>
            <br/>
            <p align="justify">
               Get a URI list of all available algorithms (including classification, regression and preprocessing). 
              <br/>
              <br/>
              <code>curl -v -X GET -H &quot;Accept:text/uri-list&quot; <? echo ($algorithmurl)?> </code>
              <br/>
               Get a URI list of all available learning algorithms 
              <br/>
              <br/>
              <code>curl -v -X GET -H &quot;Accept:text/uri-list&quot; <?echo($learningurl)?> </code>
              <br/>
               Get a URI list of all available classification algorithms 
              <br/>
              <br/>
              <code>curl -v -X GET -H &quot;Accept:text/uri-list&quot; <? echo ($classificationurl)?> </code>
              <br/>
               Get the XML representation of a classification algorithm (see the above line for a list of all available classification algorithms) 
              <br/>
              <br/>
              <code>curl -v -X GET -H &quot;Accept:text/xml&quot; <?echo($svctrainurl)?> </code>
              <br/>
               In order to train an SVM classification model you have to provide a set of parameters. The are the id of an uploaded dataset (<em>dataId</em>) which is a positive integer that must correspond to an uploaded file (e.g. instead of http://opentox.ntua.gr:3000/OpenToxServices/dataset/dataSet-35 the dataId is just 35), the <em>kernel</em> of the SVM (choose among rbf, linear, polynomial or sigmoid), the <em>gamma</em> parameter which is a positive real number, <em>coeff0</em> which is a positive integer. In case you use the polynomial kernel you have to provide the <em>degree</em> of the kernel which is a positive integer. The <em>cost</em> is a strictly positive real number that is advisable to be chosen large enough (usually 1000 is far enough) but not extremely(!) large (not 10000000) because this would cause arithmetic instability. The <em>tolerance</em> of the learning algorithm can be adjusted. You can also modify the <em>cacheSize</em> of the algorithm in MB to accelerate the process (default 50MB). For more information get the specifications of the algorithm in XML fomat <a href="<? echo($svctrainurl)?>">here</a>. Here is an example of how you can train an svm model: 
              <br/>
              <br/>
              <code>curl -v -H &quot;Accept:text/plain&quot; -X POST -d &quot;dataId=35&amp;cost=500&amp;kernel=rbf&amp;gamma=1.5&amp;tolerance=0.00001&amp;cacheSize=100&quot; <?echo($svctrainurl)?> </code>
              <br/>
               It is mandatory to post a valid <em>dataId</em> parameter which corresponds to an uploaded dataset. However, if you dont post any of the other parameters, a default value will be assigned to it. For example: 
              <br/>
              <br/>
              <code> curl -v -H &quot;Accept:text/plain&quot; -X POST -d &quot;dataId=35&amp;cost=500&amp;gamma=1.5&amp;tolerance=0.00001&amp;cacheSize=100&quot; <?echo($svctrainurl)?> </code>
              <br/>
              <a href="#page">(Back to Top)</a>
              <br/>
              <br/>
              <br/>
              <br/>
            </p>
          </div>
          <div id="sec2">
            <h2>
               2. Regression Models 
            </h2>
            <p>
               List of involved URIs: 
            </p>
            <ol>
              <li>
                 <?echo ($algorithmurl)?>
              </li>
              <li>
                 <? echo($regressionurl)?>
              </li>
              <li>
                 <?echo($svmtrainurl)?>
              </li>
              <li>
                 <?echo($mlrtrainurl)?>
              </li>
            </ol>
            <br/>
            <h3>
               cURL commands 
            </h3>
            <br/>
            <p>
               Get a URI list of all available regression algorithms 
              <br/>
              <br/>
              <code>curl -v -X GET -H &quot;Accept:text/uri-list&quot; <?echo($regressionurl)?> </code>
              <br/>
               Get the XML representation of a regression algorithm (see the above line for a list of all available regression algorithms) 
              <br/>
              <br/>
              <code>curl -v -X GET -H &quot;Accept:text/xml&quot; <?echo($svmtrainurl)?> curl -v -X GET -H &quot;Accept:text/xml&quot; <?echo($mlrtrainurl)?> </code>
              <br/>
               Train an SVM regression model. The posted paramteres are identical to the ones mentioned for the case of SVM classifier. The only difference is that the dataset should have a target of type <em>real</em> and not <em>categorical</em>. Take a look at the XML representation of the algorithm <a href="../OpenToxServices/algorithm/learning/regression/svm">here</a>
              <br/>
              <br/>
              <code>curl -v -H &quot;Accept:text/plain&quot; -X POST -d &quot;dataId=8&amp;cost=500&amp;kernel=rbf&amp;gamma=1.5&amp;tolerance=0.00001&amp;cacheSize=100&quot; http://opentox.ntua.gr:3000/OpenToxServices/algorithm/learning/regression/svm </code>
              <br/>
               Train a Multiple Linear Regression model. No algorithm-specific parameters have to be posted. Take a look at the XML representation of the algorithm <a href="../OpenToxServices/algorithm/learning/regression/mlr">here</a>. 
              <br/>
              <br/>
              <code>curl -v -H &quot;Accept:text/plain&quot; -X POST -d &quot;dataId=8&quot; http://opentox.ntua.gr:3000/OpenToxServices/algorithm/learning/regression/mlr </code>
            </p>
            <br/>
            <a href="#page">(Back to Top)</a>
            <br/>
            <br/>
            <br/>
            <br/>
          </div>
<!--
Section : 5 - Operations Involving Models
-->
          <div id="sec3">
            <h2>
               3. Operations involving Models 
            </h2>
            <p>
               List of involved URIs: 
            </p>
            <ol>
              <li>
                  
              </li>
              <li>
                <?echo($modelurl)?>
              </li>
              <li>
                 <?echo($modelurl)?>?searchAlgorithm=<em>keyword</em>
              </li>
              <li>
                 <?echo($modelurl)?>/{model_id}
              </li>
            </ol>
            <br/>
            <h3>
               cURL commands 
            </h3>
            <br/>
             Get an XML listing all available models (both classification and regression). 
            <br/>
            <br/>
            <code> curl -H "Accept:text/xml" <?echo($modelurl)?> </code>
            <br/>
             Get a URI-list of all available classification models
            <br/>
            <br/>
            <code> curl -H "Accept:text/xml" <?echo($modelurl)?>?searchAlgorithm=classification </code>
            <br/>
             Get a URI-list of all available MLR  models
            <br/>
            <br/>            
            <code> curl -H "Accept:text/xml" <? echo ($modelurl)?>?searchAlgorithm=mlr </code>
            <br/>
             Delete a model (Only administrators are authorized to delete models)
            <br/>
            <br/>
            <code> curl -X DELETE -u admin:pass <?echo($modelurl)?>/{model_id}
                    <br/>
                   curl -X DELETE -u admin:pass <?echo($modelurl)?>/{model_id} 
            </code>
<br/>
            <a href="#page">(Back to Top)</a>
<br/><br/><br/><br/>
          </div>

          <!--
End of Main Body
-->
        </div>
