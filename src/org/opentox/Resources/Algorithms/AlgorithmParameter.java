package org.opentox.Resources.Algorithms;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;

/**
 * Algorithm Parameter. An algorithm parameter consists of its name, datatype
 * (according to XSD datatypes classification), its value and its scope.
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmParameter<E> {

    /**
     * The parameter's name.
     */
    public String paramName="";
    /**
     * Parameter datatype according to the <a href="http://www.w3.org/TR/xmlschema-2/">
     * XSD</a> specifications.
     */
    public XSDDatatype dataType;

    /**
     * The default value of the parameter.
     */
    public E paramValue;

    /**
     * The scope of the parameter which is either "optional" or
     * "mandatory".
     */
    public String paramScope;

    public AlgorithmParameter(
            String paramName, XSDDatatype dataType,
            E paramValue, String paramScope){
        this.dataType=dataType;
        this.paramName=paramName;
        this.paramValue=paramValue;
        this.paramScope=paramScope;
    }


}
