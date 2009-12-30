	<div id="content">
<!--
Main Body of the page
-->
		<div id="page">
			<h2>Train an SVM Regression Model</h2>
			<p align="justify">



<?php echo "<form enctype=\"x-www-form-urlencoded\" method=\"POST\" action=\"".$svmtrainurl."\">"; ?>
<br/>
Dataset to train the model<br/>
<input type="text" name="dataser_uri" value="http://opentox.ntua.gr/ds.rdf" size=35/> 
<br/><br/>
Cost<br/>
<input type="text" name="cost" value="1000"/>
<br/><br/>
Epsilon<br/>
<input type="text" name="epsilon" value="0.1"/>
<br/><br/>
SVM kernel<br/>
<select name="kernel" id="kernel" onchange="setOptions(this.value)">
<option >RBF</option>
<option >Sigmoid</option>
<option >Polynomial</option>
<option >Linear</option>
</select>
<br/><br/>
<div id="Options">
</div>
<br/><br/>
<input type="submit" value="Train Model" />
</form>

</p>
<!--
End of Main Body
-->
		</div>

