package org.opentox.Resources.Algorithms;


import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmTurtleFormater extends AbstractAlgorithmFormater{
    
//      _:b1  rdf:type ot:Parameter ;
//       dc:title "Pruning"^^xsd:string ;
//       ot:paramScope "mandatory"^^xsd:string ;
//       ot:paramValue "-P"^^xsd:string .
//
//	_:b2  rdf:type ot:Parameter ;
//	      dc:title "Max number of instances in a node"^^xsd:string ;
//	      ot:paramScope "optional"^^xsd:string ;
//	      ot:paramValue "-M 10"^^xsd:string .
//


    private static final MediaType mime = MediaType.APPLICATION_RDF_TURTLE;

    public AlgorithmTurtleFormater(AlgorithmMetaInf metainf){
        super();
        super.metainf = metainf;
    }
    
    @Override
    public StringRepresentation getStringRepresentation() {
        StringBuilder builder = new StringBuilder();
        builder.append("@prefix dc: <"+dc+"> .\n");
        builder.append("@prefix default: <"+defaultNs +"> .\n");
        builder.append("@prefix ot: <"+ot +"> .\n");
        builder.append("@prefix owl: <"+owl +"> .\n");
        builder.append("@prefix rdf: <"+rdf  +"> .\n");
        builder.append("@prefix rdfs: <"+rdfs +"> .\n");
        builder.append("@prefix protege: <"+protege +"> .\n");
        builder.append("@prefix xsd: <"+xsd+"> .\n");
        for (int i=0;i<metainf.getParameters().length;i++){
            builder.append("_:prmnode"+i+ " rdf:type ot:Parameter ;\n");
            builder.append("dc:title \""+metainf.getParameters()[i][0]+"\" ; \n");
            builder.append("ot:paramScope \""+metainf.getParameters()[i][3]+"\"^^xsd:"+
                    metainf.getParameters()[i][1]+" ; \n");
            builder.append("ot:paramValue \""+metainf.getParameters()[i][2]+"\""+"^^xsd:"+
                    metainf.getParameters()[i][1]+" . \n");
        }

        builder.append("default:"+metainf.getTitle()+"\n");
        builder.append("rdf:type ot:Algorithm;\n");
        builder.append("rdfs:comment \"\"^^xsd:string ;\n");
        builder.append("dc:identifier \""+metainf.getIdentifier()+"\" ;\n");
        builder.append("dc:title \""+metainf.getTitle()+"\" ;\n");
        builder.append("ot:parameters: ");
        for (int i=0;i<metainf.getParameters().length-1;i++){
            builder.append("_:prmnode"+i+" , ");
        }
        builder.append("_:prmnode"+metainf.getParameters().length);
        builder.append(" .\n");


        return new StringRepresentation(builder.toString(), mime);
    }

    
}
