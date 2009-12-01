package org.opentox.util.converters;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.util.IndentedWriter;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import weka.core.Instances;
import java.io.BufferedWriter;
import java.util.ArrayList;
import org.opentox.formatters.NameSpaces.OT;
/**
*
* @author OpenTox - http://www.opentox.org
* @author Sopasakis Pantelis
* @author Sarimveis Harry
* @author Kolotouros Dimitris
*/
public class Converter extends AbstractConverter{

    private static final long serialVersionUID = 85251113527439L;

    public void Converter(){
    }

    
    @Override
    public void convert(final Instances instances, File dsdFile) {
        int i, j, targetIdx;

        StringBuilder DsdString = new StringBuilder();


        targetIdx = instances.classIndex();
        System.out.println(targetIdx);
        for(i=0; i<instances.numInstances(); i++){
            DsdString.append(instances.instance(i).value(targetIdx) + " ");

            for(j=0; j<targetIdx; j++){
                DsdString.append((j+1) + ":" + instances.instance(i).value(j) + " ");
            }
            for(j=targetIdx+1; j<instances.numAttributes(); j++){
                DsdString.append(j + ":" + instances.instance(i).value(j));
                if(j!=instances.numAttributes()){
                    DsdString.append(" ");
                }
            }
            DsdString.append("\n");
        }

        try {

            FileWriter fstream = new FileWriter(dsdFile);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(DsdString.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void convert(final File dsdFile, Instances instances) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    @Override
    public void convert(InputStream input_RDF_file, Instances instances) {

        com.hp.hpl.jena.rdf.model.Model model =
                com.hp.hpl.jena.rdf.model.ModelFactory.createDefaultModel();

        model.read(input_RDF_file, null);

        Query query = QueryFactory.make();

        query.setQueryType(Query.QueryTypeSelect);

        // Build pattern
        ElementGroup elg = new ElementGroup();

        Var varEntry = Var.alloc("dataEntry");
        Var varX = Var.alloc("x");

        // ?x ot:dataEntry ?entry
        Triple triple = new Triple(varX, OT.dataEntry.asNode(),  varEntry);

        elg.addTriplePattern(triple);
        query.setQueryPattern(elg);
        query.addResultVar(varEntry);

        QueryExecution qexec = QueryExecutionFactory.create(query, model) ;

        try {
            // Assumption: it's a SELECT query.
            ResultSet rs = qexec.execSelect() ;

            // The order of results is undefined.
            System.out.println("DataEntries......... ") ;
            

            for ( ; rs.hasNext() ; )
            {
                QuerySolution rb = rs.nextSolution() ;

                // Get title - variable names do not include the '?' (or '$')
                RDFNode x = rb.get("dataEntry") ;


                // Check the type of the result value                                    
                    System.out.println("    " + x) ;

            }
        }
        finally {
            // QueryExecution objects should be closed to free any system resources
            qexec.close() ;
        }

    }


    public static void main(String[] args){
         InputStream in = FileManager.get().open(System.getProperty("user.home")+"/Desktop/small.rdf");
         Converter cvrtr = new Converter();
         cvrtr.convert(in, null);
    }


}
