package org.opentox.formatters;


import org.opentox.Resources.Algorithms.*;
import org.opentox.formatters.Elements.*;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;

/**
 *
 * @author OpenTox - http://www.opentox.org
 * @author Sopasakis Pantelis
 * @author Sarimveis Harry
 */
public class AlgorithmTurtleFormatter extends AbstractAlgorithmFormatter{

    private static final MediaType mime = MediaType.APPLICATION_RDF_TURTLE;

    public AlgorithmTurtleFormatter(AlgorithmMetaInf metainf){
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
        builder.append("@prefix xsd: <"+xsd+"> .\n\n");

        for (int i=0;i<metainf.Parameters.length;i++){
            builder.append("_:prmnode"+i+ " rdf:type ot:Parameter ;\n");
            builder.append(DC.title+" \""+metainf.Parameters[i][0]+"\" ; \n");
            builder.append(OT.paramScope+" \""+metainf.Parameters[i][3]+"\"^^xsd:string ; \n");
            builder.append(OT.paramValue+" \""+metainf.Parameters[i][2]+"\""+"^^xsd:"+
                    metainf.Parameters[i][1]+" . \n\n");
        }
        builder.append("\n\n");

          for (int j = 0; j < metainf.statisticsSupported.size(); j++) {
            builder.append("_:stat" + j + " rdf:type ot:statistic;\n");
            builder.append("dc:title \"" + metainf.statisticsSupported.get(j) +
                    "\"^^xsd:string .\n\n");
        }

        builder.append("default:"+metainf.title+"\n");
        builder.append(RDF.type+" ot:Algorithm;\n");
        builder.append(RDFS.comment+" \"\"^^xsd:string ;\n");
        builder.append(DC.identifier+" \""+metainf.identifier+"\" ;\n");
        builder.append(DC.title+" \""+metainf.title+"\" ;\n");
        builder.append(DC.source+" \""+metainf.source+"\" ;\n");
        builder.append(DC.rights+" \""+metainf.rights+"\" ;\n");

        builder.append("ot:statisticsSupported :");
        for (int i = 0; i < metainf.statisticsSupported.size() - 1; i++) {
            builder.append("_:stat" + i + " , ");
        }
        builder.append("_:stat" + (metainf.statisticsSupported.size() - 1)+";\n");

        builder.append("ot:parameters: ");
        for (int i=0;i<metainf.Parameters.length-1;i++){
            builder.append("_:prmnode"+i+" , ");
        }
        builder.append("_:prmnode"+(metainf.Parameters.length-1));

        builder.append(" .\n");


        return new StringRepresentation(builder.toString(), mime);
    }

    
}
