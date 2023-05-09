package edu.isu.archeo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;

/**
 * Hello world!
 *
 */
public class App 
{
    public static String 
        url = "https://gist.githubusercontent.com/eugeneai" +
        "/141f89eace745682ece44a9fc4245085/raw/cb84c1892b642af894ac5a9d0ec19cc177552c31/" + 
        "foaf.rdf";
    public static String
        // rdfFile = "./foaf.rdf";
        rdfFile = "./geolprocessesowl6.rdf";
    public static String
        rdfOutFile = "./foaf-out.rdf";
    public static void ontoMonad () {
        System.out.println( url );
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        OntDocumentManager manager = model.getDocumentManager();
        manager.setProcessImports(true);
        // manager.addIgnoreImport(...);
        // manager.addAltEntry(url, url);
        // model.read(url); // original download
        // BufferedReader rd = new BufferedReader(new FileReader(rdfFile));
        model.read(rdfFile);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(rdfOutFile));
            // model.write(System.out);
            model.write(writer);
            
            for (ExtendedIterator iter = model.listClasses(); iter.hasNext();) { 
                OntClass tempClass = (OntClass) iter.next();
                System.out.println(tempClass.getLocalName());
            }
            System.out.println( "Writing OK!" );
        } catch (IOException exc) {
            System.out.println( "Writing problems!" );
        }
    }
    public static void query () {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        model.read(rdfFile);
        String q1 = String.join("\n",
              "PREFIX g: <http://www.semanticweb.org/moks/ontologies/2023/1/geolprocesses#>"
            , "SELECT ?s"
            , "WHERE {"
            , "    ?s a g:Участок"
            , "}");
        Query query = QueryFactory.create(q1);
        QueryExecution qexec = QueryExecutionFactory.create(query,model);
        try {
            ResultSet results = qexec.execSelect();
            // for (;results.hasNext();) {
            //     QuerySolution sol = results.nextSolution();
            //     System.out.println(sol);
            // }
            ResultSetFormatter.out (results, query);
        } finally {
            qexec.close();
        }

    }
    public static void main( String[] args )
    {
        System.out.println( "Jena based Geoprocess query tool!" );
        ontoMonad();
        System.out.println( "------- Try queries -----" );
        query();
    }
}
