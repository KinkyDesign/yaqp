	<div id="content">
<!--
Main Body of the page
-->
		<div id="page">
			<h2>Train an SVM Regression Model</h2>
			<p align="justify">


<?php echo "<form enctype=\"x-www-form-urlencoded\" method=\"POST\" action=\"".$mlrtrainurl."\">"; ?>
<br/>
Dataset to train the model
<br/>
<input type="text" name="dataset" value="http://opentox.ntua.gr/arff/reg"/> 
<br/><br/>
Target Feature Definition
<br/>
<input type="text" name="target" value="http://someserver.com:8080/feature_definition/445" />
<br/><br/>
<input type="submit" value="Train Model" />
</form>
</p>

<!--
End of Main Body
-->
		</div>

