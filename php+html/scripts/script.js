var linearHtml = '';
var polynomialHtml = 'Gamma<br/>\n<input type="text" name="gamma" value="1.5"/><br/>\n<br/>\nDegree<br/>\n<input type="text" name="degree" value="3"/><br/>\n<br/>\nBias<br/>\n<input type="text" name="coeff0" value="0" />';
var rbfHtml = 'Gamma<br/>\n<input type="text" name="gamma" value="1.5"/>';
var sigmoidHtml = 'Gamma<br/>\n<input type="text" name="gamma" value="1.5"/><br/><br/>\nBias<br/>\n<input type="text" name="coeff0" value="0"/>';
var generalHtml = '';

window.onload=function(){setOptions(document.getElementById("kernel").value);};

function setOptions(kernel){
    if (kernel == "Linear"){
        document.getElementById("Options").innerHTML=linearHtml;
    } else if (kernel == "Polynomial") {
        document.getElementById("Options").innerHTML=polynomialHtml;
    } else if (kernel == "RBF"){
        document.getElementById("Options").innerHTML=rbfHtml;
    } else if (kernel == "Sigmoid") {
        document.getElementById("Options").innerHTML=sigmoidHtml;
    } else {
        document.getElementById("Options").innerHTML=generalHtml;
    }
}

function searchfocus(){
    document.getElementById("searchfield").value="";
}
