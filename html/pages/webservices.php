      <div id="content">
        <!--
Main Body of the page
-->
        <div id="page">
          <h2>
             RESTful Web Services 
          </h2>
          <p align="justify">
             At first a brief description of what a RESTful web service is will be presented.
No expert knowledge is assumed nor we mean to thoroughly examine the server-client terminology. 
We start with some elementary definitions: 
A <a href="http://en.wikipedia.org/wiki/Client_(computing)">client</a> (that is practically your computer) can communicate with a <a href="http://en.wikipedia.org/wiki/Web_server">server</a> 
( that is our computer) using the well known <a href="http://www.ietf.org/rfc/rfc2616.txt">HTTP protocol</a>. 

The term <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html">request</a> refers to what the client asks the server to do and 
<a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec6.html">response</a> is what the server answers to the client. 
The information conducted between the client and the server has two parts: 
the body (content) and the <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html">header</a>. 
The body of the request or the response is what you usually see while the header contains information you usually dont see but are vital for the communication.
 A <a href="http://en.wikipedia.org/wiki/Resource_(Web)">resource</a> is any information that can be provided from the server. 
The client can perform <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html">four basic types</a> of requests, namely 
<a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.3">GET</a>, 
<a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.5">POST</a>,
<a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.6">PUT</a> and 
<a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html#sec9.3">DELETE</a>, 
also known as HTTP methods. 
Using the GET method, the client asks the server for a certain resource. 
Using the POST method the client provides some parameters to the server and the latter replies with a response according to the posted parametes. 
Using PUT the client can upload a file or in general put some content in the server. 
The DELETE method is invoked when the user needs to remove some content from the server. 
            <br/>
            <br/>
             A server must implement some of these methods. 
In fact when someone is building a web service, tells the server how to respond to a request. 
A request is applied on a certain <a href="http://www.ietf.org/rfc/rfc2396.txt">URI</a> and the server responds from the same URI, so a URI can be thought of as a communication gate between the client and the server. As mentioned above, one or more of the HTTP methods GET, POST, PUT or DELETE can be implemented on the server-side. A client is expected to know certain things to be able to use the web service: 
            <br/>
            <ol>
              <li>
                 The URI of the service. 
              </li>
              <li>
                 The HTTP methods that are implemented and what every method does. 
              </li>
              <li>
                 As far as the POST method is concerned, the client should know the names of the parameters to be posted 
              </li>
              <li>
                 Header information that the client should send with the request. 
              </li>
              <li>
                 If the resource is protected by an authentication protocol, the user should be granted a username-password pair. 
              </li>
            </ol>
             The above information are systematically provided to the client via a RESTful API. To cut a long story short, we give detailed instructions on how you can use the services we offer. The only thing you should do before proceeding to the next paragraph is to download and install <a href="">cURL</a> on your system. If you are a linux user, open a terminal and type: 
            <br/>
            <br/>
            <code>sudo apt-get isntall curl</code>
            <br/>
            <br/>
            <br/>
		</p>
          <h2>
             Summary of Services 
          </h2>
          <p align="justify">
             Let us summarize what a client can using these web services. 
            <br/>
            <ul>
              <li>
                 Upload a dataset on the server 
              </li>
              <li>
                 View an uploaded dataset in 4 different formats: arff, xrff (xml), dsd and PMML Data Dictionary (meta inf about the dataset) 
              </li>
              <li>
                 Delete an uploaded dataset (Requires Administrative Skills) 
              </li>
              <li>
                 Select attributes from a data set using the algorithm "infoGainAttributeEvaluation" thus create a new dataset containing less attributes. 
              </li>
              <li>
                 Train a Support Vector Machine (SVM) classification model. 
              </li>
              <li>
                 Get the parameters of a trained SVM model. 
              </li>
              <li>
                 Delete a trained SVM model (Requires Administrative Skills). 
              </li>
              <li>
                 Evaluate a trained SVM classification model using an uploaded dataset. 
              </li>
              <li>
                 Train a Support Vector Machine (SVM) regression model or a Multiple Linear Regression (MLR) model. 
              </li>
              <li>
                 Get the parameters of a trained regression model. 
              </li>
              <li>
                 Delete a trained regression SVM or MLR model (Requires Administrative Skills). 
              </li>
              <li>
                 Evaluate a trained regression model using a dataset. 
              </li>
              <li>
                 Delete a validation result. 
              </li>
            </ul>
             To start using these services read this <a href="../guide">guide</a> or visit our <a href="../interface">interfaces</a>
          </p>
          <!--
	End of Main Body
	-->
        </div>
